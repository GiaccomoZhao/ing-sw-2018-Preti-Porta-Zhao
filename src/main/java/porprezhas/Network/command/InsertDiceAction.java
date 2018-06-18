package porprezhas.Network.command;

public class InsertDiceAction implements Action {
    public final String username;
    public final long diceId;
    public final int row;
    public final int col;

    public InsertDiceAction(String username, long diceId, int row, int col) {
        this.username = username;
        this.diceId = diceId;
        this.row = row;
        this.col = col;
    }

    @Override
    public Answer handle(ActionHandler actionHandler) {
       return  actionHandler.handle(this);
    }
}
