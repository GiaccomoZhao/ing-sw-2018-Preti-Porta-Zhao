package porprezhas.exceptions.toolCard;

public class NotEnoughTokenException extends ToolCardParameterException {
    public NotEnoughTokenException(String message) {
        super(message);
    }
    public NotEnoughTokenException(){super("You haven't enough tokens to use this Tool card");}
}
