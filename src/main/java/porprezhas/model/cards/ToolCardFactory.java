package porprezhas.model.cards;

import java.util.ArrayList;
import java.util.Random;


public class ToolCardFactory implements CardFactory {

    ArrayList<Card> cards;
    ArrayList<Integer> numberList;
    public static final int TOOL_CARD_NUMBER = 12;
    public static final int LIST_LENGHT = 3;

    public ToolCardFactory() {


        numberList = new ArrayList<Integer>(TOOL_CARD_NUMBER);
        for(int i=0; i<TOOL_CARD_NUMBER; i++){
            numberList.set(i,i);
        }

    }


    @Override
    public ArrayList<Card> createCard() {
        int cardId;
        Random random = new Random();

        cards= new ArrayList<>();

        for(int i=0; i < LIST_LENGHT; i++){


            cardId = random.nextInt(numberList.size() - 1);

            cards.add(new ToolCard(Card.Effect.values()[cardId+16]));

            numberList.remove(cardId);
        }
        return cards;
    }
}
