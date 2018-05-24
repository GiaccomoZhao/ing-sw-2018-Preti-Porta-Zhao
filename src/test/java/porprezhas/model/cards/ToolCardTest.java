package porprezhas.model.cards;


import org.junit.Before;
import org.junit.Test;
import porprezhas.model.dices.*;
import porprezhas.model.track.RoundTrack;
import porprezhas.model.dices.Board;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ToolCardTest {

    DiceBag diceBagTest = new DiceBag();
    DraftPool draftPoolTest = new DraftPool(diceBagTest,4);
    Board boardTest = new Board(Pattern.TypePattern.KALEIDOSCOPIC_DREAM);
    RoundTrack roundTrackTest =new RoundTrack();

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


    Dice draftPoolDice1 = new Dice(Dice.ColorDice.BLUE, 1);
    Dice draftPoolDice2 = new Dice(Dice.ColorDice.YELLOW, 2);
    Dice draftPoolDice3 = new Dice(Dice.ColorDice.RED, 3);
    Dice draftPoolDice4 = new Dice(Dice.ColorDice.GREEN, 4);
    Dice draftPoolDice5 = new Dice(Dice.ColorDice.PURPLE, 5);
    Dice draftPoolDice6 = new Dice(Dice.ColorDice.BLUE, 6);
    Dice draftPoolDice7 = new Dice(Dice.ColorDice.YELLOW, 1);
    Dice draftPoolDice8 = new Dice(Dice.ColorDice.RED, 2);
    Dice draftPoolDice9 = new Dice(Dice.ColorDice.BLUE, 3);

    Dice roundTrackDice1 = new Dice(Dice.ColorDice.PURPLE, 4);
    Dice roundTrackDice2 = new Dice(Dice.ColorDice.RED, 2);
    Dice roundTrackDice3 = new Dice(Dice.ColorDice.YELLOW, 3);




    int num = 1;
    int xFirst1;
    int xFirst2;
    int xSecond1;
    int xSecond2;
    int yFirst1;
    int yFirst2;
    int ySecond1;
    int ySecond2;
    boolean operationChoose = true;




    ToolCard toolCard1;
    ToolCard toolCard2;
    ToolCard toolCard3;
    ToolCard toolCard4;
    ToolCard toolCard5;
    ToolCard toolCard6;
    ToolCard toolCard7;
    ToolCard toolCard8;
    ToolCard toolCard9;
    ToolCard toolCard10;
    ToolCard toolCard11;
    ToolCard toolCard12;

    @Before
    public void setUp() {

        boardTest = mock(Board.class);

        when(boardTest.getDice(0,0)).thenReturn(boardDice1);
        when(boardTest.getDice(0,1)).thenReturn(boardDice2);
        when(boardTest.getDice(0,2)).thenReturn(diceNull);
        when(boardTest.getDice(0,3)).thenReturn(diceNull);
        when(boardTest.getDice(0,4)).thenReturn(diceNull);
        when(boardTest.getDice(1,0)).thenReturn(boardDice3);
        when(boardTest.getDice(1,1)).thenReturn(boardDice4);
        when(boardTest.getDice(1,2)).thenReturn(boardDice5);
        when(boardTest.getDice(1,3)).thenReturn(boardDice6);
        when(boardTest.getDice(1,4)).thenReturn(diceNull);
        when(boardTest.getDice(2,0)).thenReturn(boardDice7);
        when(boardTest.getDice(2,1)).thenReturn(diceNull);
        when(boardTest.getDice(2,2)).thenReturn(boardDice8);
        when(boardTest.getDice(2,3)).thenReturn(diceNull);
        when(boardTest.getDice(2,4)).thenReturn(diceNull);
        when(boardTest.getDice(3,0)).thenReturn(boardDice9);
        when(boardTest.getDice(3,1)).thenReturn(diceNull);
        when(boardTest.getDice(3,2)).thenReturn(diceNull);
        when(boardTest.getDice(3,3)).thenReturn(diceNull);
        when(boardTest.getDice(3,4)).thenReturn(diceNull);



        draftPoolTest = mock(DraftPool.class);

        when(draftPoolTest.chooseDice(1)).thenReturn(draftPoolDice1);
        when(draftPoolTest.chooseDice(2)).thenReturn(draftPoolDice2);
        when(draftPoolTest.chooseDice(3)).thenReturn(draftPoolDice3);
        when(draftPoolTest.chooseDice(4)).thenReturn(draftPoolDice4);
        when(draftPoolTest.chooseDice(5)).thenReturn(draftPoolDice5);
        when(draftPoolTest.chooseDice(6)).thenReturn(draftPoolDice6);
        when(draftPoolTest.chooseDice(7)).thenReturn(draftPoolDice7);
        when(draftPoolTest.chooseDice(8)).thenReturn(draftPoolDice8);
        when(draftPoolTest.chooseDice(9)).thenReturn(draftPoolDice9);


        roundTrackTest = mock(RoundTrack.class);

        roundTrackTest.addExternalDice(1,roundTrackDice1);
        roundTrackTest.addExternalDice(2,roundTrackDice2);
        roundTrackTest.addExternalDice(3,roundTrackDice3);

        toolCard1 = new ToolCard(Card.Effect.TC1);
        toolCard2 = new ToolCard(Card.Effect.TC2);
        toolCard3 = new ToolCard(Card.Effect.TC3);
        toolCard4 = new ToolCard(Card.Effect.TC4);
        toolCard5 = new ToolCard(Card.Effect.TC5);
        toolCard6 = new ToolCard(Card.Effect.TC6);
        toolCard7 = new ToolCard(Card.Effect.TC7);
        toolCard8 = new ToolCard(Card.Effect.TC8);
        toolCard9 = new ToolCard(Card.Effect.TC9);
        toolCard10 = new ToolCard(Card.Effect.TC10);
        toolCard11 = new ToolCard(Card.Effect.TC11);
        toolCard12 = new ToolCard(Card.Effect.TC12);
    }

    @Test
    public void useTest1(){

        toolCard1.use(boardTest,draftPoolTest,xFirst1,yFirst1,xSecond1,ySecond1,xFirst2,yFirst2,xSecond2,ySecond2,draftPoolDice1,num,operationChoose,roundTrackTest,diceBagTest);
        assertEquals(draftPoolTest.chooseDice(1).getColorDice(),Dice.ColorDice.BLUE);
        assertEquals(draftPoolTest.chooseDice(1).getDiceNumber(),2);

    }

    @Test
    public void useTest2(){

        toolCard2.use(boardTest,draftPoolTest,3,0,0,2,xFirst2,yFirst2,xSecond2,ySecond2,draftPoolDice1,num,operationChoose,roundTrackTest,diceBagTest);
        assertEquals(boardTest.getDice(3,0).getColorDice(), Dice.ColorDice.WHITE);
        //assertEquals(boardTest.getDice(3,0).getDiceNumber(), 0);

        //assertEquals(boardTest.getDice(0,2).getColorDice(), Dice.ColorDice.PURPLE);
        //assertEquals(boardTest.getDice(0,2).getDiceNumber(), 2);

    }




}
