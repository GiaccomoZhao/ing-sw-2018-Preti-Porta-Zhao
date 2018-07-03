package porprezhas.model.cards;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PublicObjectiveCardFactoryTest {

    int numberOfPlayerSingle = 1;
    int numberOfPlayersMulti = 4;

    @Test
    public void createCardSinglePlayerTest(){


        PublicObjectiveCardFactory publicObjectiveCardFactory  = new PublicObjectiveCardFactory(numberOfPlayerSingle);

        int previousNumberListSize = publicObjectiveCardFactory.numberList.size();

        ArrayList<Card> cards = publicObjectiveCardFactory.createCard();

        assertEquals(cards.size(),2);

        assertEquals( publicObjectiveCardFactory.numberList.size(), previousNumberListSize-2);


        Card [] choosenCardArray = new Card[2];
        int i=0;

        for (Card card: cards) {
            choosenCardArray[i]=card;
            i++;
        }

        assertFalse(choosenCardArray[0].equals(choosenCardArray[1]));




    }

    @Test
    public void createCardMultiPlayerTest(){


        PublicObjectiveCardFactory publicObjectiveCardFactory  = new PublicObjectiveCardFactory(numberOfPlayersMulti);

        int previousNumberListSize = publicObjectiveCardFactory.numberList.size();

        ArrayList<Card> cards = publicObjectiveCardFactory.createCard();

        assertEquals(cards.size(),3);

        assertEquals( publicObjectiveCardFactory.numberList.size(), previousNumberListSize-3);

        Card [] choosenCardArray = new Card[3];
        int i=0;

        for (Card card: cards) {
            choosenCardArray[i]=card;
            i++;
        }

        assertFalse(choosenCardArray[0].equals(choosenCardArray[1]));
        assertFalse(choosenCardArray[1].equals(choosenCardArray[2]));
        assertFalse(choosenCardArray[0].equals(choosenCardArray[2]));






    }
}
