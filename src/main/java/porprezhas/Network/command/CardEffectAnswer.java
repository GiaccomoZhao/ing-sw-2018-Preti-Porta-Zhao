package porprezhas.Network.command;

public class CardEffectAnswer implements Answer {

    public final Object object;


    public CardEffectAnswer(Object object) {
        this.object = object;
    }

    @Override
    public void handle(AnswerHandler answerHandler) {
        answerHandler.handle(this);
    }
}
