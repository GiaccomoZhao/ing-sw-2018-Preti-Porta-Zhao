package porprezha.model.track;

import porprezha.model.Game;

import java.util.ArrayList;

public class RoundTrack {
    class Dice { ;}
    class Riserva {ListOfDices dices ;}
    class ListOfDices extends ArrayList<Dice> {
        ;
    }

    private int actualRound;
    private ListOfDices[] dices;

    public RoundTrack() {
        dices = new ListOfDices[Game.GameConstants.ROUND_NUM];
        actualRound = 0;
    }

    public void addDice(Riserva riserva) {
        for (Dice d : riserva.dices) {
            dices[actualRound].add(d);
        }
    }
    public int getRound() {
        return actualRound;
    }
}