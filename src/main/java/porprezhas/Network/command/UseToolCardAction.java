package porprezhas.Network.command;

import java.util.ArrayList;

public class UseToolCardAction implements Action {
    public final String username;
    public  final int toolCardID;
    public final ArrayList<Integer> paramList;

    public UseToolCardAction(String username, int toolCardID, ArrayList<Integer> paramList) {
        this.username = username;
        this.toolCardID = toolCardID;
        this.paramList = paramList;
    }

    @Override
    public Answer handle(ActionHandler actionHandler) {
        return actionHandler.handle(this);
    }
}
