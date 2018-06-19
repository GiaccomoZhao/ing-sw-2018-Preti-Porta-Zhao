package porprezhas.exceptions.toolCard;

public class DifferentColorException extends ToolCardParameterException {
    public DifferentColorException(String message) {
        super(message);
    }
    public DifferentColorException(Object dice1, Object dice2) {
        super("The dices to move in Board has NOT the same color: \n" +
                "Dice1= " + dice1 + " \t" +
                "Dice2= " + dice2
        );
    }
}
