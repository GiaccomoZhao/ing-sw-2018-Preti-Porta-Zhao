package porprezhas.exceptions.diceMove;

import porprezhas.exceptions.GenericInvalidActionException;

public class AlreadyPickedException extends GenericInvalidActionException {
    public AlreadyPickedException(String message) {
        super(message);
    }
}
