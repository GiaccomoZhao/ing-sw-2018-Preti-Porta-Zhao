package porprezha.model.cards;

import porprezha.model.cards.Card;

import java.util.LinkedList;
import java.util.List;

public class CardFactory {

    private int number;

    public enum CardTypes{
        PUBLIC_OBJECTIVE_CARD,
        PRIVATE_OBJECTIVE_CARD,
        TOOL_CARD;
    }



    public CardFactory(int number) {
        this.number = 0;

    }

    public List<Card> createCard(String cardType, int lenghtList){

        List<Integer> extractionNumberList;


        if(cardType.equals(CardTypes.PUBLIC_OBJECTIVE_CARD)){

            extractionNumberList = new LinkedList();    // 10

            for (int i=0; i<10;i++){

//                extractionNumberList

            }


        }
        else
        if(cardType.equals(CardTypes.PRIVATE_OBJECTIVE_CARD)){

        }
        else
        if(cardType.equals(CardTypes.TOOL_CARD)){

        }
        return  null;
    }
}
