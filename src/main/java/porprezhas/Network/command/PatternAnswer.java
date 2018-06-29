package porprezhas.Network.command;

public class PatternAnswer implements Answer {

    public final Boolean answer;

    public PatternAnswer(Boolean answer) {
        this.answer = answer;
    }

    @Override
    public void handle(AnswerHandler answerHandler) {
        answerHandler.handle(this);
    }
}
