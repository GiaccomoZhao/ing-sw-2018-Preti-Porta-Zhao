package porprezhas.exceptions.toolCard;

public class PickedDiceException extends ToolCardParameterException {
    public PickedDiceException(Boolean bPicked) {
        super(  "You " +
                (bPicked ? "have already" : "haven't") +
                " picked a Dice" +
                (bPicked ? "" : "yet") +
                "!"
        );
    }
}
