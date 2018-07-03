package porprezhas.model.cards;

import org.junit.Test;
import porprezhas.model.Game;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ToolCardFactoryTest {

    int numberOfPlayerSingle = 1;
    int numberOfPlayersMulti = 4;

    @Test
    public void createCardSinglePlayerBeginnerTest(){


        ToolCardFactory toolCardFactory  = new ToolCardFactory(Game.SolitaireDifficulty.BEGINNER);

        int previousNumberListSize = toolCardFactory.numberList.size();

        ArrayList<Card> cards = toolCardFactory.createCard();

        assertEquals(cards.size(),5);

        assertEquals( toolCardFactory.numberList.size(), previousNumberListSize-5);


        Card [] choosenCardArray = new Card[5];
        int i=0;

        for (Card card: cards) {
            choosenCardArray[i]=card;
            i++;
        }

        for(i=0;i<5;i++){
            for(int j=1;j<5;j++){
                if(i!=j){
                assertFalse(choosenCardArray[i].equals(choosenCardArray[j]));
                }
            }
        }





    }

    @Test
    public void createCardSinglePlayerEasyTest(){


        ToolCardFactory toolCardFactory  = new ToolCardFactory(Game.SolitaireDifficulty.EASY);

        int previousNumberListSize = toolCardFactory.numberList.size();

        ArrayList<Card> cards = toolCardFactory.createCard();

        assertEquals(cards.size(),4);

        assertEquals( toolCardFactory.numberList.size(), previousNumberListSize-4);


        Card [] choosenCardArray = new Card[4];
        int i=0;

        for (Card card: cards) {
            choosenCardArray[i]=card;
            i++;
        }

        for(i=0;i<4;i++){
            for(int j=1;j<4;j++){
                if(i!=j){
                    assertFalse(choosenCardArray[i].equals(choosenCardArray[j]));
                }
            }
        }
    }

    @Test
    public void createCardSinglePlayerNormalTest(){


        ToolCardFactory toolCardFactory  = new ToolCardFactory(Game.SolitaireDifficulty.NORMAL);

        int previousNumberListSize = toolCardFactory.numberList.size();

        ArrayList<Card> cards = toolCardFactory.createCard();

        assertEquals(cards.size(),3);

        assertEquals( toolCardFactory.numberList.size(), previousNumberListSize-3);


        Card [] choosenCardArray = new Card[3];
        int i=0;

        for (Card card: cards) {
            choosenCardArray[i]=card;
            i++;
        }

        for(i=0;i<3;i++){
            for(int j=1;j<3;j++){
                if(i!=j){
                    assertFalse(choosenCardArray[i].equals(choosenCardArray[j]));
                }
            }
        }
    }

    @Test
    public void createCardSinglePlayerHardTest(){


        ToolCardFactory toolCardFactory  = new ToolCardFactory(Game.SolitaireDifficulty.HARD);

        int previousNumberListSize = toolCardFactory.numberList.size();

        ArrayList<Card> cards = toolCardFactory.createCard();

        assertEquals(cards.size(),2);

        assertEquals( toolCardFactory.numberList.size(), previousNumberListSize-2);


        Card [] choosenCardArray = new Card[2];
        int i=0;

        for (Card card: cards) {
            choosenCardArray[i]=card;
            i++;
        }

        for(i=0;i<2;i++){
            for(int j=1;j<2;j++){
                if(i!=j){
                    assertFalse(choosenCardArray[i].equals(choosenCardArray[j]));
                }
            }
        }
    }

    @Test
    public void createCardSinglePlayerExtremeTest(){


        ToolCardFactory toolCardFactory  = new ToolCardFactory(Game.SolitaireDifficulty.EXTREME);

        int previousNumberListSize = toolCardFactory.numberList.size();

        ArrayList<Card> cards = toolCardFactory.createCard();

        assertEquals(cards.size(),1);

        assertEquals( toolCardFactory.numberList.size(), previousNumberListSize-1);

    }

    @Test
    public void createCardMultiPlayerTest(){


        ToolCardFactory toolCardFactory  = new ToolCardFactory(numberOfPlayersMulti);

        int previousNumberListSize = toolCardFactory.numberList.size();

        ArrayList<Card> cards = toolCardFactory.createCard();

        assertEquals(cards.size(),12);

        assertEquals( toolCardFactory.numberList.size(), previousNumberListSize-12);

        Card [] choosenCardArray = new Card[12];
        int i=0;

        for (Card card: cards) {
            choosenCardArray[i]=card;
            i++;
        }

        for(i=0;i<12;i++){
            for(int j=1;j<12;j++){
                if(i!=j){
                    assertFalse(choosenCardArray[i].equals(choosenCardArray[j]));
                }
            }
        }
    }

}
