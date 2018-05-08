package porprezhas.model.cards;

import java.util.ArrayList;
import java.util.Random;


public class ToolCardFactory implements CardFactory {

    ArrayList<Card> cards;
    ArrayList<Integer> numberList;

    public ToolCardFactory() {


        numberList = new ArrayList<Integer>(12);
        for(int i=0; i<12; i++){
            numberList.set(i,i);
        }

    }


    @Override
    public ArrayList<Card> createCard() {
        int cardId;
        Random random = new Random();

        cards= new ArrayList<>();

        for(int i=0; i < 3; i++){


            cardId = random.nextInt(numberList.size() - 1);

            cards.add(new ToolCard(Card.Effect.values()[cardId+16]));

            numberList.remove(cardId);
        }
        return cards;
    }
}
