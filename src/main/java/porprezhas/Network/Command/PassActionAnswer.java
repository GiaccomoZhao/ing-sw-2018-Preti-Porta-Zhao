package porprezhas.Network.Command;

public class PassActionAnswer implements Answer {
    public final Boolean answer;

    public PassActionAnswer(Boolean answer) {
        this.answer = answer;
    }


    @Override
    public void handle(AnswerHandler answerHandler) {
        answerHandler.handle(this);
    }
}
