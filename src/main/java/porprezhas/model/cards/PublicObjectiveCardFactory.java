package porprezhas.model.cards;

import java.util.ArrayList;
import java.util.Random;

public class PublicObjectiveCardFactory implements CardFactory {

    ArrayList<Card> cards;
    ArrayList<Integer> numberList;
    public static final int PUBLIC_OBJECTIVE_CARD_NUMBER = 10;
    public static final int LIST_LENGHT = 3;

    public PublicObjectiveCardFactory() {
        numberList = new ArrayList<Integer>(PUBLIC_OBJECTIVE_CARD_NUMBER);

        for (int i = 0; i < PUBLIC_OBJECTIVE_CARD_NUMBER; i++) {
            numberList.set(i, i);
        }
    }

    @Override
        public ArrayList<Card> createCard( ) {
            int cardId;
            Random random = new Random();

            cards= new ArrayList<>();

            for(int i=0; i <LIST_LENGHT ; i++){


                cardId = random.nextInt(numberList.size() - 1);

                cards.add(new PublicObjectiveCard(Card.Effect.values()[cardId+1]));

                numberList.remove(cardId);
            }
            return cards;
        }
}
