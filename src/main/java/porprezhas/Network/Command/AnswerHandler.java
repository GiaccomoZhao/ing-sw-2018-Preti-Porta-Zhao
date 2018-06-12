package porprezhas.Network.Command;

public interface AnswerHandler {

    void handle(UpdateAnswer updateAnswer);

    void handle(LoginActionAnswer loginActionAnswer);

    void handle(JoinActionAnswer joinActionAnswer);

    void handle(PassActionAnswer passActionAnswer);

    Boolean handle(DiceInsertedAnswer diceInsertedAnswer);
}
