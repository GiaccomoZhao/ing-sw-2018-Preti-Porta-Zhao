package porprezhas.Network.command;

public class JoinActionAnswer implements Answer {

    public final Boolean answer;

    public JoinActionAnswer(Boolean answer) {
        this.answer = answer;
    }
    @Override
    public boolean handle(AnswerHandler answerHandler) {
      return   answerHandler.handle(this);
    }
}
