package porprezhas.model.cards;

import porprezhas.model.dices.*;

import java.util.List;

public class ToolCardParam {
    private final RoundTrack roundTrack;
    private final DraftPool draftPool;
    private final DiceBag diceBag;
    private final Dice dice;
    private final List<Board> boards;
    private final List<Integer> params;


    public ToolCardParam(RoundTrack roundTrack, DraftPool draftPool, DiceBag diceBag, Dice dice, List<Board> boards, List<Integer> params) {
        this.roundTrack = roundTrack;
        this.draftPool  = draftPool;
        this.diceBag    = diceBag;
        this.dice       = dice;
        this.boards     = boards;
        this.params     = params;
    }

    public ToolCardParam(ToolCardParam param, int fromIndex, int toIndex) {
        this.roundTrack = param.getRoundTrack();
        this.draftPool  = param.getDraftPool();
        this.diceBag    = param.getDiceBag();
        this.dice       = param.getDice();
        this.boards     = param.getBoards();
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

    public Dice getDice() {
        return dice;
    }

    public List<Board> getBoards() {
        return boards;
    }

    public List<Integer> getParams() {
        return params;
    }
}
