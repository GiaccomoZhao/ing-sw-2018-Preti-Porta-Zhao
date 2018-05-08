package porprezhas.model.cards;

import java.util.ArrayList;
import java.util.Random;

public class PrivateObjectiveCardFactory implements CardFactory {
    ArrayList<Card> cards;
    ArrayList<Integer> numberList;
    int numberOfPlayer;

    public PrivateObjectiveCardFactory( int numberOfPlayer) {
        numberList = new ArrayList<Integer>(5);
        for (int i = 0; i < 5; i++) {
            numberList.set(i, i);
        }
        this.numberOfPlayer=numberOfPlayer;
    }

    @Override
    public ArrayList<Card> createCard() {
        int cardId;
        Random random = new Random();

        cards= new ArrayList<>();

        for(int i=0; i < numberOfPlayer ; i++){


            cardId = random.nextInt(numberList.size() - 1);

            cards.add(new PrivateObjectiveCard(Card.Effect.values()[cardId+11]));

            numberList.remove(cardId);
        }
        return cards;
    }
}
