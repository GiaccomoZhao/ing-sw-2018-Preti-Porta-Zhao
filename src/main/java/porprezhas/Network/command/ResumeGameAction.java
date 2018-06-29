package porprezhas.Network.command;

public class ResumeGameAction implements Action {

    public final String username;

    public ResumeGameAction(String username) {
        this.username = username;
    }

    @Override
    public Answer handle(ActionHandler actionHandler) {
        return actionHandler.handle(this);
    }
}
