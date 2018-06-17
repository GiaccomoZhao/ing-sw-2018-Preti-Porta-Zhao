package porprezhas.Network.command;

import java.io.Serializable;

public interface Action extends Serializable {
    Answer handle(ActionHandler actionHandler);
}
