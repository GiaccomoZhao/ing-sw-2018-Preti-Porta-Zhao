package porprezhas.model.cards;

import org.junit.Before;
import org.junit.Test;
import porprezhas.model.dices.Dice;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;


public class BoardTest {

    Pattern pattern;
    Dice die, die1, die0;
    Board board;
    Box box;

    @Before
    public void setUp()  {
        pattern = mock(Pattern.class);
        die = mock(Dice.class);
        die1 = mock(Dice.class);
        die0 = mock(Dice.class);
        board = new Board(pattern);
        box = mock(Box.class);


        when(die.getDiceNumber()).thenReturn(1);
        when(die1.getDiceNumber()).thenReturn(2);
        when(die0.getDiceNumber()).thenReturn(0);
        when(die.getColorDice()).thenReturn(Dice.ColorDice.RED);
        when(die1.getColorDice()).thenReturn(Dice.ColorDice.BLUE);
        when(die0.getColorDice()).thenReturn(Dice.ColorDice.WHITE);


        when(pattern.checkEdges(0,0)).thenReturn(true);
        when(pattern.getBox(0,0)).thenReturn(box);
        when(pattern.getBox(1,1)).thenReturn(box);
        when(pattern.getBox(0,1)).thenReturn(box);
        when(box.checkCostraint(die)).thenReturn(true);

    }



    @Test
    public void compatibleDiceTest() {

        assertFalse(board.compatibleDice(die1, die1));

        assertTrue(board.compatibleDice(die, die1));
    }

    @Test
    public void adjacentDiceTest() {
        board.insertDice(die, 0,0);
        assertTrue(board.adjacentDice(die,1,1));
        assertFalse(board.adjacentDice(die,0,1));
        assertTrue(board.adjacentDice(die1, 0, 1));




    }

    @Test
    public void insertDiceTest() {

        assertEquals(board.getDiceQuantity(), 0);
        assertTrue( board.insertDice(die, 0,0));
        assertEquals(board.getDiceQuantity(), 1);
        assertFalse( board.insertDice(die, 0,0));
        assertEquals(board.getDiceQuantity(), 1);
        assertFalse(board.occupiedBox(0,1));
        assertTrue(board.insertDice(die, 1,1));
        assertEquals(board.getDiceQuantity(), 2);


    }



    @Test
    public void getDiceTest() {

        assertEquals(board.getDice(0,0).colorDice, die0.getColorDice());
        assertEquals(board.getDice(0,0).getDiceNumber(), die0.getDiceNumber());

        board.insertDice(die, 0,0);
        assertEquals(board.getDice(0,0).getDiceNumber(), die.getDiceNumber());
        assertEquals(board.getDice(0,0).getColorDice(), die.getColorDice());

    }
}