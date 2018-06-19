package porprezhas.exceptions.diceMove;

public class DiceNotFoundInDraftPoolException extends RuntimeException {
    public DiceNotFoundInDraftPoolException(String message) {
        super(message);
    }
    public DiceNotFoundInDraftPoolException(long diceID) {
        super("dice with id = " + diceID + " does NOT Exist in Draft Pool");
    }
}
