package porprezhas.Network.command;

public class LoginActionAnswer implements Answer {
    public final Boolean answer;
    public final String username;

    public LoginActionAnswer(Boolean answer, String username) {
        this.answer = answer;
        this.username = username;
    }


    @Override
    public boolean handle(AnswerHandler answerHandler) {
       return answerHandler.handle(this);
    }
}
