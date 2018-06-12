package porprezhas.model.dices;

import porprezhas.Useful;
import porprezhas.model.Game;
import porprezhas.model.GameConstants;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.DraftPool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static porprezhas.model.GameConstants.ROUND_NUM;


public class RoundTrack implements Serializable {

    private int actualRound;
    private List<Dice>[] track;

    public RoundTrack() {
        track= new ArrayList[ROUND_NUM];
        actualRound = 0;        // NOTE: this round counter really starts from 1
                                // Game Controller will start the game calling:
                                // addDice(emptyDraftPool){ nextRound(); }
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


    public int nextRound() {
        return ++actualRound;
    }

    public void addDice(DraftPool draftPool){
        List<Dice> diceList = draftPool.diceList();

        // safety check
        if(null != diceList  &&  diceList.size() > 0) {
            if(Useful.isValueOutOfBounds(actualRound, 1, ROUND_NUM)) {
                throw new IndexOutOfBoundsException(
                        "Fatal Error: trying to add dice in round = " + actualRound
                );
            }

            // add Dices
            track[actualRound - 1] = diceList;
        }
        nextRound();
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