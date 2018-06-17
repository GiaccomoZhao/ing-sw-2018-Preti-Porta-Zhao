package porprezhas.Network.command;

import java.io.Serializable;

public interface Answer extends Serializable {
    boolean handle(AnswerHandler answerHandler);
}
