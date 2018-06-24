package porprezhas.model.cards;

import java.util.ArrayList;
import java.util.Random;

public class PublicObjectiveCardFactory implements CardFactory {

    ArrayList<Card> cards;
    ArrayList<Integer> numberList;
    private final int numberOfCard;

    /**
     * create the list of Public Objective Cards
     *
     * @param numberOfPlayer used to know if the game mode is singleplayer or multiplayer
     */

    public PublicObjectiveCardFactory(int numberOfPlayer) {
        numberList = new ArrayList<Integer>();

        for (int i = 0; i < Card.Effect.PUBLIC_OBJECTIVE_CARD_NUMBER; i++) {
            numberList.add(i);
        }

        if (numberOfPlayer == 1)
            this.numberOfCard = 2;
        else
            this.numberOfCard = 3;
    }


    /**
     * create the Public Objective Cards for the players
     * @return the list of Public Objective Cards
     */
    @Override
        public ArrayList<Card> createCard( ) {
            int cardId;
            Random random = new Random();

            cards= new ArrayList<>();

            for(int i=0; i <numberOfCard ; i++){


                cardId = random.nextInt(numberList.size());

                cards.add(new PublicObjectiveCard(Card.Effect.values()[Card.Effect.PUC1.ordinal() + cardId]));

                numberList.remove(cardId);
            }
            return cards;
        }
}
