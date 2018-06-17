package porprezhas.exceptions.toolCard;

public class ToolCardParameterException extends RuntimeException{
    public ToolCardParameterException(String message) {
        super("Can not use this Card with given action!\n"
                + message);
    }
}
