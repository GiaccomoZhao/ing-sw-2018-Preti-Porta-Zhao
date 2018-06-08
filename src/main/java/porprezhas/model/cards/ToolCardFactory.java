package porprezhas.model.cards;

import porprezhas.model.Game;

import java.util.ArrayList;
import java.util.Random;

import static porprezhas.model.cards.Card.Effect.*;


public class ToolCardFactory implements CardFactory {

    ArrayList<Card> cards;
    ArrayList<Integer> numberList;
    private final int numberOfCard;


    public ToolCardFactory(Game.SolitaireDifficulty difficulty) {
        numberList = new ArrayList<Integer>();
        for(int i = 0; i<TOOL_CARD_NUMBER; i++){
            numberList.add(i);
        }

        this.numberOfCard = difficulty.toToolCardsQuantity();
    }

    public ToolCardFactory(int numberOfPlayer) {
        numberList = new ArrayList<Integer>();
        for(int i = 0; i<TOOL_CARD_NUMBER; i++){
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


            cardId = random.nextInt(numberList.size());

            Card newCard = new ToolCard( Card.Effect.values() [TC1.ordinal() + cardId] );
            cards.add(newCard);

            numberList.remove(cardId);
        }
        return cards;
    }
}
