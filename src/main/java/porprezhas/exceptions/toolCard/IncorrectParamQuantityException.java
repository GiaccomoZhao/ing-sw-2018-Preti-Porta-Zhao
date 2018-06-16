package porprezhas.exceptions.toolCard;

import java.util.List;

public class IncorrectParamQuantityException extends ToolCardParameterException{
    public IncorrectParamQuantityException(String message) {
        super(message);
    }

    public IncorrectParamQuantityException() {
        super("No Parameter has been given!");
    }

    public IncorrectParamQuantityException(int excepted, List<Integer> actual) {
        super("Incorrect parameter quantity: \texpected = " + excepted + "\tactual = " + actual);
    }

}
