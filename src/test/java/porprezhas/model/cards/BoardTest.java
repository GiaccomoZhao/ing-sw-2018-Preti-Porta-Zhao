package porprezhas.model.cards;

import org.junit.Before;
import org.junit.Test;
import porprezhas.model.dices.Board;
import porprezhas.model.dices.Box;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.Pattern;

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
        die = new Dice(Dice.ColorDice.YELLOW,1);
        die1 = new Dice (Dice.ColorDice.BLUE,2);
        die0 = new Dice (Dice.ColorDice.WHITE,0);
        board = new Board(Pattern.TypePattern.KALEIDOSCOPIC_DREAM);
        box = mock(Box.class);




        when(pattern.checkEdges(0,0)).thenReturn(true);
        when(pattern.getBox(0,0)).thenReturn(box);
        when(pattern.getBox(1,1)).thenReturn(box);
        when(pattern.getBox(0,1)).thenReturn(box);
        when(box.checkConstraint(die)).thenReturn(true);

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

        assertEquals(board.getDice(0,0).getColorDice(), die0.getColorDice());
        assertEquals(board.getDice(0,0).getDiceNumber(), die0.getDiceNumber());

        board.insertDice(die, 0,0);
        assertEquals(board.getDice(0,0).getDiceNumber(), die.getDiceNumber());
        assertEquals(board.getDice(0,0).getColorDice(), die.getColorDice());

    }
}