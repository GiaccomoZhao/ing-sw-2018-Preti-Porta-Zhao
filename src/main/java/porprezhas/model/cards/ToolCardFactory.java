package porprezhas.model.cards;

import porprezhas.model.Game;

import java.util.ArrayList;
import java.util.Random;


public class ToolCardFactory implements CardFactory {

    ArrayList<Card> cards;
    ArrayList<Integer> numberList;
    private final int numberOfCard;

    public static final int TOOL_CARD_NUMBER = 12;
    public static final int LIST_LENGHT = 3;


    public ToolCardFactory(Game.SolitaireDifficulty difficulty) {
        numberList = new ArrayList<Integer>();
        for(int i=0; i<TOOL_CARD_NUMBER; i++){
            numberList.add(i);
        }

        this.numberOfCard = difficulty.toToolCardsQuantity();
    }

    public ToolCardFactory(int numberOfPlayer) {
        numberList = new ArrayList<Integer>();
        for(int i=0; i<TOOL_CARD_NUMBER; i++){
            numberList.add(i);
        }

        this.numberOfCard = 3;
    }

    @Override
    public ArrayList<Card> createCard() {
        int cardId;
        Random random = new Random();

        cards= new ArrayList<>();

        for(int i=0; i < numberOfCard; i++){


            cardId = random.nextInt(numberList.size() - 1);

            cards.add(new ToolCard(Card.Effect.values()[cardId+16]));

            numberList.remove(cardId);
        }
        return cards;
    }
}
