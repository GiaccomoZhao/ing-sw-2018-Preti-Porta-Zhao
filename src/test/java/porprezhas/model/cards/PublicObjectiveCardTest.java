package porprezhas.model.cards;


import org.junit.Before;
import org.junit.Test;
import porprezhas.model.dices.Board;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.Pattern;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PublicObjectiveCardTest {


    Dice die0, die1, die2, die3, die4, die5, die6, die7, die8, die9, die10;
    Board board;
    PublicObjectiveCard publicObjectiveCard1;
    PublicObjectiveCard publicObjectiveCard2;
    PublicObjectiveCard publicObjectiveCard3;
    PublicObjectiveCard publicObjectiveCard4;
    PublicObjectiveCard publicObjectiveCard5;
    PublicObjectiveCard publicObjectiveCard6;
    PublicObjectiveCard publicObjectiveCard7;
    PublicObjectiveCard publicObjectiveCard8;
    PublicObjectiveCard publicObjectiveCard9;
    PublicObjectiveCard publicObjectiveCard10;
    Card.Effect effect;


    @Before
    public void setUp() {

        die0 = new Dice(Dice.ColorDice.PURPLE,5,0);
        die1 = new Dice(Dice.ColorDice.BLUE,3,1);
        die2 = new Dice(Dice.ColorDice.RED,2,2);
        die3 = new Dice(Dice.ColorDice.GREEN,1,3);
        die4 = new Dice(Dice.ColorDice.YELLOW,4,4);
        die5 = new Dice(Dice.ColorDice.PURPLE,4,5);
        die6 = new Dice(Dice.ColorDice.YELLOW,3,6);
        die7 = new Dice(Dice.ColorDice.RED,5,7);
        die8 = new Dice(Dice.ColorDice.GREEN,6,8);
        die9 = new Dice(Dice.ColorDice.BLUE,5,9);
        die10 = new Dice(Dice.ColorDice.GREEN,1,10);


        board = new Board(Pattern.TypePattern.VOID);


        publicObjectiveCard1 = new PublicObjectiveCard(Card.Effect.PUC1);
        publicObjectiveCard2 = new PublicObjectiveCard(Card.Effect.PUC2);
        publicObjectiveCard3 = new PublicObjectiveCard(Card.Effect.PUC3);
        publicObjectiveCard4 = new PublicObjectiveCard(Card.Effect.PUC4);
        publicObjectiveCard5 = new PublicObjectiveCard(Card.Effect.PUC5);
        publicObjectiveCard6 = new PublicObjectiveCard(Card.Effect.PUC6);
        publicObjectiveCard7 = new PublicObjectiveCard(Card.Effect.PUC7);
        publicObjectiveCard8 = new PublicObjectiveCard(Card.Effect.PUC8);
        publicObjectiveCard9 = new PublicObjectiveCard(Card.Effect.PUC9);
        publicObjectiveCard10 = new PublicObjectiveCard(Card.Effect.PUC10);


        board.insertDice(die0,0,0);
        board.insertDice(die1,0,1);
        board.insertDice(die2,0,2);
        board.insertDice(die3,0,3);
        board.insertDice(die4,0,4);
        board.insertDice(die5,1,1);
        board.insertDice(die6,1,2);
        board.insertDice(die7,1,3);
        board.insertDice(die8,1,4);
        board.insertDice(die9,2,2);
        board.insertDice(die10,3,2);

    }


    @Test
    public void applyCard1Test() {

        assertEquals(publicObjectiveCard1.apply(board),6);

    }

    @Test
    public void applyCard2Test() {

        assertEquals(publicObjectiveCard2.apply(board), 5);

    }

    @Test
    public void applyCard3Test() {

        assertEquals(publicObjectiveCard3.apply(board), 5);

    }

    @Test
    public void applyCard4Test() {

        assertEquals(publicObjectiveCard4.apply(board), 4);

    }

    @Test
    public void applyCard5Test() {

        assertEquals(publicObjectiveCard5.apply(board), 2);

    }

    @Test
    public void applyCard6Test() {

        assertEquals(publicObjectiveCard6.apply(board), 4);

    }

    @Test
    public void applyCard7Test() {

        assertEquals(publicObjectiveCard7.apply(board), 2);

    }

    @Test
    public void applyCard8Test() {

        assertEquals(publicObjectiveCard8.apply(board), 5);

    }

    @Test
    public void applyCard9Test() {

        assertEquals(publicObjectiveCard9.apply(board), 6);

    }

    @Test
    public void applyCard10Test() {

        assertEquals(publicObjectiveCard10.apply(board), 8);


    }
}

