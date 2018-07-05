package porprezhas.model.cards;

import org.junit.Before;
import org.junit.Test;
import porprezhas.model.dices.Board;
import porprezhas.model.dices.Dice;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PrivateObjectiveCardTest {

    Dice die0, die1, die2, die3, die4,die5,die6,die7,die8,dieNull;
    Board board;

    PrivateObjectiveCard privateObjectiveCard1;
    PrivateObjectiveCard privateObjectiveCard2;
    PrivateObjectiveCard privateObjectiveCard3;
    PrivateObjectiveCard privateObjectiveCard4;
    PrivateObjectiveCard privateObjectiveCard5;

    Card.Effect effect;

    @Before
    public void setUp(){

        die0 = mock(Dice.class);
        die1 = mock(Dice.class);
        die2 = mock(Dice.class);
        die3 = mock(Dice.class);
        die4 = mock(Dice.class);
        die5 = mock(Dice.class);
        die6 = mock(Dice.class);
        die7 = mock(Dice.class);
        die8 = mock(Dice.class);
        dieNull = mock(Dice.class);
        board = mock(Board.class);


        privateObjectiveCard1 = new PrivateObjectiveCard(Card.Effect.PRC1);
        privateObjectiveCard2 = new PrivateObjectiveCard(Card.Effect.PRC2);
        privateObjectiveCard3 = new PrivateObjectiveCard(Card.Effect.PRC3);
        privateObjectiveCard4 = new PrivateObjectiveCard(Card.Effect.PRC4);
        privateObjectiveCard5 = new PrivateObjectiveCard(Card.Effect.PRC5);


        when(die0.getDiceNumber()).thenReturn(1);
        when(die1.getDiceNumber()).thenReturn(2);
        when(die2.getDiceNumber()).thenReturn(5);
        when(die3.getDiceNumber()).thenReturn(2);
        when(die4.getDiceNumber()).thenReturn(4);
        when(die5.getDiceNumber()).thenReturn(1);
        when(die6.getDiceNumber()).thenReturn(6);
        when(die7.getDiceNumber()).thenReturn(4);
        when(die8.getDiceNumber()).thenReturn(3);
        when(dieNull.getDiceNumber()).thenReturn(0);

        when(die0.getDiceColor()).thenReturn(Dice.ColorDice.RED);
        when(die1.getDiceColor()).thenReturn(Dice.ColorDice.BLUE);
        when(die2.getDiceColor()).thenReturn(Dice.ColorDice.BLUE);
        when(die3.getDiceColor()).thenReturn(Dice.ColorDice.YELLOW);
        when(die4.getDiceColor()).thenReturn(Dice.ColorDice.YELLOW);
        when(die5.getDiceColor()).thenReturn(Dice.ColorDice.GREEN);
        when(die6.getDiceColor()).thenReturn(Dice.ColorDice.PURPLE);
        when(die7.getDiceColor()).thenReturn(Dice.ColorDice.RED);
        when(die8.getDiceColor()).thenReturn(Dice.ColorDice.GREEN);
        when(dieNull.getDiceColor()).thenReturn(Dice.ColorDice.WHITE);


        when(board.getDice(0,0)).thenReturn(die1);
        when(board.getDice(0,1)).thenReturn(die0);
        when(board.getDice(0,2)).thenReturn(dieNull);
        when(board.getDice(0,3)).thenReturn(dieNull);
        when(board.getDice(0,4)).thenReturn(dieNull);
        when(board.getDice(1,0)).thenReturn(die5);
        when(board.getDice(1,1)).thenReturn(die2);
        when(board.getDice(1,2)).thenReturn(die3);
        when(board.getDice(1,3)).thenReturn(die6);
        when(board.getDice(1,4)).thenReturn(die7);
        when(board.getDice(2,0)).thenReturn(dieNull);
        when(board.getDice(2,1)).thenReturn(die4);
        when(board.getDice(2,2)).thenReturn(dieNull);
        when(board.getDice(2,3)).thenReturn(dieNull);
        when(board.getDice(2,4)).thenReturn(dieNull);
        when(board.getDice(3,0)).thenReturn(dieNull);
        when(board.getDice(3,1)).thenReturn(die8);
        when(board.getDice(3,2)).thenReturn(dieNull);
        when(board.getDice(3,3)).thenReturn(dieNull);
        when(board.getDice(3,4)).thenReturn(dieNull);



    }

    @Test
    public void applyCard1Test() {

        assertEquals(privateObjectiveCard1.apply(board),5);

    }

    @Test
    public void applyCardVoidTest(){

    }

    @Test
    public void applyCard2Test() {

        assertEquals(privateObjectiveCard2.apply(board),6);

    }

    @Test
    public void applyCard3Test() {

        assertEquals(privateObjectiveCard3.apply(board),4);

    }

    @Test
    public void applyCard4Test() {

        assertEquals(privateObjectiveCard4.apply(board),7);

    }

    @Test
    public void applyCard5Test() {

        assertEquals(privateObjectiveCard5.apply(board),6);

    }
}