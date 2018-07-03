package porprezhas.exceptions.diceMove;

import porprezhas.exceptions.GenericInvalidActionException;

public class DiceNotFoundInDraftPoolException extends GenericInvalidActionException {
    public DiceNotFoundInDraftPoolException(String message) {
        super(message);
    }
    public DiceNotFoundInDraftPoolException(long diceID) {
        super("dice with id = " + diceID + " does NOT Exist in Draft Pool");
    }
}
