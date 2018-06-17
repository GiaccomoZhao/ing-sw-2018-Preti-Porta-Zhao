package porprezhas.exceptions;

public class GameAbnormalException extends RuntimeException {
    public GameAbnormalException(String message) {
        super( "An Abnormal Error has occurred in Game:\n"
                + message);
    }
}
