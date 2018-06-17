package porprezhas.model.cards;

import porprezhas.Useful;
import porprezhas.exceptions.GameAbnormalException;
import porprezhas.exceptions.diceMove.DiceNotFoundException;
import porprezhas.exceptions.diceMove.IndexOutOfBoardBoundsException;
import porprezhas.exceptions.toolCard.*;
import porprezhas.model.dices.*;

import java.io.Serializable;
import java.util.List;

import static porprezhas.model.cards.ToolCardParam.IncDec.DECREMENT;
import static porprezhas.model.cards.ToolCardParam.IncDec.INCREMENT;
import static porprezhas.model.dices.Board.COLUMN;
import static porprezhas.model.dices.Board.ROW;
import static porprezhas.model.dices.CellPosition.*;
import static porprezhas.model.dices.Dice.MAX_DICE_NUMBER;
import static porprezhas.model.dices.Dice.MIN_DICE_NUMBER;

public interface ToolCardStrategy {
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

    boolean use(ToolCardParam param);
}



// Effect of tool card N.1
// @Param bIncDec   bIncDec == true → increment,
//                  bIncDec == false → decrement the number of dice
// @Return true     means the tool card effect has verified, uses token
//         false    means this operation is invalid, refund or not use tokens
class ToolCard1 implements ToolCardStrategy, Serializable {
    private boolean savedReturn = false;        // NOTE: this may be not need to be saved; savedReturn is used to return 2 times: a boolean and an other type

    private DraftPool draftPool;
    private Integer indexChosenDice;
    private Boolean bIncDec;

    private final int parameterSize = 2;

    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        // check NULL
        if(null == param  ||  !param.safetyCheck(parameterSize))
            return false;

        // initialize Parameters
        int iParam = 0;
        this.draftPool      = param.getDraftPool();
        this.indexChosenDice= param.getParams().get(iParam++);
        this.bIncDec = ToolCardParam.IncDec.fromIntegerToBoolean(
                              param.getParams().get(iParam++) );

        // Use Tool Card
        savedReturn = use(draftPool, indexChosenDice, bIncDec);
        return savedReturn;
    }

    public boolean getReturn() {
        return savedReturn;
    }

    private boolean use(DraftPool draftPool, int indexChosenDice, boolean bIncDec) {
        // safety Check
        if(null == draftPool  ||  !draftPool.safetyCheck(indexChosenDice))
            return false;

        List<Dice> diceList = draftPool.diceList();

        // Get dice Information from selected Dice
        Dice chosenDice = diceList.get(indexChosenDice);
        Dice.ColorDice diceColor = chosenDice.getColorDice();
        int diceNumber = chosenDice.getDiceNumber();
        long id = chosenDice.getId();

        // Increment the dice number
        if(diceNumber != MAX_DICE_NUMBER  &&  INCREMENT.toBoolean().equals( bIncDec )) {
            diceNumber++;
        }

        // Decrement the dice number
        else if(diceNumber != MIN_DICE_NUMBER  &&  DECREMENT.toBoolean().equals( bIncDec )) {
            diceNumber--;
        }

        // Can not Increment or Decrement the Number
        else
            return false;

        // Replace the dice with the modified number's Dice
        Dice newDice = new Dice( diceColor, diceNumber, id);
        draftPool.setDice( indexChosenDice, newDice );

        // Operation done successfully
        return true;
    }
}



class ToolCard2_4 implements ToolCardStrategy, Serializable {
    private boolean savedReturn;

    private Board board;
    private int fromRow;
    private int fromColumn;
    private int toRow;
    private int toColumn;
    private Board.Restriction restriction;

    private final int parameterSize = 5;

    public ToolCard2_4() {
        this.savedReturn = false;
    }

    @Override
    public boolean use(ToolCardParam param)
            throws IncorrectParamQuantityException, MoveToSelfException, NotRemovableDiceException {

        // reset the return
        resetReturn();      // TODO: reset this at NEW_TURN

        // safety Check parameter quantity
        if(null == param  ||  !param.safetyCheck(parameterSize)) {
            throw new GameAbnormalException("integer parameter should not be incorrect!\n"
                    + "A previous Check must not be verified");
        }

        // initialize Parameters
        int iParam = 0;
        this.board      = param.getBoard();
        this.fromRow    = param.getParams().get(iParam++);
        this.fromColumn = param.getParams().get(iParam++);
        this.toRow      = param.getParams().get(iParam++);
        this.toColumn   = param.getParams().get(iParam++);
        this.restriction= Board.Restriction.values()[ param.getParams().get(iParam) ];

        // CHECK
        // check NULL Pointer
        if(null == board) {
            throw new GameAbnormalException("Board is " + board + "!");
        }
        // check Out of Bound
        if(     Useful.isValueOutOfBounds(fromRow,      MIN_ROW,    MAX_ROW)    ||
                Useful.isValueOutOfBounds(fromColumn,   MIN_COLUMN, MAX_COLUMN) ) {
            throw new IndexOutOfBoardBoundsException(fromRow, fromColumn);

        }
        if(     Useful.isValueOutOfBounds(toRow,        MIN_ROW,    MAX_ROW)    ||
                Useful.isValueOutOfBounds(toColumn,     MIN_COLUMN, MAX_COLUMN)     ) {
            throw new IndexOutOfBoardBoundsException(toRow, toColumn);
        }
        // NULL Dice
        if(null == board.getBoard()[fromRow][fromColumn]) {
            throw new DiceNotFoundInBoardException(fromRow, fromColumn);
        }
        // moving to the same position
        if(fromRow == toRow && fromColumn == toColumn) {
            throw new MoveToSelfException();
        }


        // check adjacent constraint after removing this
//        if(!board.canBeRemoved(fromRow, fromColumn)) {
//            throw new NotRemovableDiceException(fromRow, fromColumn, board.toString(null, fromRow, fromColumn));
//        }

        // DO the operation
        savedReturn = use(board, fromRow, fromColumn, toRow, toColumn, restriction);
        return savedReturn;
    }


    public void resetReturn() {
        savedReturn = false;
    }

    public boolean getReturn() {
        return savedReturn;
    }


    // Effect of tool card N.2-3, used by card n.4 too
    private boolean use(Board board, int fromRow, int fromColumn, int toRow, int toColumn, Board.Restriction restriction)
            throws ToolCardParameterException {
        // remove and save the dice
        Dice removedDice = board.removeDice(fromRow, fromColumn);

        try {
            board.insertDice(removedDice, toRow, toColumn, restriction);
        } catch (Exception e) {
            // if the insertion is not a valid move then we undo the remove
            try {
                board.insertDice(removedDice, fromRow, fromColumn, Board.Restriction.NONE);
            } catch (Exception e1) {
                System.err.println("Abnormal error: Can NOT undo the effect of tool card " + this + "");
                e1.printStackTrace();
            }

            throw e; // new ToolCardParameterException(e.getMessage());
        }
        return true;
    }
}

class ToolCard2 extends ToolCard2_4 {
    private final int parameterSize = 4;

    public ToolCard2() {
        super();
    }


    @Override
    public boolean use(ToolCardParam param)
            throws IncorrectParamQuantityException, MoveToSelfException, NotRemovableDiceException {

        // safety Check
        // check NULL
        if(null == param) {
            throw new IncorrectParamQuantityException();
        }
        else if(!param.safetyCheck(parameterSize)) {
                throw new IncorrectParamQuantityException(parameterSize, param.getParams());
        }

        // add parameter
        ToolCardParam newToolCardParam = new ToolCardParam(param, Board.Restriction.WITHOUT_COLOR.ordinal());

        // use
        return super.use(newToolCardParam);
    }
}

class ToolCard3 extends ToolCard2_4 {
    private final int parameterSize = 4;

    public ToolCard3() {
        super();
    }

    @Override
    public boolean use(ToolCardParam param)
            throws IncorrectParamQuantityException, MoveToSelfException, NotRemovableDiceException {

        // safety Check
        // check NULL
        if(null == param) {
            throw new IncorrectParamQuantityException();
        }
        else if(!param.safetyCheck(parameterSize)) {
            throw new IncorrectParamQuantityException(parameterSize, param.getParams());
        }

        // add parameter
        ToolCardParam newToolCardParam = new ToolCardParam(param, Board.Restriction.WITHOUT_NUMBER.ordinal());
        return super.use(newToolCardParam);
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

    private final int parameterSize = 8;

    public ToolCard4() {
        super();
    }

    // Effect of tool card N.4: Lathekin
    // Move 2 dices
    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        if(null == param) {
            throw new IncorrectParamQuantityException();
        }
        else if(!param.safetyCheck(parameterSize)) {
            throw new IncorrectParamQuantityException(parameterSize, param.getParams());
        }

        // Initialize from Parameters
        int iParam = 0;
        this.board      = param.getBoard();
        this.fromRow1   = param.getParams().get(iParam++);
        this.fromColumn1= param.getParams().get(iParam++);
        this.toRow1     = param.getParams().get(iParam++);
        this.toColumn1  = param.getParams().get(iParam++);
        this.fromRow2   = param.getParams().get(iParam++);
        this.fromColumn2= param.getParams().get(iParam++);
        this.toRow2     = param.getParams().get(iParam++);
        this.toColumn2  = param.getParams().get(iParam++);

        // Create 2 new ToolCardParam to recall super -parent- class use() method
        ToolCardParam param1 = new ToolCardParam(param, 0, 4);
        ToolCardParam param2 = new ToolCardParam(param, 4, 8);


        // Save the dice of first action for Undo
        Dice storeDice1 = board.getDice(fromRow1, fromColumn1);

        // we call 2 times use(tool card N.2-3) without ALL restriction

        // add parameter
        param1 = new ToolCardParam(param1, Board.Restriction.ALL.ordinal());

        if( !super.use(param1) ) {
            return false;
        }

        // add parameter
        param2 = new ToolCardParam(param2, Board.Restriction.ALL.ordinal());

        try {
            // If it Fails at second action: UNDO the insert done by first action
            if( !super.use(param2) ) {
                board.removeDice(fromRow1, fromColumn1);
                try {
                    board.insertDice(
                            storeDice1,
                            fromRow1, fromColumn1,
                            Board.Restriction.NONE);    // no restriction because we are doing a UNDO
                    return false;

                } catch (Exception e) {
                    System.err.println("Insert with Restriction.NONE shouldn't give exception!!!");
                    e.printStackTrace();
                }
            }
        } catch (ToolCardParameterException e) {
            throw new ToolCardParameterException( "Error in the second placement:\n"
                    + e.getMessage());
        }
        return true;
    }
}


class ToolCard5 implements ToolCardStrategy, Serializable {
    private boolean savedReturn = false;

    private DraftPool draftPool;
    private long idDiceDraftPool;
    private RoundTrack roundTrack;
    private int indexRound;
    private int indexDiceRoundTrack;    // the dice to be removed from round track

    private final int parameterSize = 3;


    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        if(null == param  ||  !param.safetyCheck(parameterSize))
            return false;

        int iParam = 0;
        this.draftPool      = param.getDraftPool();
        this.idDiceDraftPool= param.getParams().get(iParam++).longValue();
        this.roundTrack     = param.getRoundTrack();
        this.indexRound     = param.getParams().get(iParam++);
        this.indexDiceRoundTrack = param.getParams().get(iParam++);

        savedReturn = use(draftPool, idDiceDraftPool, roundTrack, indexRound, indexDiceRoundTrack);
        return savedReturn;
    }


    public boolean getReturn() {
        return savedReturn;
    }


    // Effect of tool card N.5
    public boolean use(DraftPool draftPool, long idDiceDraftPool, RoundTrack roundTrack, int indexRound,  int indexDiceRoundTrack) {

        //remove the dice from the roundTrack
        Dice diceRoundTrack = roundTrack.removeDice(indexRound, indexDiceRoundTrack);
        //add the dice removed from the roundTrack to the draftPool and replace the old dice in the roundTrack
        // with the dice in the chosen position
        roundTrack.addDice(indexRound,
                draftPool.diceSubstitute(diceRoundTrack, idDiceDraftPool));
        return true;
    }
}


class ToolCard6 implements ToolCardStrategy, Serializable {
    private boolean savedReturn = false;

    private DraftPool draftPool;
    private long idDiceDraftPool;
    private Board board;

    private final int parameterSize = 1;

    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        if(null == param  ||  !param.safetyCheck(parameterSize))
            return false;

        this.draftPool      = param.getDraftPool();
        this.idDiceDraftPool= param.getParams().get(0).longValue();
        this.board          = param.getBoard();

        savedReturn = use(draftPool, idDiceDraftPool, board);
        return savedReturn;
    }


    public boolean getReturn() {
        return savedReturn;
    }


    // Effect of tool card N.6
    // rolls a chosen dice from the draftPool and,
    // if it cannot be placed in the board, re-adds it to the draftPool
    // @Return mustBePlaced true means the Caller MUST insert this dice in board
    private boolean use (DraftPool draftPool, long idDiceDraftPool, Board board) {

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
}


class ToolCard7 implements ToolCardStrategy, Serializable {
    private boolean savedReturn = false;

    private DraftPool draftPool;

    private final int parameterSize = 0;

    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        if(null == param  ||  !param.safetyCheck(parameterSize))
            return false;

        this.draftPool = param.getDraftPool();

        savedReturn = use(draftPool);
        return savedReturn;
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


class ToolCard8_9 implements ToolCardStrategy, Serializable {
    private boolean savedReturn = false;

    private DraftPool draftPool;
    private long idDiceDraftPool;
    private Board board;
    private int row;
    private int col;
    private Board.Restriction restriction;

    private final int parameterSize = 4;

    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        if(null == param  ||  !param.safetyCheck(parameterSize))
            return false;

        int iParam = 0;
        this.draftPool      = param.getDraftPool();
        this.idDiceDraftPool= param.getParams().get(iParam++).longValue();
        this.board          = param.getBoard();
        this.row            = param.getParams().get(iParam++);
        this.col            = param.getParams().get(iParam++);
        this.restriction    = Board.Restriction.values()[ param.getParams().get(iParam++) ];

        savedReturn = use(draftPool, idDiceDraftPool, board, row, col, restriction);
        return savedReturn;
    }

    protected boolean use(Board.Restriction restriction) {
        return use(draftPool, idDiceDraftPool, board, row, col, restriction);
    }


    public boolean getReturn() {
        return savedReturn;
    }



    // Effect of tool card N.8
    // this card can be used only after the first turn's picking,
    // NOTE: the Player MUST skip the second turn of the round
    // Effect of tool card N.9
    // place dice from draft pool to board anywhere
    private boolean use(DraftPool draftPool, long idDiceDraftPool, Board board, int row, int col, Board.Restriction restriction) {

        try {
            if (board.validMove(draftPool.getDiceByID(idDiceDraftPool), row, col, restriction)) {

                board.insertDice(
                        draftPool.chooseDice(idDiceDraftPool),
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
    private final int parameterSize = 2;

    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        if(null == param  ||  !param.safetyCheck(parameterSize))
            return false;

        param.getParams().add(Board.Restriction.ALL.ordinal());
        return super.use(param);
    }
}

class ToolCard9 extends ToolCard8_9 {
    private final int parameterSize = 3;

    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        if(null == param  ||  !param.safetyCheck(parameterSize)){
            return false;}


        param.getParams().add(Board.Restriction.WITHOUT_ADJACENT.ordinal());
        return super.use(param);
    }
}


class ToolCard10 implements ToolCardStrategy, Serializable {
    private boolean savedReturn = false;

    private DraftPool draftPool;
    private long idDiceDraftPool;

    private final int parameterSize = 1;

    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        if(null == param  ||  !param.safetyCheck(parameterSize))
            return false;

        this.draftPool      = param.getDraftPool();
        this.idDiceDraftPool= param.getParams().get(0);

        savedReturn = use(draftPool, idDiceDraftPool);
        return savedReturn;
    }


    public boolean getReturn() {
        return savedReturn;
    }


    // Effect of tool card N.10
    // rotate a dice in draft pool, face up the bottom side
    private boolean use(DraftPool draftPool, long id) {
        int index = draftPool.getDiceIndexByID(id);
        Dice dice = draftPool.diceList().get(index);

        // rotate the selected dice
        dice.setNumber(7 - dice.getDiceNumber());
        draftPool.setDice(index, dice);

        return true;
    }
}

class ToolCard11 implements ToolCardStrategy, Serializable {
    private Dice savedReturn = null;

    private DraftPool draftPool;
    private long idDiceDraftPool;
    private DiceBag diceBag;

    private final int parameterSize = 1;

    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        if(null == param  ||  !param.safetyCheck(parameterSize))
            return false;

        this.draftPool      = param.getDraftPool();
        this.idDiceDraftPool= param.getParams().get(0).longValue();
        this.diceBag        = param.getDiceBag();

        savedReturn = use(draftPool, idDiceDraftPool, diceBag);
        if(null != savedReturn)
            return true;
        else
            return false;
    }


    public Dice getReturn() {
        return savedReturn;
    }

    // Effect of tool card N.11
    // discards a dice in draft pool for
    // pick a new dice from diceBag and
    // choose it's value to
    // place in board   // NOTE: this is caller job and must be done
    private Dice use(DraftPool draftPool, long idDiceDraftPool, DiceBag diceBag) {
        diceBag.addDice(
                draftPool.chooseDice(idDiceDraftPool));
        return diceBag.extractDice();
    }
}


class ToolCard12 implements ToolCardStrategy, Serializable {
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

    private final int parameterSize = 8;

    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        if(null == param  ||  !param.safetyCheck(parameterSize))
            return false;

        int iParam = 0;
        this.roundTrack = param.getRoundTrack();
        this.board      = param.getBoard();
        this.fromRow1   = param.getParams().get(iParam++);
        this.fromColumn1= param.getParams().get(iParam++);
        this.toRow1     = param.getParams().get(iParam++);
        this.toColumn1  = param.getParams().get(iParam++);
        this.fromRow2   = param.getParams().get(iParam++);
        this.fromColumn2= param.getParams().get(iParam++);
        this.toRow2     = param.getParams().get(iParam++);
        this.toColumn2  = param.getParams().get(iParam++);

        savedReturn = use(roundTrack, board, fromRow1, fromColumn1, toRow1, toColumn1, fromRow2, fromColumn2, toRow2, toColumn2);
        return savedReturn;
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

