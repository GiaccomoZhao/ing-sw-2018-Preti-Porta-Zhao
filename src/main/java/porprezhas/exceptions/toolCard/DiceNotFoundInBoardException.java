package porprezhas.exceptions.toolCard;

public class DiceNotFoundInBoardException extends ToolCardParameterException {
    public DiceNotFoundInBoardException(String message) {
        super(message);
    }

    public DiceNotFoundInBoardException(int row, int col) {
        super("The specified Dice in board (" + row + ":" + col + ") is NOT FOUND");
    }
}
