package porprezhas.Network.command;

public class ChoosePrivateAction implements Action {
    public final String username;
    public final int choosen;

    public ChoosePrivateAction(String username, int choosen) {
        this.username = username;
        this.choosen = choosen;
    }

    @Override
    public Answer handle(ActionHandler actionHandler) {
        return actionHandler.handle(this);
    }
}
