package porprezhas.exceptions.diceMove;

public class AdjacentRestrictionException extends InvalidMoveException {
    public AdjacentRestrictionException(String message) {
        super("You must respect the Adjacent constraint!\n"
                + message);
    }
}
