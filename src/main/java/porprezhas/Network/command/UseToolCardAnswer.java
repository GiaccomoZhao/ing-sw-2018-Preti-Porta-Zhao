package porprezhas.Network.command;

public class UseToolCardAnswer implements Answer {

    public final Boolean answer;
    public Exception exception;

    public UseToolCardAnswer(Boolean answer) {
        this.answer = answer;
        exception=null;
    }

    public UseToolCardAnswer(Boolean answer, Exception exception) {
        this.answer = answer;
        this.exception = exception;
    }

    @Override
    public void handle(AnswerHandler answerHandler) {
        answerHandler.handle(this);
    }
}
