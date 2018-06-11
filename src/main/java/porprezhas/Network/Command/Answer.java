package porprezhas.Network.Command;

import java.io.Serializable;

public interface Answer extends Serializable {
    void handle(AnswerHandler answerHandler);
}
