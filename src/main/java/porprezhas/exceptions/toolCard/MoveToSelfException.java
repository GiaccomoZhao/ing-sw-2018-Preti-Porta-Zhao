package porprezhas.exceptions.toolCard;

public class MoveToSelfException extends ToolCardParameterException {
    public MoveToSelfException() {
        super("The selected Dice will not be moved and the the tokens will not variate.");
    }

    public MoveToSelfException(String message) {
        super(message);
    }
}
