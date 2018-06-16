package porprezhas.exceptions.diceMove;

public class InvalidMoveException extends RuntimeException {
    public InvalidMoveException(String message) {
        super("Invalid Move!\n" + message);
    }
}
