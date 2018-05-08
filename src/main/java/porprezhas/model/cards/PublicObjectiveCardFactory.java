package porprezhas.model.cards;

import java.util.ArrayList;
import java.util.Random;

public class PublicObjectiveCardFactory implements CardFactory {

    ArrayList<Card> cards;
    ArrayList<Integer> numberList;

    public PublicObjectiveCardFactory() {
        numberList = new ArrayList<Integer>(10);
        for (int i = 0; i < 10; i++) {
            numberList.set(i, i);
        }
    }

    @Override
        public ArrayList<Card> createCard( ) {
            int cardId;
            Random random = new Random();

            cards= new ArrayList<>();

            for(int i=0; i <3 ; i++){


                cardId = random.nextInt(numberList.size() - 1);

                cards.add(new PublicObjectiveCard(Card.Effect.values()[cardId+1]));

                numberList.remove(cardId);
            }
            return cards;
        }
}
