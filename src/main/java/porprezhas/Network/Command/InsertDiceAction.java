package porprezhas.Network.Command;

public class InsertDiceAction implements Action {
    public final String username;
    public final int diceIndex;
    public final int row;
    public final int col;

    public InsertDiceAction(String username, int diceIndex, int row, int col) {
        this.username = username;
        this.diceIndex = diceIndex;
        this.row = row;
        this.col = col;
    }

    @Override
    public Answer handle(ActionHandler actionHandler) {
       return  actionHandler.handle(this);
    }
}
