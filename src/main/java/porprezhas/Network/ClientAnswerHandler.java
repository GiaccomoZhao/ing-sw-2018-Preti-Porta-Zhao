package porprezhas.Network;

import porprezhas.Network.Command.*;

public class ClientAnswerHandler implements AnswerHandler {

    private ViewUpdateHandlerInterface viewUpdateHandlerInterface;


    public void setViewUpdateHandlerInterface(ViewUpdateHandlerInterface viewUpdateHandlerInterface) {
        this.viewUpdateHandlerInterface = viewUpdateHandlerInterface;
    }


    @Override
    public boolean handle(UpdateAnswer updateAnswer) {

        this.viewUpdateHandlerInterface.update(updateAnswer.serializableGameInterface);
        return true;
    }

    @Override
    public boolean handle(LoginActionAnswer loginActionAnswer) {

    return loginActionAnswer.answer;

    }

    @Override
    public boolean handle(JoinActionAnswer joinActionAnswer) {

        return joinActionAnswer.answer;
    }

    @Override
    public boolean handle(PassActionAnswer passActionAnswer) {
        if (!passActionAnswer.answer.equals(true))
            System.out.println("It's not your turn!");
        return true;
    }

    @Override
    public boolean handle(DiceInsertedAnswer diceInsertedAnswer) {
        return false;
    }
}
