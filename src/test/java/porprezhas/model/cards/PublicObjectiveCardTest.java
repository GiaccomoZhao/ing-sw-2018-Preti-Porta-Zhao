package porprezhas.model.cards;


import org.junit.Before;
import org.junit.Test;
import porprezhas.model.dices.Board;
import porprezhas.model.dices.Dice;



import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PublicObjectiveCardTest {


    Dice die0, die1, die2, die3, die4,die5,die6,die7,die8,dieNull;
    Board board;
    PublicObjectiveCard publicObjectiveCard1;
    PublicObjectiveCard publicObjectiveCard8;
    Card.Effect effect;





    @Before
    public void setUp()  {

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





        publicObjectiveCard1 = new PublicObjectiveCard(Card.Effect.PUC5);
        publicObjectiveCard8 = new PublicObjectiveCard(Card.Effect.PUC8);


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

        when(die0.getColorDice()).thenReturn(Dice.ColorDice.RED);
        when(die1.getColorDice()).thenReturn(Dice.ColorDice.BLUE);
        when(die2.getColorDice()).thenReturn(Dice.ColorDice.BLUE);
        when(die3.getColorDice()).thenReturn(Dice.ColorDice.YELLOW);
        when(die4.getColorDice()).thenReturn(Dice.ColorDice.YELLOW);
        when(die5.getColorDice()).thenReturn(Dice.ColorDice.GREEN);
        when(die6.getColorDice()).thenReturn(Dice.ColorDice.PURPLE);
        when(die7.getColorDice()).thenReturn(Dice.ColorDice.RED);
        when(die8.getColorDice()).thenReturn(Dice.ColorDice.GREEN);
        when(die8.getColorDice()).thenReturn(Dice.ColorDice.WHITE);


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
    public void applyTest(){

        assertEquals(publicObjectiveCard1.apply(board),4);

        assertEquals(publicObjectiveCard8.apply(board),5);


    }


}

