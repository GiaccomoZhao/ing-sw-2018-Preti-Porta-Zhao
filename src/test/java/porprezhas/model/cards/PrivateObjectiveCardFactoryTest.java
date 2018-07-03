package porprezhas.model.cards;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PrivateObjectiveCardFactoryTest {

    int numberOfPlayerSingle = 1;
    int numberOfPlayersMulti = 4;

    @Test
    public void createCardSinglePlayerTest(){


        PrivateObjectiveCardFactory privateObjectiveCardFactory  = new PrivateObjectiveCardFactory(numberOfPlayerSingle);

        int previousNumberListSize = privateObjectiveCardFactory.numberList.size();

        ArrayList<Card> cards = privateObjectiveCardFactory.createCard();

        assertEquals(cards.size(),2);

        assertEquals( privateObjectiveCardFactory.numberList.size(), previousNumberListSize-2);


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


        PrivateObjectiveCardFactory privateObjectiveCardFactory  = new PrivateObjectiveCardFactory(numberOfPlayersMulti);

        int previousNumberListSize = privateObjectiveCardFactory.numberList.size();

        ArrayList<Card> cards = privateObjectiveCardFactory.createCard();

        assertEquals(cards.size(),1);

        assertEquals( privateObjectiveCardFactory.numberList.size(), previousNumberListSize-1);





    }
}
