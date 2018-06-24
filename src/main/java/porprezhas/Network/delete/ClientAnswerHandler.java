package porprezhas.Network.delete;

import porprezhas.Network.ViewUpdateHandlerInterface;
import porprezhas.Network.command.*;

import static porprezhas.control.ServerController.USERNAME_AVAILABLE;

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
    if (loginActionAnswer.answer == USERNAME_AVAILABLE)
        return true;
    return false;

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

        System.out.println("Il tentativo è: " + diceInsertedAnswer.answer);
        return false;
    }
}
