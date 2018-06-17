package porprezhas.Network.command;

public class JoinAction implements  Action {

    public final String username;

    public JoinAction(String username) {
        this.username = username;
    }

    @Override
    public Answer handle(ActionHandler actionHandler) {
       return actionHandler.handle(this);
    }
}
