package porprezhas.model.dices;

import porprezhas.model.Game;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.DraftPool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class RoundTrack implements Serializable {

    int actualRound;
    ArrayList<Dice>[] track;

    public RoundTrack() {
        track= new ArrayList[10];
        actualRound=1;
    }
    public int getActualRound() {
        return actualRound;
    }

    public ArrayList<Dice>[] getTrack() {
        return track;
    }

    public ArrayList<Dice> getRoundDice(int round){
        return track[round-1];
    }

    public void addDice(DraftPool draftPool){
        track[actualRound-1]=draftPool.diceList();
        actualRound++;
    }
    public void addDice(int round, Dice dice){
        if(track[round-1]==null){
            track[round-1] = new ArrayList<>();
        }
        track[round-1].add(dice);
    }

    public void removeDice(int round,Dice dice){
        track[round-1].remove(dice);
    }


    @Override
    public String toString() {
        StringBuilder sbRoundTrack = new StringBuilder(
                "RoundTrack{ " +
                "actualRound=" + actualRound +
                ", track: " );
        for (int i = 1; i <= actualRound; i++) {
            sbRoundTrack.append('\n');
            sbRoundTrack.append(i);
            sbRoundTrack.append(" → ");
            sbRoundTrack.append(Arrays.asList(track[i-1]));
        }
        sbRoundTrack.append(" }");
        return sbRoundTrack.toString();
    }
}