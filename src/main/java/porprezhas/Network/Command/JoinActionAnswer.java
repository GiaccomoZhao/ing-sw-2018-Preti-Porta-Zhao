package porprezhas.Network.Command;

public class JoinActionAnswer implements Answer {

    public final Boolean answer;

    public JoinActionAnswer(Boolean answer) {
        this.answer = answer;
    }
    @Override
    public void handle(AnswerHandler answerHandler) {
        answerHandler.handle(this);
    }
}
