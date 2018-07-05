package porprezhas.exceptions.toolCard;

public class WrongTurnException extends ToolCardParameterException {
//    public WrongTurnException(String message) {
//        super(message);
//    }


    public WrongTurnException(Boolean bFirstTurn) {
        super("You can not use this tool card at " +
                (bFirstTurn ? "First" : "Second")
                + " turn");
    }

    public WrongTurnException() {
        super("You can not use this tool card at current turn, retry at next turn");
    }
}
