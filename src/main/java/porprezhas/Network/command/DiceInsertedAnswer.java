package porprezhas.Network.command;

public class DiceInsertedAnswer implements Answer {
    public final Boolean answer;
    public  Exception exception;


    public DiceInsertedAnswer(Boolean answer) {
        this.answer = answer;

    }
    public DiceInsertedAnswer(Boolean answer, Exception exception) {
        this.answer = answer;
        this.exception = exception;

    }
    @Override
    public void handle(AnswerHandler answerHandler) {
        answerHandler.handle(this);
    }
}
