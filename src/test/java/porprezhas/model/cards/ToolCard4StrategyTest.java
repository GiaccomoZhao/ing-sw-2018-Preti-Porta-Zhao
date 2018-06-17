package porprezhas.model.cards;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import porprezhas.exceptions.diceMove.AdjacentRestrictionException;
import porprezhas.exceptions.diceMove.BoardCellOccupiedException;
import porprezhas.exceptions.diceMove.PatternColorRestrictionException;
import porprezhas.exceptions.diceMove.PatternNumericRestrictionException;
import porprezhas.exceptions.toolCard.IncorrectParamQuantityException;
import porprezhas.model.dices.*;
import porprezhas.view.fx.gameScene.GuiSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static porprezhas.model.dices.CellPosition.*;

public class ToolCard4StrategyTest {

    private Pattern.TypePattern testPattern;
    private Board testBoard;
    private List<Integer> testIntegerParams;
    private ToolCardParam testParams;


    private ToolCard toolCard4;

    boolean bSuccess;


    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        testPattern = Pattern.TypePattern.SUNS_GLORY;
        testBoard = new Board(testPattern);
        testIntegerParams = new ArrayList<>();
        toolCard4 = new ToolCard(Card.Effect.TC4);

        bSuccess = false;
    }


    @Test
    public void lessParameterCheck() {
        testBoard.insertDice(
                new Dice(Dice.ColorDice.RED, 1, -1),
                0, 0);
        testBoard.insertDice(
                new Dice(Dice.ColorDice.PURPLE, 2, -1),
                0, 1);

        int fromRow = 0;
        int fromCol = 1;
        int toRow = 1;
        int toCol = 0;

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nlessParameterCheck: ");
        testBoard.print(GuiSettings.bFixedFont);

        exception.expect(IncorrectParamQuantityException.class);
        bSuccess = toolCard4.getStrategy().use(testParams);

        assertEquals(false, bSuccess);
    }
    @Test
    public void moreParameterCheck() {
        testBoard.insertDice(
                new Dice(Dice.ColorDice.YELLOW, 1, -1),
                0, 0);
        testBoard.insertDice(
                new Dice(Dice.ColorDice.PURPLE, 2, -1),
                0, 1);

        int fromRow = 0;
        int fromCol = 0;
        int toRow = 1;
        int toCol = 1;
        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);


        int fromRow2 = 0;
        int fromCol2 = 1;
        int toRow2 = 1;
        int toCol2 = 0;
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testIntegerParams.add(toCol2);      // with one of TOO more

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nmoreParameterCheck: ");
        testBoard.print(GuiSettings.bFixedFont);

        exception.expect(IncorrectParamQuantityException.class);
        bSuccess = toolCard4.getStrategy().use(testParams);

        assertEquals(false, bSuccess);
    }

    @Test
    public void justMoveTest() {
        testBoard.insertDice(
                new Dice(Dice.ColorDice.YELLOW, 1, -1),
                0, 0);
        testBoard.insertDice(
                new Dice(Dice.ColorDice.PURPLE, 2, -1),
                0, 1);

        int fromRow = 0;
        int fromCol = 0;
        int toRow = 1;
        int toCol = 1;
        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);


        int fromRow2 = 0;
        int fromCol2 = 1;
        int toRow2 = 1;
        int toCol2 = 0;
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\njustMoveTest: \t");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        // SUCCESS use
        bSuccess = toolCard4.getStrategy().use(testParams);

        assertEquals(true, bSuccess);
    }


    // check it has ALL constraints
    @Test
    public void ignorePatternNumberCorrectTest() {
        testBoard.insertDice(
                new Dice(Dice.ColorDice.PURPLE, 4, -1),
                bottomLeftPosition.getRow(), bottomLeftPosition.getCol());
        testBoard.insertDice(
                new Dice(Dice.ColorDice.YELLOW, 5, -1),
                bottomLeftPosition.getRow() -1, bottomLeftPosition.getCol() +1);

        int fromRow = bottomLeftPosition.getRow();
        int fromCol = bottomLeftPosition.getCol();
        int toRow = bottomLeftPosition.getRow();
        int toCol = bottomLeftPosition.getCol() +2;

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        int fromRow2 = bottomLeftPosition.getRow() -1;
        int fromCol2 = bottomLeftPosition.getCol() +1;
        int toRow2 = bottomLeftPosition.getRow() -1;
        int toCol2 = bottomLeftPosition.getCol() +3;
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nignorePatternNumberCorrectTest: ");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        bSuccess = toolCard4.getStrategy().use(testParams);

        assertEquals(true, bSuccess);
    }

    @Test
    public void ignorePatternNumberFailureTest() {
        testBoard.insertDice(
                new Dice(Dice.ColorDice.PURPLE, 1, -1),
                MAX_ROW, MAX_COLUMN);
        testBoard.insertDice(
                new Dice(Dice.ColorDice.YELLOW, 2, -1),
                MAX_ROW, MAX_COLUMN -1);

        int fromRow = MAX_ROW;
        int fromCol = MAX_COLUMN;
        int toRow = MAX_ROW -1;
        int toCol = MAX_COLUMN;

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        int fromRow2 = MAX_ROW;
        int fromCol2 = MAX_COLUMN -1;
        int toRow2 = MAX_ROW;
        int toCol2 = MAX_COLUMN ;
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nignorePatternNumberTest: \t");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        exception.expect(PatternNumericRestrictionException.class);
        //bSuccess =
                toolCard4.getStrategy().use(testParams);

//        assertEquals(false, bSuccess);
    }

    @Test
    public void ignoreNumberAdjacentTest() {
        testBoard.insertDice(
                new Dice(Dice.ColorDice.PURPLE, 2, -1),
                0, 1);
        testBoard.insertDice(
                new Dice(Dice.ColorDice.YELLOW, 2, -1),
                1, 2);

        int fromRow = 1;
        int fromCol = 2;
        int toRow = 1;
        int toCol = 1;

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        int fromRow2 = 1;
        int fromCol2 = 2;
        int toRow2 = 0;
        int toCol2 = 1;
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nignoreNumberAdjacentTest: ");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        exception.expect(AdjacentRestrictionException.class);
        bSuccess = toolCard4.getStrategy().use(testParams);

        assertEquals(true, bSuccess);
        assertEquals(true, ((ToolCard2) toolCard4.getStrategy()).getReturn());
        assertEquals(2, testBoard.getDiceQuantity());
    }



    // test that we have COLOR Constraint

    @Test
    public void ignorePatternColorTest() {
        testBoard.insertDice(
                new Dice(Dice.ColorDice.RED, 1, -1),
                0, 0);
        testBoard.insertDice(
                new Dice(Dice.ColorDice.PURPLE, 2, -1),
                0, 1);

        int fromRow = 0;
        int fromCol = 0;
        int toRow = 1;
        int toCol = 0;

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        int fromRow2 = 123;     // we don't care about these value
        int fromCol2 = 123;     // because it fails at first move
        int toRow2 = 1234;
        int toCol2 = 1234;
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.print("\nignorePatternColorTest: \t");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        exception.expect(PatternColorRestrictionException.class);
        bSuccess = toolCard4.getStrategy().use(testParams);

        assertEquals(false, bSuccess);
        assertEquals(false, ((ToolCard2) toolCard4.getStrategy()).getReturn());
        assertEquals(2, testBoard.getDiceQuantity());
    }

    @Test
    public void ignoreColorAdjacentTest() {
        testBoard.insertDice(
                new Dice(Dice.ColorDice.YELLOW, 1, -1),
                0, 3);
        testBoard.insertDice(
                new Dice(Dice.ColorDice.YELLOW, 2, -1),
                1, 2);

        int fromRow = 1;
        int fromCol = 2;
        int toRow = 0;
        int toCol = 2;

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        int fromRow2 = 123;     // we don't care about these value
        int fromCol2 = 123;     // because it fails at first move
        int toRow2 = 1234;
        int toCol2 = 1234;
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nignoreColorAdjacentTest: \t");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        exception.expect(AdjacentRestrictionException.class);
        bSuccess = toolCard4.getStrategy().use(testParams);

        assertEquals(false, bSuccess);
        assertEquals(false, ((ToolCard2) toolCard4.getStrategy()).getReturn());
        assertEquals(2, testBoard.getDiceQuantity());
    }



    // test that we have the Adjacent Constraint
    @Test
    public void rootMoveTest() {
        testBoard.insertDice(
                new Dice(Dice.ColorDice.YELLOW, 1, -1),
                0, 0);
        testBoard.insertDice(
                new Dice(Dice.ColorDice.YELLOW, 2, -1),
                1, 1);

        int fromRow = 0;
        int fromCol = 0;
        int toRow = 2;
        int toCol = 2;

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        int fromRow2 = 1;
        int fromCol2 = 1;
        int toRow2 = 3;
        int toCol2 = 3;
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nrootMoveTest: \t");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        bSuccess = toolCard4.getStrategy().use(testParams);

        assertEquals(true, bSuccess);
        assertEquals(true, ((ToolCard2) toolCard4.getStrategy()).getReturn());
        assertEquals(2, testBoard.getDiceQuantity());
    }

    @Test
    public void rootMoveWithInvertedOrderTest() {
        testBoard.insertDice(
                new Dice(Dice.ColorDice.YELLOW, 1, -1),
                0, 0);
        testBoard.insertDice(
                new Dice(Dice.ColorDice.YELLOW, 2, -1),
                1, 1);

        int fromRow = 1;
        int fromCol = 1;
        int toRow = 3;
        int toCol = 3;

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        int fromRow2 = 0;
        int fromCol2 = 0;
        int toRow2 = 2;
        int toCol2 = 2;
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nrootMoveWithInvertedOrderTest: \t");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        exception.expect(AdjacentRestrictionException.class);
        bSuccess = toolCard4.getStrategy().use(testParams);

        assertEquals(false, bSuccess);
        assertEquals(false, ((ToolCard2) toolCard4.getStrategy()).getReturn());
        assertEquals(2, testBoard.getDiceQuantity());
    }


    @Test
    public void exchangeTest() {
        testBoard.insertDice(
                new Dice(Dice.ColorDice.PURPLE, 4, -1),
                bottomLeftPosition.getRow(), bottomLeftPosition.getCol());
        testBoard.insertDice(
                new Dice(Dice.ColorDice.YELLOW, 5, -1),
                bottomLeftPosition.getRow() -1, bottomLeftPosition.getCol() +1);

        int fromRow = bottomLeftPosition.getRow();
        int fromCol = bottomLeftPosition.getCol();
        int toRow = bottomLeftPosition.getRow() -1;
        int toCol = bottomLeftPosition.getCol() +1;

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        int fromRow2 = bottomLeftPosition.getRow() -1;
        int fromCol2 = bottomLeftPosition.getCol() +1;
        int toRow2 = bottomLeftPosition.getRow();
        int toCol2 = bottomLeftPosition.getCol();
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nexchangeTest: ");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        exception.expect(BoardCellOccupiedException.class);
        bSuccess = toolCard4.getStrategy().use(testParams);

        assertEquals(false, bSuccess);
        assertEquals(false, ((ToolCard2) toolCard4.getStrategy()).getReturn());
        assertEquals(2, testBoard.getDiceQuantity());
    }


    @Test
    public void doubleMoveTest() {
        testBoard.insertDice(
                new Dice(Dice.ColorDice.PURPLE, 4, -1),
                bottomLeftPosition.getRow(), bottomLeftPosition.getCol());
        testBoard.insertDice(
                new Dice(Dice.ColorDice.YELLOW, 5, -1),
                bottomLeftPosition.getRow() -1, bottomLeftPosition.getCol() +1);

        int fromRow = bottomLeftPosition.getRow();
        int fromCol = bottomLeftPosition.getCol();
        int toRow = bottomLeftPosition.getRow() -2;
        int toCol = bottomLeftPosition.getCol() +2;

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        int fromRow2 = bottomLeftPosition.getRow()-2;
        int fromCol2 = bottomLeftPosition.getCol()+2;
        int toRow2 = bottomLeftPosition.getRow() -1;
        int toCol2 = bottomLeftPosition.getCol() +2;
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\ndoubleMoveTest: ");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        bSuccess = toolCard4.getStrategy().use(testParams);

        assertEquals(true, bSuccess);
        assertEquals(true, ((ToolCard2) toolCard4.getStrategy()).getReturn());
        assertEquals(2, testBoard.getDiceQuantity());
    }

    @Test
    public void oneDiceDoubleRootMoveTest() {
        testBoard.insertDice(
                new Dice(Dice.ColorDice.PURPLE, 4, -1),
                topRightPosition.getRow(), topRightPosition.getCol());

        int fromRow = topRightPosition.getRow();
        int fromCol = topRightPosition.getCol();
        int toRow = MAX_ROW;
        int toCol = MAX_COLUMN/2;

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        int fromRow2 = MAX_ROW;
        int fromCol2 = MAX_COLUMN/2;
        int toRow2 = 0;
        int toCol2 = 1;
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\noneDiceDoubleRootMoveTest: ");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        bSuccess = toolCard4.getStrategy().use(testParams);

        assertEquals(true, bSuccess);
        assertEquals(true, ((ToolCard2) toolCard4.getStrategy()).getReturn());
        assertEquals(1, testBoard.getDiceQuantity());
    }



    @After
    public void tearDown() {
        // Check After -result-
        if(null != testBoard)
            testBoard.print(GuiSettings.bFixedFont);

        if(bSuccess)
            System.out.println("\nSUCCESS!!!");
        else
            System.out.println("\nFAILURE!!!");
        System.out.println("_______________________________________________________________________\n\n");
        System.out.flush();
    }

}
