package porprezhas.exceptions.toolCard;

public class NotFirstTurnException extends ToolCardParameterException {
    public NotFirstTurnException(String message) {
        super(message);
    }

    public NotFirstTurnException() {
        super("You can not use this tool card at second turn");
    }
}
