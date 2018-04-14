package PorPreZha.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RoundTrack {
    class Dice { ;}
    class Riserva {ListOfDices dices ;}
    class ListOfDices extends ArrayList<Dice> {
        ;
    }

    private int actualRound;
    private ListOfDices[] dices;

    RoundTrack() {
        dices = new ListOfDices[Game.GameConstants.ROUND_NUM];
        actualRound = 0;
    }

    void addDice(Riserva riserva) {
        for (Dice d : riserva.dices) {
            dices[actualRound].add(d);
        }
    }
    int getRound() {
        return actualRound;
    }
}
