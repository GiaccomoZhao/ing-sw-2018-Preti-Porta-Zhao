package porprezhas.model.cards;

import java.util.ArrayList;
import java.util.Random;

public class PublicObjectiveCardFactory implements CardFactory {

    ArrayList<Card> cards;
    ArrayList<Integer> numberList;
    private final int numberOfCard;

    public static final int PUBLIC_OBJECTIVE_CARD_NUMBER = 10;
    public static final int LIST_LENGHT = 3;

    public PublicObjectiveCardFactory(int numberOfPlayer) {
        numberList = new ArrayList<Integer>();

        for (int i = 0; i < PUBLIC_OBJECTIVE_CARD_NUMBER; i++) {
            numberList.add(i);
        }

        if (numberOfPlayer == 1)
            this.numberOfCard = 2;
        else
            this.numberOfCard = 3;
    }

    @Override
        public ArrayList<Card> createCard( ) {
            int cardId;
            Random random = new Random();

            cards= new ArrayList<>();

            for(int i=0; i <numberOfCard ; i++){


                cardId = random.nextInt(numberList.size() - 1);

                cards.add(new PublicObjectiveCard(Card.Effect.values()[cardId+1]));

                numberList.remove(cardId);
            }
            return cards;
        }
}
