package porprezhas.Network.Command;

import java.io.Serializable;

public interface Answer extends Serializable {
    boolean handle(AnswerHandler answerHandler);
}
