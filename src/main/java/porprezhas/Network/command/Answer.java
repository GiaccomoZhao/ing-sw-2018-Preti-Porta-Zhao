package porprezhas.Network.command;

import java.io.Serializable;

public interface Answer extends Serializable {
   void handle(AnswerHandler answerHandler);
}
