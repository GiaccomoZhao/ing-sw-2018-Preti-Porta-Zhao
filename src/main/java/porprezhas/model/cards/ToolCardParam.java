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
        this.draftPool = draftPool;
        this.diceBag = diceBag;
        this.dice = dice;
        this.boards = boards;
        this.params = params;
    }


    public DraftPool getDraftPool() {
        return draftPool;
    }

    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    public List<Board> getBoards() {
        return boards;
    }

    public List<Integer> getParams() {
        return params;
    }
}
