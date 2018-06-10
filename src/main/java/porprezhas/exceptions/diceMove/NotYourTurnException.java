package porprezhas.exceptions.diceMove;

public class NotYourTurnException extends RuntimeException{
    public NotYourTurnException(String message) {
        super(message);
    }
}
