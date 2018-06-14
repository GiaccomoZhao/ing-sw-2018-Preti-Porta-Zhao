package porprezhas.model.cards;

import porprezhas.model.dices.*;

import java.util.List;

public class ToolCardParam {
    private final RoundTrack roundTrack;
    private final DraftPool draftPool;
    private final DiceBag diceBag;
    private final Board board;
    private final List<Integer> params;


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
