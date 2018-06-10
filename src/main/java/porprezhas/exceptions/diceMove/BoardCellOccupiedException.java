package porprezhas.exceptions.diceMove;

public class BoardCellOccupiedException extends RuntimeException{
    public BoardCellOccupiedException(String message) {
        super(message);
    }
}
