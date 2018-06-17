package porprezhas.exceptions.toolCard;

public class NotRemovableDiceException extends ToolCardParameterException {
    public NotRemovableDiceException(String message) {
        super(message);
    }

    public NotRemovableDiceException(int row, int column, String board) {
        super("The dice in position = (" + row + ":" + column + ") " + "can not be removed!\n"
                + board);
    }
}
