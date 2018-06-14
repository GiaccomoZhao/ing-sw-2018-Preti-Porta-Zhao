package porprezhas.model.cards;
import porprezhas.model.dices.Board;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.DiceBag;
import porprezhas.model.dices.DraftPool;
import porprezhas.model.dices.RoundTrack;


import java.io.Serializable;

import static porprezhas.model.cards.Card.Effect.*;
import static porprezhas.model.dices.Board.COLUMN;
import static porprezhas.model.dices.Board.ROW;
import static porprezhas.model.dices.Dice.ColorDice.*;



public class ToolCard extends Card implements Serializable {
    private Dice.ColorDice[] cardColors={PURPLE,BLUE,RED,YELLOW,GREEN,PURPLE,BLUE,RED,YELLOW,GREEN,PURPLE,BLUE};

    private ToolCardStrategy strategy;

    private int tokensQuantity;

    private Dice.ColorDice cardColor;


    public ToolCard(Effect effect) {
        super(effect);  //if(effect.ID<16) throw  new InvalidIdException();
        setStrategy();

        this.tokensQuantity = 0;
        this.cardColor = cardColors[this.effect.ID -1];
    }

    public ToolCardStrategy getStrategy() {
        return strategy;
    }

    private void setStrategy() {
        strategy = ToolCardStrategy.list[effect.ID -1];
    }


    public void addTokens(){

        tokensQuantity = tokensQuantity + 1;
    }


/*
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

        Dice chosenDice = draftPool.diceList().get(indexChosenDice);
        int diceNumber = chosenDice.getDiceNumber();
        Dice.ColorDice diceColor = chosenDice.getColorDice();

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
    public boolean use(Board board, int fromRow, int fromColumn, int toRow, int toColumn, Board.Restriction restriction) {
        if( (verify(TC2.ID)  &&  restriction == Board.Restriction.COLOR) ||
            (verify(TC3.ID)  &&  restriction == Board.Restriction.NUMBER) ||
            (verify(TC4.ID) &&  restriction == Board.Restriction.DICE)) {

            if(board.canBeRemoved(fromRow, fromColumn)) {
                // remove and save the dice
                Dice removedDice = board.removeDice(fromRow, fromColumn);

                try {
                    board.insertDice(removedDice, toRow, toColumn, restriction);
                } catch (Exception e) {
                    // if the insertion is not a valid move then we undo the remove
                    try {
                        board.insertDice(removedDice, fromRow, fromColumn, Board.Restriction.NONE);
                    } catch (Exception e1) {
                        System.err.println("Can not use tool card " + this + "");
                    } finally {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    // Effect of tool card N.4: Lathekin
    // Move 2 dices
    public boolean use(Board board, int fromRow1, int fromColumn1, int toRow1, int toColumn1, int fromRow2, int fromColumn2, int toRow2, int toColumn2) {
        if(!verify(TC4.ID))
            return false;

        // we call 2 times use(tool card N.2-3) with a different restriction
        Dice storeDice1 = board.getDice(fromRow1, fromColumn1);   // save the dice for undo
        if( !use(board, fromRow1, fromColumn1, toRow1, toColumn1, Board.Restriction.DICE) ) {
            return false;
        }
        if( !use(board, fromRow2, fromColumn2, toRow2, toColumn2, Board.Restriction.DICE) ) {
            // Undo the insert done by first dice, in case of Failure of the second use
            board.removeDice(fromRow1, fromColumn1);
            try {
                board.insertDice(
                        storeDice1,
                        fromRow1, fromColumn1,
                        Board.Restriction.NONE);    // no restriction because we are doing a UNDO
            } catch (Exception e) {
                System.err.println("Insert with Restriction.NONE shouldn't give exception!!!");
                e.printStackTrace();
            } finally {
                return false;
            }
        }
        return true;
    }

    // Effect of tool card N.5
    public boolean use(DraftPool draftPool, long idDiceDraftPool, RoundTrack roundTrack, int indexRound,  Dice diceRoundTrack) {
        if(!verify(TC5.ID))
            return false;

        //remove the dice from the roundTrack
        roundTrack.removeDice(indexRound, diceRoundTrack);
        //add the dice removed from the roundTrack to the draftPool and replace the old dice in the roundTrack
        // with the dice in the chosen position
        roundTrack.addDice(indexRound,
                draftPool.diceSubstitute(diceRoundTrack, idDiceDraftPool));
        return true;
    }

    // Effect of tool card N.6
    // rolls a chosen dice from the draftPool and,
    // if it cannot be placed in the board, re-adds it to the draftPool
    // @Return mustBePlaced true means the Caller MUST insert this dice in board
    public boolean use (DraftPool draftPool, long idDiceDraftPool, Board board) {
        if(!verify(TC6.ID))
            return false;

        Dice rolledDice;
        rolledDice = draftPool.chooseDice(idDiceDraftPool);
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
    public boolean use(DraftPool draftPool) { /* , int turn, boolean hasPicked) {
        if(turn == 2 && !hasPicked) {
            draftPool.diceList().forEach(Dice::roll);
            return true;
        } else
            return false;
            */
/*        if(!verify(TC7.ID))
            return false;

        draftPool.diceList().forEach(Dice::roll);
        return true;
    }

    // Effect of tool card N.8
    // this card can be used only after the first turn's picking,
    // NOTE: the Player MUST skip the second turn of the round
    // Effect of tool card N.9
    // place dice from draft pool to board anywhere
    public boolean use(DraftPool draftPool, long idDiceDraftPool, Board board, int row, int col, Board.Restriction restriction) {
        if( (verify(TC8.ID)  &&  restriction == Board.Restriction.ALL) ||
            (verify(TC9.ID) &&  restriction == Board.Restriction.WITHOUT_ADJACENT))
            return false;

        try {
            board.validMove(draftPool.diceList().get(idDiceDraftPool), row, col, restriction);
        } catch (Exception e) {
            return false;
        }

        // we not need catch this, because we already checked with validMove()
        board.insertDice(
                draftPool.chooseDice(idDiceDraftPool),
                row, col,
                restriction);   // note: insertDice does not need check valid move
        return true;
    }

    // Effect of tool card N.10
    // rotate a dice in draft pool, face up the bottom side
    public boolean use(DraftPool draftPool, int index) {
        if(!verify(TC10.ID))
            return false;

        Dice dice = draftPool.diceList().get(index);
        dice.setNumber(7 - dice.getDiceNumber());
        draftPool.setDice(index, dice);
        return true;
    }

    // Effect of tool card N.11
    // discards a dice in draft pool for
    // pick a new dice from diceBag and
    // choose it's value to
    // place in board   // NOTE: this is caller job and must be done
    public Dice use(DraftPool draftPool, long idDiceDraftPool, DiceBag diceBag) {
        if(!verify(TC11.ID))
            return null;

        diceBag.addDice(
                draftPool.chooseDice(idDiceDraftPool));
        return diceBag.extractDice();
    }


    // Effect of tool card N.12
    // Move 2 dice in the board, respecting ALL constraints
    // These dices must have the same color and this color's dice exists in the round track
    public boolean use(RoundTrack roundTrack, Board board, int fromRow1, int fromColumn1, int toRow1, int toColumn1, int fromRow2, int fromColumn2, int toRow2, int toColumn2) {
        if(!verify(TC12.ID))
            return false;

        boolean bExistSameColor = false;    // has RoundTrack this color
        Dice.ColorDice colorDice = board.getDice(fromRow1, fromColumn1).getColorDice();
        if (colorDice.equals(board.getDice(fromRow2, fromColumn2).getColorDice())) {  // have dices to move the same color

            // search in round track
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
            Dice dice1 = board.getDice(fromRow1, fromColumn1);
            Dice dice2 = board.getDice(fromRow2, fromColumn2);

            // inside if we have read only methods
            if (    board.canBeRemoved(fromRow1, fromColumn1) &&
                    board.canBeRemoved(fromRow2, fromColumn2) &&
                    board.validMove(dice1, toRow1, toColumn1, Board.Restriction.ALL) &&
                    board.validMove(dice2, toRow2, toColumn2, Board.Restriction.ALL)) {

                board.insertDice(board.removeDice(fromRow1, fromColumn1), toRow1, toColumn1);
                board.insertDice(board.removeDice(fromRow2, fromColumn2), toRow2, toColumn2);
                return true;
            }
        }

        return false;
    }*/
}





