package porprezhas.exceptions.diceMove;

import porprezhas.exceptions.GenericInvalidActionException;

public class InvalidMoveException extends GenericInvalidActionException {
    public InvalidMoveException(String message) {
        super("Invalid Move!\n" + message);
    }
}
