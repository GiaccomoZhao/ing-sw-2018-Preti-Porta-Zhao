package porprezhas.Network.command;

import porprezhas.model.dices.Dice;

import java.util.List;

public class CardEffectAnswer implements Answer {

    public final List<Dice> diceList;


    public CardEffectAnswer(List<Dice> diceList) {
        this.diceList = diceList;
    }

    @Override
    public void handle(AnswerHandler answerHandler) {
        answerHandler.handle(this);
    }
}
