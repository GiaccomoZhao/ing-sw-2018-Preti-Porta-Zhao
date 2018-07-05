package porprezhas.Network.command;

import porprezhas.model.Game;

public class JoinSinglePlayerAction implements Action {
    public final String username;
    public final Game.SolitaireDifficulty solitaireDifficulty;

    public JoinSinglePlayerAction(String username, Game.SolitaireDifficulty solitaireDifficulty) {
        this.username = username;
        this.solitaireDifficulty = solitaireDifficulty;
    }

    @Override
    public Answer handle(ActionHandler actionHandler) {
        return actionHandler.handle(this);
    }
}
