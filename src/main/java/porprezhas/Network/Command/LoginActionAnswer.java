package porprezhas.Network.Command;

public class LoginActionAnswer implements Answer {
    public final Boolean answer;
    public final String username;

    public LoginActionAnswer(Boolean answer, String username) {
        this.answer = answer;
        this.username = username;
    }


    @Override
    public void handle(AnswerHandler answerHandler) {
        answerHandler.handle(this);
    }
}
