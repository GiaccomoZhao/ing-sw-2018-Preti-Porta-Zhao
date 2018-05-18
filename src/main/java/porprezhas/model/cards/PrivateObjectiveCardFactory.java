package porprezhas.model.cards;

import java.util.ArrayList;
import java.util.Random;

public class PrivateObjectiveCardFactory implements CardFactory {
    ArrayList<Card> cards;
    ArrayList<Integer> numberList;
//    int numberOfPlayer;
    private final int numberOfCard;

    public static final int PRIVATE_OBJECTIVE_CARD_NUMBER = 5;


    public PrivateObjectiveCardFactory( int numberOfPlayer) {
        numberList = new ArrayList<Integer>();
        for (int i = 0; i < PRIVATE_OBJECTIVE_CARD_NUMBER; i++) {
            numberList.add(i);
        }

        if (numberOfPlayer == 1)
            this.numberOfCard = 2;
        else
            this.numberOfCard = 1;
    }

    @Override
    public ArrayList<Card> createCard() {
        int cardId;
        Random random = new Random();

        cards= new ArrayList<>();

        for(int i=0; i < numberOfCard ; i++){

            cardId = random.nextInt(numberList.size() - 1);

            cards.add(new PrivateObjectiveCard(Card.Effect.values()[cardId+11]));

            numberList.remove(cardId);
        }
        return cards;
    }
}
