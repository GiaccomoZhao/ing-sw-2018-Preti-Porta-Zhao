package porprezhas.model.cards;

import porprezhas.Useful;
import porprezhas.exceptions.GameAbnormalException;
import porprezhas.exceptions.diceMove.DiceNotFoundInDraftPoolException;
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

    /**
     * apply the effect of the Tool Card 1
     * @param param contains all the parameter used by the card
     * @return the success of the operation
     */

    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        // check NULL
        if(null == param) {
            throw new IncorrectParamQuantityException();
        }
        else if(!param.safetyCheck(parameterSize)) {
            throw new IncorrectParamQuantityException(parameterSize, param.getParams());
        }

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

    /**
     * auxiliary function for the Tool Card 1
     * @param draftPool draftpool of the game
     * @param indexChosenDice index to find the dice
     * @param bIncDec true->increment, false->decrement
     * @return the success of the operation
     */

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



class ToolCard_Move implements ToolCardStrategy, Serializable {
    private boolean savedReturn;

    private Board board;
    private int fromRow;
    private int fromColumn;
    private int toRow;
    private int toColumn;
    private Board.Restriction restriction;

    private final int parameterSize = 5;

    public ToolCard_Move() {
        this.savedReturn = false;
    }

    /**
     * apply the effect of the Tool Cards
     * @param param contains all the parameter used by the card
     * @return the success of the operation
     * @throws IncorrectParamQuantityException
     * @throws MoveToSelfException
     * @throws NotRemovableDiceException
     */

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

    /**
     * auxiliary function used by Tool Card 2,3,4
     * @param board board of the player
     * @param fromRow index of the first row considered
     * @param fromColumn index of the first column considered
     * @param toRow index of the last row considered
     * @param toColumn index of the last column considered
     * @param restriction kind of restriction applied for the dice positioning
     * @return the success of the operation
     * @throws ToolCardParameterException
     */

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

class ToolCard2 extends ToolCard_Move {
    private final int parameterSize = 4;

    public ToolCard2() {
        super();
    }

    /**
     * implementation of the effect of the Tool Card 2
     * @param param contains all the parameter used by the card
     * @return the success of the operation
     * @throws IncorrectParamQuantityException
     * @throws MoveToSelfException
     * @throws NotRemovableDiceException
     */

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

class ToolCard3 extends ToolCard_Move {
    private final int parameterSize = 4;

    public ToolCard3() {
        super();
    }

    /**
     * implementation of the effect of the Tool Card 3
     * @param param contains all the parameter used by the card
     * @return
     * @throws IncorrectParamQuantityException
     * @throws MoveToSelfException
     * @throws NotRemovableDiceException
     */

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


class ToolCard4 extends ToolCard_Move {
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

    /**
     * implementation of the effect of the Tool Card 4
     * @param param contains all the parameter used by the card
     * @return the success of the operation
     */

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


    /**
     * implementation of the Tool Card 5
     * @param param contains all the parameter used by the card
     * @return the success of the operation
     */

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


    /**
     * effect of the Tool Card 5
     * @param draftPool draftpool of the game
     * @param idDiceDraftPool ID of the dice in the draftpool to swap
     * @param roundTrack roundtrack of the game
     * @param indexRound index of the round considered in the roundtrack
     * @param indexDiceRoundTrack index of the dice in the roundtrack to swap
     * @return the success of the operation
     */

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
//    private int indexDiceDraftPool;
    private Board board;

    private final int parameterSize = 1;


    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        if(null == param  ||  !param.safetyCheck(parameterSize))
            return false;

        this.draftPool      = param.getDraftPool();
        this.idDiceDraftPool= param.getParams().get(0).longValue();
//        this.indexDiceDraftPool= param.getParams().get(0);
        this.board          = param.getBoard();

        savedReturn = use(draftPool, idDiceDraftPool, board);
//        savedReturn = use(draftPool, indexDiceDraftPool, board);
        return savedReturn;
    }


    public boolean getReturn() {
        return savedReturn;
    }

    /**
     * implementation of the effect of Tool Card 6
     * @param draftPool draftpool of the game
     * @param idDiceDraftPool Id of the dice in the draftpool to roll
     * @param board board of the player
     * @return
     */

    // Effect of tool card N.6
    // rolls a chosen dice from the draftPool and,
    // if it cannot be placed in the board, re-adds it to the draftPool
    // @Return mustBePlaced true means the Caller MUST insert this dice in board
    private boolean use (DraftPool draftPool, long idDiceDraftPool, Board board) {
//    private boolean use (DraftPool draftPool, int indexDiceDraftPool, Board board) {

        Dice rolledDice;
        rolledDice = draftPool.chooseDice(idDiceDraftPool);
//        rolledDice = draftPool.chooseDice(indexDiceDraftPool);
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

    /**
     * Implementation of Tool Card 7
     * @param param all the parameters used by the method
     * @return the success of the operation
     */

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


    /**
     *implementation of the effect of Tool Card 7
     * @param draftPool draftpool of the game
     * @return the success of the operation
     */

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
//    private int indexDiceDraftPool;
    private Board board;
    private int row;
    private int col;
    private Board.Restriction restriction;

    private final int parameterSize = 4;

    /**
     * implementation of Tool Card 8 & 9
     * @param param list of parameters used
     * @return the success of the operation
     */

    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        if(null == param  ||  !param.safetyCheck(parameterSize))
            return false;

        int iParam = 0;
        this.draftPool      = param.getDraftPool();
        this.idDiceDraftPool= param.getParams().get(iParam++).longValue();
//        this.indexDiceDraftPool= param.getParams().get(iParam++);
        this.board          = param.getBoard();
        this.row            = param.getParams().get(iParam++);
        this.col            = param.getParams().get(iParam++);
        this.restriction    = Board.Restriction.values()[ param.getParams().get(iParam++) ];

        savedReturn = use(draftPool, idDiceDraftPool, board, row, col, restriction);
//        savedReturn = use(draftPool, indexDiceDraftPool, board, row, col, restriction);
        return savedReturn;
    }

    protected boolean use(Board.Restriction restriction) {
        return use(draftPool, idDiceDraftPool, board, row, col, restriction);
//        return use(draftPool, indexDiceDraftPool, board, row, col, restriction);
    }


    public boolean getReturn() {
        return savedReturn;
    }


    /**
     * implementation of the effect of Tool Card 8 & 9
     * @param draftPool draftpool of the game
     * @param idDiceDraftPool ID of the dice of the draftpool considered
     * @param board board of the player
     * @param row index of the row considered
     * @param col index of the column considered
     * @param restriction kind of restrictions applied to dice positioning
     * @return the success of the operation
     */

    // Effect of tool card N.8
    // this card can be used only after the first turn's picking,
    // NOTE: the Player MUST skip the second turn of the round
    // Effect of tool card N.9
    // place dice from draft pool to board anywhere
    private boolean use(DraftPool draftPool, long idDiceDraftPool, Board board, int row, int col, Board.Restriction restriction) {
//    private boolean use(DraftPool draftPool, int indexDiceDraftPool, Board board, int row, int col, Board.Restriction restriction) {

        try {
            if (board.validMove(draftPool.getDiceByID(idDiceDraftPool), row, col, restriction)) {
//            if (board.validMove(draftPool.diceList().get(indexDiceDraftPool), row, col, restriction)) {

                board.insertDice(
                        draftPool.chooseDice(idDiceDraftPool),
//                        draftPool.chooseDice(indexDiceDraftPool),
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

    /**
     * application of Tool Card 8
     * @param param list of parameters used
     * @return the success of the operation
     */

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

    /**
     * application of Tool Card9
     * @param param list of parameters used
     * @return success of the operation
     */

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
//    private int indexDiceDraftPool;

    private final int parameterSize = 1;

    /**
     * implementation of Tool Card 10
     * @param param list of parameters used by Tool Card 10
     * @return the success of the operation
     */

    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        if(null == param  ||  !param.safetyCheck(parameterSize))
            return false;

        this.draftPool      = param.getDraftPool();
        this.idDiceDraftPool= param.getParams().get(0).longValue();
//        this.indexDiceDraftPool= param.getParams().get(0);

        savedReturn = use(draftPool, idDiceDraftPool);
//        savedReturn = use(draftPool, indexDiceDraftPool);
        return savedReturn;
    }


    public boolean getReturn() {
        return savedReturn;
    }


    /**
     * implementation of the effect of Tool Card 10
     * @param draftPool draftpool of the game
     * @param id ID of the dice
     * @return the success of the operation
     */

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



// Effect of tool card N.11
// discards a dice in draft pool for
// pick a new dice from diceBag and
// choose it's value to
// place in board   // NOTE: this is caller job and must be done
class ToolCard11 implements ToolCardStrategy, Serializable {
    private Dice savedReturn = null;

    private DraftPool draftPool;
    private long idDiceDraftPool;
//    private int indexDiceDraftPool;
    private DiceBag diceBag;

    private final int parameterSize = 1;

    /**
     * implementation of Tool Card 11
     * @param param list of the parameters used by Tool Card 11
     * @return the success of the operation
     */

    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        if(null == param  ||  !param.safetyCheck(parameterSize))
            return false;

        this.draftPool      = param.getDraftPool();
        this.idDiceDraftPool= param.getParams().get(0).longValue();
//        this.indexDiceDraftPool= param.getParams().get(0);
        this.diceBag        = param.getDiceBag();

        savedReturn = use(draftPool, idDiceDraftPool, diceBag);
//        savedReturn = use(draftPool, indexDiceDraftPool, diceBag);
        if(null != savedReturn)
            return true;
        else
            return false;
    }


    public Dice getReturn() {
        return savedReturn;
    }

    /**
     * implementation of the effect of Tool Card 11
     * @param draftPool draftpool of the game
     * @param idDiceDraftPool ID of the dice of the draftpool considered
     * @param diceBag dicebag of the game
     * @return the success of the operation
     */
    private Dice use(DraftPool draftPool, long idDiceDraftPool, DiceBag diceBag) {
//    private Dice use(DraftPool draftPool, int indexDiceDraftPool, DiceBag diceBag) {
        diceBag.addDice(
                draftPool.chooseDice(idDiceDraftPool));
//                draftPool.chooseDice(indexDiceDraftPool));
        return diceBag.extractDice();
    }
}



// Effect of tool card N.12
// Move 2 dice in the board, respecting ALL constraints
// These dices must have the same color and this color's dice exists in the round track

class ToolCard12 extends ToolCard4 implements ToolCardStrategy, Serializable {
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

    /**
     * implementation of the effect of Tool Card 12
     * @param param contains all the parameter used by the card
     * @return the success of the operation
     */

    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        if(null == param) {
            throw new IncorrectParamQuantityException();
        }
        else if(!param.safetyCheck(parameterSize)) {
            throw new IncorrectParamQuantityException(parameterSize, param.getParams());
        }

        // get parameters information
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

        // NULL POINTER safety check
        if(null == board) {
            throw new GameAbnormalException("Board is " + board + "!");
        }
        if( null == roundTrack ) {
            throw new GameAbnormalException("Round Track is " + roundTrack + "!");
        }

        // getDice return a new White 0 Dice when it does not exists
        if(0 == board.getDice(fromRow1, fromColumn1).getDiceNumber() )
            throw new DiceNotFoundInBoardException(fromRow1, fromColumn1);
        if(0 == board.getDice(fromRow2, fromColumn2).getDiceNumber())
            throw new DiceNotFoundInBoardException(fromRow2, fromColumn2);


        // COLOR Check

        boolean bHasColorInTrack = false;    // has RoundTrack this color
        Dice.ColorDice searchedColor = board.getDice(fromRow1, fromColumn1).getColorDice();

        // have the 2 dices to move in the board the same color
        if (!searchedColor.equals(board.getDice(fromRow2, fromColumn2).getColorDice())) {
            throw new DifferentColorException(
                    board.getDice(fromRow1, fromColumn1),
                    board.getDice(fromRow2, fromColumn2)
            );

        } else {
            // search that color exists in round track,
            // until current round, because after it we shouldn't have dices in
            for (int i = 1; i <= roundTrack.getActualRound() && !bHasColorInTrack; i++) {
                if (null != roundTrack.getRoundDice(i)) {
                    for (Dice die : roundTrack.getRoundDice(i)) {
                        if (searchedColor.equals(die.getColorDice())) {
                            bHasColorInTrack = true;
                            break;
                        }
                    }
                }
            }
        }

        if (!bHasColorInTrack) {
            throw new CorlorNotFoundInRoundTrackException("A dice with color = " + searchedColor  + " has NOT be FOUND in Round Track\n"
                    + roundTrack);
        }

        // MOVE dices using the same strategy of tool card n.4
        return super.use(param);
    }

    public boolean getReturn() {
        return savedReturn;
    }

}

