package porprezhas.Network.command;

public class DiceInsertedAnswer implements Answer {
    public final Boolean answer;


    public DiceInsertedAnswer(Boolean answer) {
        this.answer = answer;

    }

    @Override
    public void handle(AnswerHandler answerHandler) {
        answerHandler.handle(this);
    }
}
