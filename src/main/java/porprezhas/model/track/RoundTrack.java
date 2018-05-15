package porprezhas.model.track;

import porprezhas.model.Game;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.DraftPool;

import java.util.ArrayList;


public class RoundTrack {

    ArrayList<Dice>[] track;
    int actualRound;

    public RoundTrack() {
        track= new ArrayList[10];
        actualRound=1;
    }
    public int getActualRound() {
        return actualRound;
    }

    public ArrayList<Dice> getRoundDice(int round){
        return track[round-1];
    }

    public void addDice(DraftPool draftPool){
        track[actualRound-1]=draftPool.diceList();
        actualRound++;
    }
}