package porprezhas.Network.command;

public class DiceInsertedAnswer implements Answer {
    public final Boolean answer;


    public DiceInsertedAnswer(Boolean answer) {
        this.answer = answer;

    }

    @Override
    public boolean handle(AnswerHandler answerHandler) {
        return answerHandler.handle(this);
    }
}
