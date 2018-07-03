package porprezhas.exceptions.toolCard;

import porprezhas.exceptions.GenericInvalidActionException;

public class ToolCardParameterException extends GenericInvalidActionException {
    public ToolCardParameterException(String message) {
        super("Can not use this Card with given action!\n"
                + message);
    }
}
