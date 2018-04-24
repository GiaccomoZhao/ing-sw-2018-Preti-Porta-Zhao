package porprezhas.model.track;

import porprezhas.model.Game;

import java.util.ArrayList;


public class RoundTrack {
    class Dice { ;}
    class DraftPool {ListOfDices dices ;}
    class ListOfDices extends ArrayList<Dice> {
        ;
    }

    private int actualRound;
    private ArrayList<Dice>[] dices;

    public RoundTrack() {
        dices = new ListOfDices[Game.GameConstants.ROUND_NUM];
        actualRound = 0;
    }


    public void addDice(DraftPool draftPool) {
        for (Dice d : draftPool.dices) {
            dices[actualRound].add(d);

        }
    }

    public void removeDice(Dice dice) {
            dices[actualRound].remove(dice);
    }

    public void addDice(Dice dice) {
        dices[getRound()].add(dice);
    }

    public int getRound() {
        return actualRound;
    }
}