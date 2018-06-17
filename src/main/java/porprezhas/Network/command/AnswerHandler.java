package porprezhas.Network.command;

public interface AnswerHandler {

    boolean handle(UpdateAnswer updateAnswer);

    boolean handle(LoginActionAnswer loginActionAnswer);

    boolean handle(JoinActionAnswer joinActionAnswer);

    boolean handle(PassActionAnswer passActionAnswer);

    boolean handle(DiceInsertedAnswer diceInsertedAnswer);
}
