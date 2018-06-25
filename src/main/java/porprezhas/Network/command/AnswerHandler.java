package porprezhas.Network.command;

public interface AnswerHandler {

    void handle(UpdateAnswer updateAnswer);

    void handle(LoginActionAnswer loginActionAnswer);

    void handle(JoinActionAnswer joinActionAnswer);

    void handle(PassActionAnswer passActionAnswer);

    void handle(DiceInsertedAnswer diceInsertedAnswer);

    void handle(PatternAnswer patternAnswer);
}
