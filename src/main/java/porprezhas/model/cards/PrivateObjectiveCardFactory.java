package porprezhas.model.cards;

import java.util.ArrayList;
import java.util.Random;

public class PrivateObjectiveCardFactory implements CardFactory {
    ArrayList<Card> cards;
    ArrayList<Integer> numberList;
//    int numberOfPlayer;
    private final int numberOfCard;


    /**
     * create the list of Private Objective Cards
     * @param numberOfPlayer used to define the cases of singleplayer or multiplayer
     */

    public PrivateObjectiveCardFactory( int numberOfPlayer) {
        numberList = new ArrayList<Integer>();
        for (int i = 0; i < Card.Effect.PRIVATE_OBJECTIVE_CARD_NUMBER; i++) {
            numberList.add(i);
        }

        if (numberOfPlayer == 1)
            this.numberOfCard = 2;
        else
            this.numberOfCard = 1;
    }

    /**
     * create the Private Objective Cards for the players
     * @return the list of cards
     */

    @Override
    public ArrayList<Card> createCard() {
        int cardId;
        Random random = new Random();

        cards= new ArrayList<>();

        for(int i=0; i < numberOfCard ; i++){

            cardId = random.nextInt(numberList.size()); // from 0 to PRIVATE_OBJECTIVE_CARD_NUMBER -1

            cards.add(new PrivateObjectiveCard( Card.Effect.values() [Card.Effect.PRC1.ordinal() + cardId] ));

            numberList.remove(cardId);
        }
        return cards;
    }
}
