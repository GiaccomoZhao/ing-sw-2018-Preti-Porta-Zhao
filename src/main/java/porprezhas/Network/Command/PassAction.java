package porprezhas.Network.Command;

public class PassAction implements Action {
    public final String username;

    public PassAction(String username) {
        this.username = username;
    }

    @Override
    public Answer handle(ActionHandler actionHandler) {
      return  actionHandler.handle(this);
    }
}
