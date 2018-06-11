package porprezhas.model.dices;

import porprezhas.model.dices.*;
import porprezhas.model.dices.DraftPool;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RoundTrackTest {

    RoundTrack roundTrackTest;
    DiceBag diceBagTest = new DiceBag();
    DraftPool draftPoolTest = new DraftPool(diceBagTest,4);
    ArrayList<Dice> diceListTest;

    Dice draftPoolDice1;
    Dice draftPoolDice2;
    Dice draftPoolDice3;
    Dice draftPoolDice4;
    Dice draftPoolDice5;
    Dice draftPoolDice6;
    Dice draftPoolDice7;
    Dice draftPoolDice8;
    Dice draftPoolDice9;

    Dice externalDice;


    @Before
    public void setUp() {

        Dice diceNull = new Dice(Dice.ColorDice.WHITE, 0);
        Dice boardDice1 = new Dice(Dice.ColorDice.YELLOW,3);
        Dice boardDice2 = new Dice(Dice.ColorDice.BLUE,6);
        Dice boardDice3 = new Dice(Dice.ColorDice.GREEN,2);
        Dice boardDice4 = new Dice(Dice.ColorDice.YELLOW,1);
        Dice boardDice5 = new Dice(Dice.ColorDice.PURPLE,5);
        Dice boardDice6 = new Dice(Dice.ColorDice.GREEN,3);
        Dice boardDice7 = new Dice(Dice.ColorDice.RED,3);
        Dice boardDice8 = new Dice(Dice.ColorDice.RED,4);
        Dice boardDice9 = new Dice(Dice.ColorDice.PURPLE,2);

         draftPoolDice1 = new Dice(Dice.ColorDice.BLUE, 1);
         draftPoolDice2 = new Dice(Dice.ColorDice.YELLOW, 2);
         draftPoolDice3 = new Dice(Dice.ColorDice.RED, 3);
         draftPoolDice4 = new Dice(Dice.ColorDice.GREEN, 4);
         draftPoolDice5 = new Dice(Dice.ColorDice.PURPLE, 5);
         draftPoolDice6 = new Dice(Dice.ColorDice.BLUE, 6);
         draftPoolDice7 = new Dice(Dice.ColorDice.YELLOW, 1);
         draftPoolDice8 = new Dice(Dice.ColorDice.RED, 2);
         draftPoolDice9 = new Dice(Dice.ColorDice.BLUE, 3);

         externalDice = new Dice(Dice.ColorDice.RED,4);

        diceListTest= new ArrayList<>(9);

        diceListTest.add(draftPoolDice1);
        diceListTest.add(draftPoolDice2);
        diceListTest.add(draftPoolDice3);
        diceListTest.add(draftPoolDice4);
        diceListTest.add(draftPoolDice5);
        diceListTest.add(draftPoolDice6);
        diceListTest.add(draftPoolDice7);
        diceListTest.add(draftPoolDice8);
        diceListTest.add(draftPoolDice9);

        draftPoolTest = mock(DraftPool.class);
        when(draftPoolTest.diceList()).thenReturn(diceListTest);
        roundTrackTest = new RoundTrack();

        int actual=4;
        roundTrackTest.actualRound=actual;
    }

    @Test
    public void getActualRoundTest() {
        System.out.println("\n\nBefore getActualRoundTest");
        printTest();

        assertEquals(roundTrackTest.getActualRound(), 4);

        roundTrackTest.addDice(draftPoolTest);

        assertEquals(roundTrackTest.getActualRound(), 5);

        System.out.println("\nAfter the test");
        printTest();
    }

    @Test
    public void removeDiceTest(){
        System.out.println("\n\nBefore removeDiceTest");
        printTest();

        roundTrackTest.addDice(draftPoolTest);
        assertEquals(roundTrackTest.getActualRound(), 5);

        System.out.println("\nIn half");
        printTest();

        roundTrackTest.removeDice(4,draftPoolDice9);
        assertEquals(roundTrackTest.getRoundDice(4).size(),8);

        System.out.println("\nAfter the test");
        printTest();
    }

    @Test
    public void addExternalDice(){
        System.out.println("\n\nBefore addExternalDice");
        printTest();

        roundTrackTest.addDice(draftPoolTest);

        System.out.println("\nIn half");
        printTest();

        roundTrackTest.addDice(3,externalDice);

        System.out.println("\nAfter the test");
        printTest();
    }

    public void printTest() {
        System.out.println( roundTrackTest.toString() );
    }
}
