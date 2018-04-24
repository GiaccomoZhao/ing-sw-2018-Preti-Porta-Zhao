package porprezhas.model.cards;



public abstract class ObjectiveCard extends Card {

    private int numScore;

    int[] numScores={6,5,5,4,2,2,2,5,0,4,0,0,0,0,0};


    public ObjectiveCard(Effect effect) {
        super(effect);
        numScore= numScores[effect.ID-1];

    }


    public int getNumScore() {

        return numScore;
    }

    public abstract int apply(Board board);

}
