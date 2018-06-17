package porprezhas.Network.command;

import porprezhas.model.SerializableGameInterface;

public class UpdateAnswer implements Answer {
    public final SerializableGameInterface serializableGameInterface;

    public UpdateAnswer(SerializableGameInterface serializableGameInterface) {
        this.serializableGameInterface = serializableGameInterface;
    }

    @Override
    public boolean handle(AnswerHandler answerHandler) {
       return answerHandler.handle(this);
    }
}
