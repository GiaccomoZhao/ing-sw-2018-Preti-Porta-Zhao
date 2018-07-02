package porprezhas.model.cards;

import porprezhas.model.dices.*;
import porprezhas.view.fx.gameScene.state.DiceContainerType;

import java.util.ArrayList;
import java.util.List;

public class ToolCardParam {
    private final RoundTrack roundTrack;
    private final DraftPool draftPool;
    private final DiceBag diceBag;
    private final Board board;
    private final Boolean bFirstTurn;
    private final List<Integer> params;

    enum IncDec {
        DECREMENT, INCREMENT;

        public static Boolean toBoolean(IncDec incDec) {
            return incDec.equals(INCREMENT)? Boolean.TRUE : Boolean.FALSE;
        }

        public static IncDec fromInteger(Integer integer) {
            return integer == 0 ? DECREMENT : INCREMENT;    // 0 means DEC, Any no 0 means INC
        }


        public static Boolean fromIntegerToBoolean(Integer integer) {
            return toBoolean( fromInteger(integer) );
        }


        public Integer toInteger(){
            return this.equals(DECREMENT)? 0 : 1;   // 0 means DEC, 1 is just a arbitrary number
        }

        public Boolean toBoolean() {
            return this.equals(DECREMENT)? Boolean.FALSE : Boolean.TRUE;   // FALSE means DECREMENT, TRUE means INCREMENT
        }
    }


    /**
     * this is used by ServerController
     *
     * @param roundTrack current game's round track, given by server
     * @param draftPool  current game's draft pool
     * @param diceBag    current game's dice bag
     * @param board      current player's board
     * @param bFirstTurn current game's turn
     * @param params     parameters given by client
     */
    public ToolCardParam(RoundTrack roundTrack, DraftPool draftPool, DiceBag diceBag, Board board, Boolean bFirstTurn, List<Integer> params) {
        this.roundTrack = roundTrack;
        this.draftPool  = draftPool;
        this.diceBag    = diceBag;
        this.board      = board;
        this.bFirstTurn = bFirstTurn;
        this.params     = params;
    }


    /**
     * initialize a new tool card Param
     * this is used only by tests, to avoid modification in the completed tests
     *
     * @param roundTrack current game's round track, given by server
     * @param draftPool  current game's draft pool
     * @param diceBag    current game's dice bag
     * @param board      current player's board
     * @param params     parameters given by client
     */
    public ToolCardParam(RoundTrack roundTrack, DraftPool draftPool, DiceBag diceBag, Board board, List<Integer> params) {
        this.roundTrack = roundTrack;
        this.draftPool  = draftPool;
        this.diceBag    = diceBag;
        this.board      = board;
        this.bFirstTurn = false;
        this.params     = params;
    }

    /**
     * used for ToolCard 4 that has 2 sub parameters
     *
     * @param param  the old param to copy
     * @param fromIndex   copy from index
     * @param toIndex   copy until index
     */
    public ToolCardParam(ToolCardParam param, int fromIndex, int toIndex) {
        this.roundTrack = param.getRoundTrack();
        this.draftPool  = param.getDraftPool();
        this.diceBag    = param.getDiceBag();
        this.board      = param.getBoard();
        this.bFirstTurn = param.getbFirstTurn();
        this.params     = param.getParams().subList(fromIndex, toIndex);
    }

    /**
     * used for ToolCard that has need to add some integer parameter
     *
     * @param param  the old param to copy
     * @param addIntegerParam   the new integer to add
     */
    public ToolCardParam(ToolCardParam param, Integer addIntegerParam) {
        // NB: Arrays.asList() returns AbstractList that hasn't implemented a correct add method
        // So: to avoid adding on a AbstractList, we must create a new Concrete List, like ArrayList
        ArrayList newParams = new ArrayList(param.getParams());
        newParams.add(addIntegerParam);
        this.params     = newParams;

        this.roundTrack = param.getRoundTrack();
        this.draftPool  = param.getDraftPool();
        this.diceBag    = param.getDiceBag();
        this.board      = param.getBoard();
        this.bFirstTurn = param.getbFirstTurn();
    }

    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    public DraftPool getDraftPool() {
        return draftPool;
    }

    public DiceBag getDiceBag() {
        return diceBag;
    }

    public Board getBoard() {
        return board;
    }

    public Boolean getbFirstTurn() {
        return bFirstTurn;
    }

    public List<Integer> getParams() {
        return params;
    }



    public boolean safetyCheck(int parameterSize) {
        if(null == this.getParams()  ||  this.getParams().size() != parameterSize)
            return false;
        return true;
    }

}
