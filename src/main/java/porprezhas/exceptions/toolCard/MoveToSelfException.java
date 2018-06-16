package porprezhas.exceptions.toolCard;

public class MoveToSelfException extends ToolCardParameterException {
    public MoveToSelfException() {
        super("Okay! I do not move this Dice! And i do not eat your Tokens.");
    }

    public MoveToSelfException(String message) {
        super(message);
    }
}
