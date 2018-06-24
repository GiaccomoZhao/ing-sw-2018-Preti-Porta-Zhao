package porprezhas.Network.command;

public class LoginActionAnswer implements Answer {

    public final String username;
    public final int answer;

    public LoginActionAnswer(int answer, String username) {
        this.answer = answer;
        this.username = username;
    }


    @Override
    public void handle(AnswerHandler answerHandler) {
       answerHandler.handle(this);
    }
}
