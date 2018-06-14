package porprezhas.model.cards;

import porprezhas.model.dices.*;

import java.util.List;

public class ToolCardParam {
    private final RoundTrack roundTrack;
    private final DraftPool draftPool;
    private final DiceBag diceBag;
    private final Board board;
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


    public ToolCardParam(RoundTrack roundTrack, DraftPool draftPool, DiceBag diceBag, Board board, List<Integer> params) {
        this.roundTrack = roundTrack;
        this.draftPool  = draftPool;
        this.diceBag    = diceBag;
        this.board     = board;
        this.params     = params;
    }

    public ToolCardParam(ToolCardParam param, int fromIndex, int toIndex) {
        this.roundTrack = param.getRoundTrack();
        this.draftPool  = param.getDraftPool();
        this.diceBag    = param.getDiceBag();
        this.board     = param.getBoard();
        this.params     = param.getParams().subList(fromIndex, toIndex);
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

    public List<Integer> getParams() {
        return params;
    }
}
