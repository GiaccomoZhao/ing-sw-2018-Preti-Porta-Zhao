package porprezhas.Network.Command;

import java.io.Serializable;
import java.net.Socket;

public interface Action extends Serializable {
    Answer handle(ActionHandler actionHandler);
}
