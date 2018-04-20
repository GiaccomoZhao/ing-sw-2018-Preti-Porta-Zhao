package porprezha.model.cards;

public abstract class ObjectiveCard extends Card {


    private static int numScore;

    int[] numScores={6,5,5,4,2,2,2,5,0,4,0,0,0,0,0};

    public ObjectiveCard(Effect effect){

        super(effect);
        int i=0;
        while(effect.ID<=15){
            ObjectiveCard.numScore=numScores[i];
        }

    }


    public int apply(Board board){return 0;}

}
