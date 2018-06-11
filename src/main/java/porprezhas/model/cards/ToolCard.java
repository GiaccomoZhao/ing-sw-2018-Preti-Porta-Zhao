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

    ToolCardStrategy strategy;

    private int tokensQuantity;

    private Dice.ColorDice cardColor;

    Dice.ColorDice[] cardColors={PURPLE,BLUE,RED,YELLOW,GREEN,PURPLE,BLUE,RED,YELLOW,GREEN,PURPLE,BLUE};

    public ToolCard(Effect effect) {
        super(effect);  //if(effect.ID<16) throw  new InvalidIdException();
        setStrategy();

        this.tokensQuantity = 0;
        this.cardColor = cardColors[this.effect.ID -1];
    }

    private void setStrategy() {
        strategy = ToolCardStrategy.list[effect.ID -1];
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
    public boolean use(DraftPool draftPool, int indexDiceDraftPool, RoundTrack roundTrack, int indexRound,  Dice diceRoundTrack) {
        if(!verify(TC5.ID))
            return false;

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
        if(!verify(TC6.ID))
            return false;

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
    public boolean use(DraftPool draftPool) { /* , int turn, boolean hasPicked) {
        if(turn == 2 && !hasPicked) {
            draftPool.diceList().forEach(Dice::roll);
            return true;
        } else
            return false;
            */
        if(!verify(TC7.ID))
            return false;

        draftPool.diceList().forEach(Dice::roll);
        return true;
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

        try {
            board.validMove(draftPool.diceList().get(indexDiceDraftPool), row, col, restriction);
        } catch (Exception e) {
            return false;
        }

        // we not need catch this, because we already checked with validMove()
        board.insertDice(
                draftPool.chooseDice(indexDiceDraftPool),
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
    public Dice use(DraftPool draftPool, int indexDiceDraftPool, DiceBag diceBag) {
        if(!verify(TC11.ID))
            return null;

        diceBag.addDice(
                draftPool.chooseDice(indexDiceDraftPool));
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
    }
}


















// *********************************************
// ********  << TOOL CARD STRATEGY >>  *********
// *********************************************


interface ToolCardStrategy {
    ToolCardStrategy[] list = {
            new ToolCard1(),
            new ToolCard2(),
            new ToolCard3(),
            new ToolCard4(),
            new ToolCard5(),
            new ToolCard6(),
            new ToolCard7(),
            new ToolCard8(),
            new ToolCard9(),
            new ToolCard10(),
            new ToolCard11(),
            new ToolCard12()
    };

    boolean use();
}


class ToolCard1 implements ToolCardStrategy {
    private boolean savedReturn = false;        // NOTE: this may be not need to be saved; savedReturn is used to return 2 times: a boolean and an other type
    private DraftPool draftPool;
    private int indexChosenDice;
    private boolean bIncDec;


    @Override
    public boolean use() {
        savedReturn = use(draftPool, indexChosenDice, bIncDec);
        return savedReturn;
    }

    public void setup(DraftPool draftPool, int indexChosenDice, boolean bIncDec) {
        this.draftPool = draftPool;
        this.indexChosenDice = indexChosenDice;
        this.bIncDec = bIncDec;
    }

    public boolean getReturn() {
        return savedReturn;
    }

    // Effect of tool card N.1
    // @Param bIncDec   bIncDec == true → increment,
    //                  bIncDec == false → decrement the number of dice
    // @Return true     means the tool card effect has verified, uses token
    //         false    means this operation is invalid, refund or not use tokens
    private boolean use(DraftPool draftPool, int indexChosenDice, boolean bIncDec) {

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
}



class ToolCard2_4 implements ToolCardStrategy {
    private boolean savedReturn = false;
    private Board board;
    private int fromRow;
    private int fromColumn;
    private int toRow;
    private int toColumn;
    private Board.Restriction restriction;


    @Override
    public boolean use() {
        savedReturn = use(board, fromRow, fromColumn, toRow, toColumn, restriction);
        return savedReturn;
    }

    protected boolean use(Board.Restriction restriction) {
        savedReturn = use(board, fromRow, fromColumn, toRow, toColumn, restriction);
        return savedReturn;
    }

    public void setup(Board board, int fromRow, int fromColumn, int toRow, int toColumn, Board.Restriction restriction) {
        this.board = board;
        this.fromRow = fromRow;
        this.fromColumn = fromColumn;
        this.toRow = toRow;
        this.toColumn = toColumn;
        this.restriction = restriction;
    }

    public boolean getReturn() {
        return savedReturn;
    }


    // Effect of tool card N.2-3, used by card n.4 too
    private boolean use(Board board, int fromRow, int fromColumn, int toRow, int toColumn, Board.Restriction restriction) {

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
        return false;
    }
}

class ToolCard2 extends ToolCard2_4 {
    @Override
    public boolean use() {
        return super.use(Board.Restriction.COLOR);
    }
}

class ToolCard3 extends ToolCard2_4 {
    @Override
    public boolean use() {
        return super.use(Board.Restriction.NUMBER);
    }
}


class ToolCard4 extends ToolCard2_4 {
    private Board board;
    private int fromRow1;
    private int fromColumn1;
    private int toRow1;
    private int toColumn1;
    private int fromRow2;
    private int fromColumn2;
    private int toRow2;
    private int toColumn2;


    @Override
    public boolean use() {
        return use(board, fromRow1, fromColumn1, toRow1, toColumn1, fromRow2, fromColumn2, toRow2, toColumn2);
        // super.use() has saved return
    }

    public void setup(Board board, int fromRow1, int fromColumn1, int toRow1, int toColumn1, int fromRow2, int fromColumn2, int toRow2, int toColumn2) {
        this.board = board;
        this.fromRow1 = fromRow1;
        this.fromColumn1 = fromColumn1;
        this.toRow1 = toRow1;
        this.toColumn1 = toColumn1;
        this.fromRow2 = fromRow2;
        this.fromColumn2 = fromColumn2;
        this.toRow2 = toRow2;
        this.toColumn2 = toColumn2;
    }

    // Effect of tool card N.4: Lathekin
    // Move 2 dices
    private boolean use(Board board, int fromRow1, int fromColumn1, int toRow1, int toColumn1, int fromRow2, int fromColumn2, int toRow2, int toColumn2) {

        // we call 2 times use(tool card N.2-3) with a different restriction
        Dice storeDice1 = board.getDice(fromRow1, fromColumn1);   // save the dice for undo
        super.setup(board, fromRow1, fromColumn1, toRow1, toColumn1, Board.Restriction.DICE);
        if( !super.use() ) {
            return false;
        }
        super.setup(board, fromRow2, fromColumn2, toRow2, toColumn2, Board.Restriction.DICE);
        if( !super.use() ) {
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
}


class ToolCard5 implements ToolCardStrategy {
    private boolean savedReturn = false;
    private DraftPool draftPool;
    private int indexDiceDraftPool;
    private RoundTrack roundTrack;
    private int indexRound;
    private Dice diceRoundTrack;

    @Override
    public boolean use() {
        savedReturn = use(draftPool, indexDiceDraftPool, roundTrack, indexRound, diceRoundTrack);
        return savedReturn;
    }

    public void setup(DraftPool draftPool, int indexDiceDraftPool, RoundTrack roundTrack, int indexRound,  Dice diceRoundTrack) {
        this.draftPool = draftPool;
        this.indexDiceDraftPool = indexDiceDraftPool;
        this.roundTrack = roundTrack;
        this.indexRound = indexRound;
        this.diceRoundTrack = diceRoundTrack;
    }

    public boolean getReturn() {
        return savedReturn;
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
}


class ToolCard6 implements ToolCardStrategy {
    private boolean savedReturn = false;
    private DraftPool draftPool;
    private int indexDiceDraftPool;
    private Board board;

    @Override
    public boolean use() {
        savedReturn = use(draftPool, indexDiceDraftPool, board);
        return savedReturn;
    }

    public void setup(DraftPool draftPool, int indexDiceDraftPool, Board board) {
        this.draftPool = draftPool;
        this.indexDiceDraftPool = indexDiceDraftPool;
        this.board = board;
    }

    public boolean getReturn() {
        return savedReturn;
    }


    // Effect of tool card N.6
    // rolls a chosen dice from the draftPool and,
    // if it cannot be placed in the board, re-adds it to the draftPool
    // @Return mustBePlaced true means the Caller MUST insert this dice in board
    private boolean use (DraftPool draftPool, int indexDiceDraftPool, Board board) {

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
}


class ToolCard7 implements ToolCardStrategy {
    private boolean savedReturn = false;
    private DraftPool draftPool;

    @Override
    public boolean use() {
        savedReturn = use(draftPool);
        return savedReturn;
    }

    public void setup(DraftPool draftPool) {
        this.draftPool = draftPool;
    }

    public boolean getReturn() {
        return savedReturn;
    }


    // Effect of tool card N.7
    // this card can be used only on the second turn before drafting
    // NOTE: This constraint is Caller job
    private boolean use(DraftPool draftPool) {
        draftPool.diceList().forEach(Dice::roll);
        return true;
    }
}


class ToolCard8_9 implements ToolCardStrategy {
    private boolean savedReturn = false;
    private DraftPool draftPool;
    private int indexDiceDraftPool;
    private Board board;
    private int row;
    private int col;
    private Board.Restriction restriction;

    @Override
    public boolean use() {
        savedReturn = use(draftPool, indexDiceDraftPool, board, row, col, restriction);
        return savedReturn;
    }

    protected boolean use(Board.Restriction restriction) {
        return use(draftPool, indexDiceDraftPool, board, row, col, restriction);
    }

    public void setup(DraftPool draftPool, int indexDiceDraftPool, Board board, int row, int col, Board.Restriction restriction) {
        this.draftPool = draftPool;
        this.indexDiceDraftPool = indexDiceDraftPool;
        this.board = board;
        this.row = row;
        this.col = col;
        this.restriction = restriction;
    }

    public boolean getReturn() {
        return savedReturn;
    }



    // Effect of tool card N.8
    // this card can be used only after the first turn's picking,
    // NOTE: the Player MUST skip the second turn of the round
    // Effect of tool card N.9
    // place dice from draft pool to board anywhere
    private boolean use(DraftPool draftPool, int indexDiceDraftPool, Board board, int row, int col, Board.Restriction restriction) {

        try {
            if (board.validMove(draftPool.diceList().get(indexDiceDraftPool), row, col, restriction)) {

                board.insertDice(
                        draftPool.chooseDice(indexDiceDraftPool),
                        row, col,
                        restriction);   // note: insertDice does not need check valid move
                return true;

            } else
                return false;   // when validMove() returns false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;   // when validMove() returns invalid move exception
        }
    }

}

class ToolCard8 extends ToolCard8_9 {
    @Override
    public boolean use() {
        return super.use(Board.Restriction.ALL);
    }
}

class ToolCard9 extends ToolCard8_9 {
    @Override
    public boolean use() {
        return super.use(Board.Restriction.WITHOUT_ADJACENT);
    }
}


class ToolCard10 implements ToolCardStrategy {
    private boolean savedReturn = false;
    private DraftPool draftPool;
    private int indexDiceDraftPool;

    @Override
    public boolean use() {
        savedReturn = use(draftPool, indexDiceDraftPool);
        return savedReturn;
    }

    public void setup(DraftPool draftPool, int index) {
        this.draftPool = draftPool;
        this.indexDiceDraftPool = index;
    }

    public boolean getReturn() {
        return savedReturn;
    }


    // Effect of tool card N.10
    // rotate a dice in draft pool, face up the bottom side
    private boolean use(DraftPool draftPool, int index) {
        Dice dice = draftPool.diceList().get(index);
        dice.setNumber(7 - dice.getDiceNumber());
        draftPool.setDice(index, dice);
        return true;
    }
}

class ToolCard11 implements ToolCardStrategy {
    private Dice savedReturn = null;
    private DraftPool draftPool;
    private int indexDiceDraftPool;
    private DiceBag diceBag;

    @Override
    public boolean use() {
        savedReturn = use(draftPool, indexDiceDraftPool, diceBag);
        if(null != savedReturn)
            return true;
        else
            return false;
    }

    public void setup(DraftPool draftPool, int indexDiceDraftPool, DiceBag diceBag) {
        this.draftPool = draftPool;
        this.indexDiceDraftPool = indexDiceDraftPool;
        this.diceBag = diceBag;
    }

    public Dice getReturn() {
        return savedReturn;
    }

    // Effect of tool card N.11
    // discards a dice in draft pool for
    // pick a new dice from diceBag and
    // choose it's value to
    // place in board   // NOTE: this is caller job and must be done
    private Dice use(DraftPool draftPool, int indexDiceDraftPool, DiceBag diceBag) {
        diceBag.addDice(
                draftPool.chooseDice(indexDiceDraftPool));
        return diceBag.extractDice();
    }
}


class ToolCard12 implements ToolCardStrategy {
    private boolean savedReturn = false;
    private RoundTrack roundTrack;
    private Board board;
    private int fromRow1;
    private int fromColumn1;
    private int toRow1;
    private int toColumn1;
    private int fromRow2;
    private int fromColumn2;
    private int toRow2;
    private int toColumn2;

    @Override
    public boolean use() {
        savedReturn = use(roundTrack, board, fromRow1, fromColumn1, toRow1, toColumn1, fromRow2, fromColumn2, toRow2, toColumn2);
        return savedReturn;
    }

    public void setup(RoundTrack roundTrack, Board board, int fromRow1, int fromColumn1, int toRow1, int toColumn1, int fromRow2, int fromColumn2, int toRow2, int toColumn2) {
        this.roundTrack = roundTrack;
        this.board = board;
        this.fromRow1 = fromRow1;
        this.fromColumn1 = fromColumn1;
        this.toRow1 = toRow1;
        this.toColumn1 = toColumn1;
        this.fromRow2 = fromRow2;
        this.fromColumn2 = fromColumn2;
        this.toRow2 = toRow2;
        this.toColumn2 = toColumn2;
    }

    public boolean getReturn() {
        return savedReturn;
    }


    // Effect of tool card N.12
    // Move 2 dice in the board, respecting ALL constraints
    // These dices must have the same color and this color's dice exists in the round track
    private boolean use(RoundTrack roundTrack, Board board, int fromRow1, int fromColumn1, int toRow1, int toColumn1, int fromRow2, int fromColumn2, int toRow2, int toColumn2) {

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

            try {
                // inside if we have read only methods
                if (    board.canBeRemoved(fromRow1, fromColumn1) &&
                        board.canBeRemoved(fromRow2, fromColumn2) &&
                        board.validMove(dice1, toRow1, toColumn1, Board.Restriction.ALL) &&
                        board.validMove(dice2, toRow2, toColumn2, Board.Restriction.ALL)) {

                    board.insertDice(board.removeDice(fromRow1, fromColumn1), toRow1, toColumn1);
                    board.insertDice(board.removeDice(fromRow2, fromColumn2), toRow2, toColumn2);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }
}

