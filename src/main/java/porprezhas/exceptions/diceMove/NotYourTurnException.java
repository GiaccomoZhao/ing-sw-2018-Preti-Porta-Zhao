package porprezhas.exceptions.diceMove;

import porprezhas.exceptions.GenericInvalidActionException;

public class NotYourTurnException extends GenericInvalidActionException {
    public NotYourTurnException(String message) {
        super(message);
    }
}
