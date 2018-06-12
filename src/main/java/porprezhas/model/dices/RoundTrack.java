package porprezhas.model.dices;

import porprezhas.model.Game;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.DraftPool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RoundTrack implements Serializable {

    int actualRound;
    List<Dice>[] track;

    public RoundTrack() {
        track= new ArrayList[10];
        actualRound=1;
    }
    public int getActualRound() {
        return actualRound;
    }

    public List<Dice>[] getTrack() {
        return track;
    }

    public List<Dice> getRoundDice(int round){
        return track[round-1];
    }

    public void addDice(DraftPool draftPool){
        List<Dice> diceList = draftPool.diceList();
        if(null != diceList  &&  diceList.size() > 0)
            track[actualRound-1] = diceList;
        actualRound++;
    }
    public void addDice(int round_1, Dice dice){
        if(track[round_1-1]==null){
            track[round_1-1] = new ArrayList<>();
        }
        track[round_1-1].add(dice);
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