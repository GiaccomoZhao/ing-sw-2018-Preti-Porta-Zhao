package porprezhas.Network.command;

public class ChoosePatternAction implements Action {

    public final int choosenPattern;
    public final String username;

    public ChoosePatternAction(int choosenPattern, String username) {
        this.choosenPattern = choosenPattern;
        this.username = username;
    }

    @Override
    public Answer handle(ActionHandler actionHandler) {

        return actionHandler.handle(this);
    }
}
