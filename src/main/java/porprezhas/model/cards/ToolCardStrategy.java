package porprezhas.model.cards;

import porprezhas.Useful;
import porprezhas.exceptions.GameAbnormalException;
import porprezhas.exceptions.diceMove.DiceNotFoundInDraftPoolException;
import porprezhas.exceptions.diceMove.IndexOutOfBoardBoundsException;
import porprezhas.exceptions.toolCard.*;
import porprezhas.model.dices.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static porprezhas.model.cards.ToolCardParam.IncDec.DECREMENT;
import static porprezhas.model.cards.ToolCardParam.IncDec.INCREMENT;
import static porprezhas.model.dices.Board.COLUMN;
import static porprezhas.model.dices.Board.ROW;
import static porprezhas.model.dices.CellPosition.*;
import static porprezhas.model.dices.Dice.MAX_DICE_NUMBER;
import static porprezhas.model.dices.Dice.MIN_DICE_NUMBER;

public interface ToolCardStrategy {
    public static final int[][] parameterSizes = {{-1}, {2}, {4}, {4}, {8}, {3}, {1,2}, {0}, {2} ,{3}, {1}, {1,3}, {8}};   // skip first for better using index
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
    Object getReturn();
    void reset();
}



class ToolCard1 implements ToolCardStrategy, Serializable {
    private boolean savedReturn = false;        // NOTE: this may be not need to be saved; savedReturn is used to return 2 times: a boolean and an other type

    private DraftPool draftPool;
    private Integer indexChosenDice;
    private Boolean bIncDec;

    private final int parameterSize = parameterSizes[1][0];

    @Override
    public void reset() {
        savedReturn = false;
    }

    @Override
    public Object getReturn() {
        return savedReturn;
    }


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

    /**
     * auxiliary function for the Tool Card 1
     * Effect of tool card N.1:
     *      Increase or Decrease the Number of a Dice in Draft pool
     *
     * @param draftPool draft pool of the game
     * @param indexChosenDice index to find the dice
     * @Param bIncDec   bIncDec == true → increment,
     *                  bIncDec == false → decrement the number of dice
     * @Return true     means the tool card effect has verified, uses token
     *         false    means this operation is invalid, refund or not use tokens
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


    @Override
    public void reset() {
        savedReturn = false;
    }

    @Override
    public Object getReturn() {
        return savedReturn;
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
        reset();      // TODO: reset this at NEW_TURN

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
                throw new GameAbnormalException("Can NOT undo the effect of tool card " + this + "");
            }

            throw e; // new ToolCardParameterException(e.getMessage());
        }
        return true;
    }
}

class ToolCard2 extends ToolCard_Move {
    private final int parameterSize = parameterSizes[2][0];

    public ToolCard2() {
        super();
    }

    /**
     * implementation of the effect of the Tool Card 2
     * move a dice without color constraint
     *
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
    private final int parameterSize = parameterSizes[3][0];

    public ToolCard3() {
        super();
    }

    /**
     * implementation of the effect of the Tool Card 3
     * move a dice without the number constraint
     *
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

    private final int parameterSize = parameterSizes[4][0];

    public ToolCard4() {
        super();
    }

    /**
     * implementation of the effect of the Tool Card 4
     * Effect of tool card N.4: Lathekin
     *      Move 2 dices inside the board
     *
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
        ToolCardParam param1 = new ToolCardParam(param, 0, 4);  // from 0 to 3
        ToolCardParam param2 = new ToolCardParam(param, 4, 8);  // includes from 4 to 7


        // Save the dice of first action for Undo
        Dice storeDice1 = board.getDice(fromRow1, fromColumn1);

        // we call 2 times use(tool card N.2-3) without ALL restriction

        // add parameter
        param1 = new ToolCardParam(param1, Board.Restriction.ALL.ordinal());

        if( !super.use(param1) ) {
            return false;   // this statement is never called, because use throws exceptions or return true only
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
    private int iDiceDraftPool;
    private RoundTrack roundTrack;
    private int indexRound;
    private int indexDiceRoundTrack;    // the dice to be removed from round track

    private final int parameterSize = parameterSizes[5][0];


    @Override
    public void reset() {
        savedReturn = false;
    }

    @Override
    public Object getReturn() {
        return savedReturn;
    }


    /**
     * implementation of the Tool Card 5
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

        int iParam = 0;
        this.draftPool      = param.getDraftPool();
        this.iDiceDraftPool= param.getParams().get(iParam++);
        this.roundTrack     = param.getRoundTrack();
        this.indexRound     = param.getParams().get(iParam++);
        this.indexDiceRoundTrack = param.getParams().get(iParam++);

        savedReturn = use(draftPool, iDiceDraftPool, roundTrack, indexRound, indexDiceRoundTrack);
        return savedReturn;
    }

    /**
     * Effect of the Tool Card 5:
     * Exchange a dice of draft pool with a dice in round track
     *
     * @param draftPool draftpool of the game
     * @param iDiceDraftPool Index of the dice in the draftpool to swap
     * @param roundTrack roundtrack of the game
     * @param indexRound index of the round considered in the roundtrack
     * @param indexDiceRoundTrack index of the dice in the roundtrack to swap
     * @return the success of the operation
     */
    public boolean use(DraftPool draftPool, int iDiceDraftPool, RoundTrack roundTrack, int indexRound,  int indexDiceRoundTrack) {

        //remove the dice from the roundTrack
        Dice diceRoundTrack = roundTrack.removeDice(indexRound, indexDiceRoundTrack);
        //add the dice removed from the roundTrack to the draftPool and replace the old dice in the roundTrack
        // with the dice in the chosen position
        roundTrack.addDice(indexRound,
                draftPool.diceSubstitute(diceRoundTrack, iDiceDraftPool));
        return true;
    }
}


class ToolCard6 implements ToolCardStrategy, Serializable {
    private Object savedReturn;
    Dice rolledDice;        // the saved return of first use

    private DraftPool draftPool;
    private int iDiceDraftPool;
//    private int indexDiceDraftPool;
    private Board board;
    private int toRow;
    private int toColumn;

    private int iProcess = 0;
//    private final int parameterSize = parameterSizes[6]


    @Override
    public void reset() {
        savedReturn = false;
    }

    @Override
    public Object getReturn() {
        return savedReturn;
    }

    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        if(null == param) {
            throw new IncorrectParamQuantityException();
        }
        else if(!param.safetyCheck(parameterSizes[6][iProcess])) {
            throw new IncorrectParamQuantityException(parameterSizes[6][iProcess], param.getParams());
        }

        if(iProcess == 0) {
            this.draftPool = param.getDraftPool();
            this.iDiceDraftPool = param.getParams().get(0);
//        this.indexDiceDraftPool= param.getParams().get(0);
            this.board = param.getBoard();

            savedReturn = use1();
//        savedReturn = use(draftPool, indexDiceDraftPool, board);
            if(null== savedReturn) {
                reset();
                return false;
            }
            else {
                iProcess ++;
                return true;
            }
        }
        else if(iProcess == 1){
            this.board = param.getBoard();
            this.toRow = param.getParams().get(0);
            this.toColumn = param.getParams().get(1);

            savedReturn = use2();
            if(savedReturn.equals(true)) {
                reset();
                return true;
            } else
                return false;
        }

        throw new GameAbnormalException("TC6 may be not reset before being used an other time");
    }

    /**
     * implementation of the effect of Tool Card 6
     * Effect of tool card N.6
     *      rolls a chosen dice from the draftPool and
     *      if it cannot be placed in the board, add it to the draftPool
     *      otherwise the player is constrained to place it in his board
     *
//     * @param draftPool draftpool of the game
//     * @param iDiceDraftPool Index of the dice in the draftpool to roll
//     * @param board board of the player
     * @Return mustBePlaced, true means the Caller MUST insert this dice in board
     *                       false means this dice will be put in draft pool
     */
    private Dice use1() { //(DraftPool draftPool, int iDiceDraftPool, Board board) {
//    private boolean use (DraftPool draftPool, int indexDiceDraftPool, Board board) {

//        rolledDice = draftPool.chooseDice(idDiceDraftPool);
        rolledDice = draftPool.chooseDice(iDiceDraftPool);
//        rolledDice = draftPool.chooseDice(indexDiceDraftPool);
        rolledDice.roll();

        boolean canBePlaced = false;
        for (int row = 0; row < ROW  &&  !canBePlaced; row++) {
            for (int col = 0; col < COLUMN  &&  !canBePlaced; col++) {
                try{
                    if (board.validMove(rolledDice, row, col)) {
                    canBePlaced = true; }
                }catch (Exception ignored){}
            }
        }
        if(!canBePlaced)
            draftPool.addDice(rolledDice);

        return canBePlaced ?
                rolledDice :
                null;
    }

    private Boolean use2() {
        return board.insertDice(rolledDice, toRow, toColumn, Board.Restriction.ALL);
    }
}


class ToolCard7 implements ToolCardStrategy, Serializable {
    private boolean savedReturn = false;

    private DraftPool draftPool;

    private final int parameterSize = parameterSizes[7][0];


    @Override
    public void reset() {
        savedReturn = false;
    }

    @Override
    public Object getReturn() {
        return savedReturn;
    }


    /**
     * Implementation of Tool Card 7
     * @param param all the parameters used by the method
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

        this.draftPool = param.getDraftPool();

        savedReturn = use(draftPool);
        return savedReturn;
    }

    /**
     *implementation of the effect of Tool Card 7
     * Effect of tool card N.7:
     *      re-roll all dices in the draft pool
     * NOTE:
     *      this card can be used only on the second turn before drafting
     *      This constraint is Caller job
     *
     * @param draftPool draftpool of the game
     * @return the success of the operation
     */
    private boolean use(DraftPool draftPool) {
        draftPool.diceList().forEach(Dice::roll);
        return true;
    }
}


class ToolCard8_9 implements ToolCardStrategy, Serializable {
    private boolean savedReturn = false;

    private DraftPool draftPool;
    private int iDiceDraftPool;
//    private int indexDiceDraftPool;
    private Board board;
    private int row;
    private int col;
    private Board.Restriction restriction;

    private final int parameterSize = 4;


    @Override
    public void reset() {
        savedReturn = false;
    }

    @Override
    public Object getReturn() {
        return savedReturn;
    }


    /**
     * implementation of Tool Card 8 & 9
     * @param param list of parameters used
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

        int iParam = 0;
        this.draftPool      = param.getDraftPool();
        this.iDiceDraftPool= param.getParams().get(iParam++);
//        this.indexDiceDraftPool= param.getParams().get(iParam++);
        this.board          = param.getBoard();
        this.row            = param.getParams().get(iParam++);
        this.col            = param.getParams().get(iParam++);
        this.restriction    = Board.Restriction.values()[ param.getParams().get(iParam++) ];

        savedReturn = use(draftPool, iDiceDraftPool, board, row, col, restriction);
//        savedReturn = use(draftPool, indexDiceDraftPool, board, row, col, restriction);
        return savedReturn;
    }

    protected boolean use(Board.Restriction restriction) {
        return use(draftPool, iDiceDraftPool, board, row, col, restriction);
//        return use(draftPool, indexDiceDraftPool, board, row, col, restriction);
    }

    /**
     * implementation of the effect of Tool Card 8 & 9
     * @param draftPool draftpool of the game
     * @param iDiceDraftPool index of the dice of the draftpool
     * @param board board of the player
     * @param row index of the row considered
     * @param col index of the column considered
     * @param restriction kind of restrictions applied to dice positioning
     * @return the success of the operation
     */

    private boolean use(DraftPool draftPool, int iDiceDraftPool, Board board, int row, int col, Board.Restriction restriction) {
//    private boolean use(DraftPool draftPool, int indexDiceDraftPool, Board board, int row, int col, Board.Restriction restriction) {

        try {
            // call validMove before insertDice to avoid undo in case of insertDice fails
            if (board.validMove(draftPool.diceList().get(iDiceDraftPool), row, col, restriction)) {
//            if (board.validMove(draftPool.diceList().get(indexDiceDraftPool), row, col, restriction)) {

                board.insertDice(
                        draftPool.chooseDice(iDiceDraftPool),       // removes from draft pool
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
    private final int parameterSize = parameterSizes[8][0];

    /**
     * application of Tool Card 8
     * Effect of tool card N.8:
     *      place an other dice from draft pool to board
     * Note:
     *      this card can be used only during first turn and the player has already picked
     *      and then the Player MUST skip the picking of second turn of the round
     *
     * @param param list of parameters used
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
        if(!param.getbFirstTurn())
            throw new NotFirstTurnException();

        param.getParams().add(Board.Restriction.ALL.ordinal());
        return super.use(param);
    }
}

class ToolCard9 extends ToolCard8_9 {
    private final int parameterSize = parameterSizes[9][0];

    /**
     * application of Tool Card 9
     * Effect of tool card N.9:
     *      move a dice from draft pool to board, anywhere, without adjacent constraint
     *
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
    private int iDiceDraftPool;
//    private int indexDiceDraftPool;

    private final int parameterSize = parameterSizes[10][0];


    @Override
    public void reset() {
        savedReturn = false;
    }

    @Override
    public Object getReturn() {
        return savedReturn;
    }


    /**
     * implementation of Tool Card 10
     * @param param list of parameters used by Tool Card 10
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

        this.draftPool      = param.getDraftPool();
        this.iDiceDraftPool= param.getParams().get(0);
//        this.indexDiceDraftPool= param.getParams().get(0);

        savedReturn = use(draftPool, iDiceDraftPool);
//        savedReturn = use(draftPool, indexDiceDraftPool);
        return savedReturn;
    }

    /**
     * implementation of the effect of Tool Card 10
     * Effect of tool card N.10:
     *      rotate a dice in draft pool, face up the bottom side
     *
     * @param draftPool draftpool of the game
     * @param iDiceDraftPool index of the dice in the draft pool's dice list
     * @return the success of the operation
     */

    private boolean use(DraftPool draftPool, int iDiceDraftPool) {
//        int index = draftPool.getDiceIndexByID(id);
        int index = iDiceDraftPool;
        Dice dice = draftPool.diceList().get(index);

        // rotate the selected dice
        dice.setNumber(7 - dice.getDiceNumber());
        draftPool.setDice(index, dice);

        return true;
    }
}



class ToolCard11 implements ToolCardStrategy, Serializable {
    private Object savedReturn = null;
    private List<Dice> savedDiceList = null;

    private DraftPool draftPool;
    private int iDiceDraftPool;
    private DiceBag diceBag;

    private Board board;
    private int indexList;
    private int toRow;
    private int toCol;

    private int iProcess = 0;

//    private final int parameterSize = parameterSizes[6]


    @Override
    public void reset() {
        savedReturn = false;
    }

    @Override
    public Object getReturn() {
        return savedReturn;
    }


    /**
     * implementation of Tool Card 11
     * @param param list of the parameters used by Tool Card 11
     * @return the success of the operation
     */
    @Override
    public boolean use(ToolCardParam param) {
        // safety Check
        if(null == param) {
            throw new IncorrectParamQuantityException();
        }
        else if(!param.safetyCheck(parameterSizes[11][iProcess])) {
            throw new IncorrectParamQuantityException(parameterSizes[11][iProcess], param.getParams());
        }

        if(0 == iProcess) {
            this.draftPool = param.getDraftPool();
            this.iDiceDraftPool = param.getParams().get(0);
//        this.indexDiceDraftPool= param.getParams().get(0);
            this.diceBag = param.getDiceBag();

            savedReturn = use1();
//        savedReturn = use(draftPool, indexDiceDraftPool, diceBag);
            if (null != savedReturn) {
                iProcess++;
                return true;
            }
            else {
                reset();        // this is not called
                return false;
            }

        } else if(1 == iProcess) {
            this.board = param.getBoard();
            this.indexList = param.getParams().get(0);
            this.toRow = param.getParams().get(1);
            this.toCol = param.getParams().get(2);

             if( use2().equals(true) ) {
                 reset();
                 return true;
             } else {
                 return false;
             }
        }

        return false;
    }

    /**
     * implementation of the effect of Tool Card 11
     * Effect of tool card N.11:
     *      1. discards a dice in draft pool for
     *         pick a new dice from diceBag and
     *         choose it's value to
     *      2. place the chosen dice in board
     * NOTE:
     *      this is caller job and must be done
     *
//     * @param draftPool draftpool of the game
//     * @param iDiceDraftPool index of the dice of the draftpool
//     * @param diceBag dicebag of the game
     * @return the success of the operation
     */
    private List<Dice> use1 () { //(DraftPool draftPool, int iDiceDraftPool, DiceBag diceBag) {
        diceBag.addDice(
                draftPool.chooseDice(iDiceDraftPool));
        Dice extracted = diceBag.extractDice();
        savedDiceList = new ArrayList<>();
        for (int i = 1; i <= MAX_DICE_NUMBER; i++) {
            savedDiceList.add(new Dice(i, extracted.getColorDice(), extracted.getId()));
        }
        return savedDiceList;
    }

    private Boolean use2() {
        return board.insertDice(savedDiceList.get(indexList), toRow, toCol, Board.Restriction.ALL);
    }
}



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

    private final int parameterSize = parameterSizes[12][0];


    @Override
    public void reset() {
        savedReturn = false;
    }

    @Override
    public Object getReturn() {
        return savedReturn;
    }


    /**
     * implementation of the effect of Tool Card 12
     * Effect of tool card N.12:
     *      Move 2 dice in the board, respecting ALL constraints
     *      These dices must have the same color and this color's dice exists in the round track
     *
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

}

