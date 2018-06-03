package porprezhas.model.cards;
import porprezhas.model.dices.Board;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.DiceBag;
import porprezhas.model.dices.DraftPool;
import porprezhas.model.track.RoundTrack;


import java.io.Serializable;

import static porprezhas.model.cards.Card.Effect.*;
import static porprezhas.model.dices.Board.COLUMN;
import static porprezhas.model.dices.Board.ROW;
import static porprezhas.model.dices.Dice.ColorDice.*;



public class ToolCard extends Card implements Serializable {


    private int tokensQuantity;

    private  Dice.ColorDice cardColor;

    Dice.ColorDice[] cardColors={PURPLE,BLUE,RED,YELLOW,GREEN,PURPLE,BLUE,RED,YELLOW,GREEN,PURPLE,BLUE};

    public ToolCard(Effect effect) {
        super(effect);
        //if(effect.ID<16) throw  new InvalidIdException();

        this.tokensQuantity = 0;
        this.cardColor = cardColors[this.effect.ID -1];
    }


    public void addTokens(){

        tokensQuantity = tokensQuantity + 1;
    }


    @Override
    public int apply(Board board) {
        return 0;
    }


    public boolean verify(int cardId) {
        if(this.effect.ID != cardId) {
            System.err.println("A Error has Occurred with tool card N." + (cardId));
            return false;
        } else
            return true;
    }

    // Effect of tool card N.1
    // @Param bIncDec   bIncDec == true → increment,
    //                  bIncDec == false → decrement the number of dice
    // @Return true     means the tool card effect has verified, uses token
    //         false    means this operation is invalid, refund or not use tokens
    public boolean use(DraftPool draftPool, int indexChosenDice, boolean bIncDec) {
        //Verify the card identity
        if(!verify(TC1.ID))
            return false;

        Dice diceChosen = draftPool.diceList().get(indexChosenDice);
        int diceNumber = diceChosen.getDiceNumber();
        Dice.ColorDice diceColor = diceChosen.getColorDice();

        if(diceNumber != 6  &&  bIncDec) {
            draftPool.setDice( indexChosenDice,
                    new Dice( diceColor, diceNumber + 1));
        }
        else if(diceNumber != 1  &&  !bIncDec) {
            draftPool.setDice( indexChosenDice,
                    new Dice( diceColor, diceNumber - 1));
        } else
            return false;
        return true;
    }

    // Effect of tool card N.2-3, used by card n.4 too
    public boolean use(Board board, int rowStart, int columnStart, int rowDestination, int columnDestination, Board.Restriction restriction) {
        if( (verify(TC2.ID)  &&  restriction == Board.Restriction.COLOR) ||
            (verify(TC3.ID)  &&  restriction == Board.Restriction.NUMBER) ||
            (verify(TC4.ID) &&  restriction == Board.Restriction.DICE)) {

            if(board.canBeRemoved(rowStart, columnStart)) {
                // remove and save the dice
                Dice removedDice = board.removeDice(rowStart, columnStart);

                // if the insertion is not a valid move, then we redo the remove
                if(false == board.insertDice(removedDice, rowDestination, columnDestination, restriction)) {
                    board.insertDice(removedDice, rowStart, columnStart, Board.Restriction.NONE);
                    return false;
                }
            }
        }
        return false;
    }

    // Effect of tool card N.4: Lathekin
    // Move 2 dices
    public boolean use(Board board, int rowStart1, int columnStart1, int rowDestination1, int columnDestination1, int rowStart2, int columnStart2, int rowDestination2, int columnDestination2) {
        if(!verify(TC4.ID))
            return false;

        Dice storeDice1 = board.getDice(rowStart1, columnStart1);
        if( !use(board, rowStart1, columnStart1, rowDestination1, columnDestination1, Board.Restriction.DICE) ) {
            return false;
        }
        if( !use(board, rowStart2, columnStart2, rowDestination2, columnDestination2, Board.Restriction.DICE) ) {
            // redo the insert done by first dice, in case of failure
            board.removeDice(rowStart1, columnStart1);
            board.insertDice(
                    storeDice1,
                    rowStart1, columnStart1, Board.Restriction.NONE);
            return false;
        }
        return true;
    }

    // Effect of tool card N.5
    public boolean use(DraftPool draftPool, int indexDiceDraftPool, RoundTrack roundTrack, int indexRound,  Dice diceRoundTrack) {
        //remove the dice from the roundTrack
        roundTrack.removeDice(indexRound, diceRoundTrack);
        //add the dice removed from the roundTrack to the draftPool and replace the old dice in the roundTrack
        // with the dice in the chosen position
        roundTrack.addDice(indexRound,
                draftPool.diceSubstitute(diceRoundTrack, indexDiceDraftPool));
        return true;
    }

    // Effect of tool card N.6
    // rolls a chosen dice from the draftPool and,
    // if it cannot be placed in the board, re-adds it to the draftPool
    // @Return mustBePlaced true means the Caller MUST insert this dice in board
    public boolean use (DraftPool draftPool, int indexDiceDraftPool, Board board) {
        Dice rolledDice;
        rolledDice = draftPool.chooseDice(indexDiceDraftPool);
        rolledDice.roll();

        boolean canBePlaced = false;
        for (int row = 0; row < ROW  &&  !canBePlaced; row++) {
            for (int col = 0; col < COLUMN  &&  !canBePlaced; col++) {
                if (board.validMove(rolledDice, row, col)) {
                    canBePlaced = true;
                }
            }
        }
        if(!canBePlaced)
            draftPool.addDice(rolledDice);

        return canBePlaced;
    }

    // Effect of tool card N.7
    // this card can be used only on the second turn before drafting
    // NOTE: This constraint is Caller job
    public void use(DraftPool draftPool) { /* , int turn, boolean hasPicked) {
        if(turn == 2 && !hasPicked) {
            draftPool.diceList().forEach(Dice::roll);
            return true;
        } else
            return false;
            */
        draftPool.diceList().forEach(Dice::roll);
    }

    // Effect of tool card N.8
    // this card can be used only after the first turn's picking,
    // NOTE: the Player MUST skip the second turn of the round
    // Effect of tool card N.9
    // place dice from draft pool to board anywhere
    public boolean use(DraftPool draftPool, int indexDiceDraftPool, Board board, int row, int col, Board.Restriction restriction) {
        if( (verify(TC8.ID)  &&  restriction == Board.Restriction.ALL) ||
            (verify(TC9.ID) &&  restriction == Board.Restriction.WITHOUT_ADJACENT))
            return false;

        if(board.validMove(draftPool.diceList().get(indexDiceDraftPool), row, col, restriction)) {
            board.insertDice(
                        draftPool.chooseDice(indexDiceDraftPool),
                        row, col,
                    restriction);   // note: insertDice does not need check valid move
            return true;
        } else
            return false;
    }

    // Effect of tool card N.10
    // rotate a dice in draft pool, face up the bottom side
    public void use(DraftPool draftPool, int index) {
        Dice dice = draftPool.diceList().get(index);
        dice.setNumber(7 - dice.getDiceNumber());
        draftPool.setDice(index, dice);
    }

    // Effect of tool card N.11
    // discards a dice in draft pool for
    // pick a new dice from diceBag and
    // choose it's value to
    // place in board   // NOTE: this is caller job and must be done
    public Dice use(DraftPool draftPool, int indexDiceDraftPool, DiceBag diceBag) {
        diceBag.addDice(
                draftPool.chooseDice(indexDiceDraftPool));
        return diceBag.extractDice();
    }


    public boolean use(RoundTrack roundTrack, Board board, int rowStart1, int columnStart1, int rowDestination1, int columnDestination1, int rowStart2, int columnStart2, int rowDestination2, int columnDestination2) {
        boolean bExistSameColor = false;
        Dice.ColorDice colorDice = board.getDice(rowStart1, columnStart1).getColorDice();
        if (colorDice.equals(board.getDice(rowStart2, columnDestination2).getColorDice())) {

            for (int i = 1; i <= roundTrack.getActualRound() && !bExistSameColor; i++) {
                for (Dice die : roundTrack.getRoundDice(i)) {
                    if (colorDice.equals(die.getColorDice())) {
                        bExistSameColor = true;
                        break;
                    }
                }
            }
        }

        if (bExistSameColor) {
            Dice dice1 = board.getDice(rowStart1, columnStart1);
            Dice dice2 = board.getDice(rowStart2, columnStart2);

            if (board.canBeRemoved(rowStart1, columnStart1) && board.canBeRemoved(rowStart2, columnStart2) &&
                    board.validMove(dice1, rowDestination1, columnDestination1) && board.validMove(dice2, rowDestination2, columnDestination2)) {

                board.insertDice(board.removeDice(rowStart1, columnStart1), rowDestination1, columnDestination1);
                board.insertDice(board.removeDice(rowStart2, columnStart2), rowDestination2, columnDestination2);
                return true;
            }
        }

        return false;
    }
}
