package porprezhas.Network.Command;

public class PassActionAnswer implements Answer {
    public final Boolean answer;

    public PassActionAnswer(Boolean answer) {
        this.answer = answer;
    }


    @Override
    public boolean handle(AnswerHandler answerHandler) {
       return answerHandler.handle(this);
    }
}
