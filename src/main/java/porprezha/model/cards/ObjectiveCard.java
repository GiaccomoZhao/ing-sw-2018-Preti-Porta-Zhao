package porprezha.model.cards;





import java.util.ArrayList;
import java.util.List;

public abstract class ObjectiveCard extends Card {



    public ObjectiveCard(Effect effect) {
        super(effect);
        numScore= numScores[effect.ID];
        this.numScore = numScore;
    }

    private int numScore;

    int[] numScores={6,5,5,4,2,2,2,5,0,4,0,0,0,0,0};



    public int getNumScore() {
        return numScore;
    }

    public abstract int apply(Board board);

}
