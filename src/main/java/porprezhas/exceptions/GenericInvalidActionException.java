package porprezhas.exceptions;

public class GenericInvalidActionException extends RuntimeException{
    public GenericInvalidActionException() {
    }

    public GenericInvalidActionException(String message) {
        super(message);
    }
}
