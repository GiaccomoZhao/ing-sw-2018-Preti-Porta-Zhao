package porprezhas.exceptions.diceMove;

import porprezhas.exceptions.GenericInvalidActionException;

public class BoardCellOccupiedException extends GenericInvalidActionException {
    public BoardCellOccupiedException(String message) {
        super(message);
    }
}
