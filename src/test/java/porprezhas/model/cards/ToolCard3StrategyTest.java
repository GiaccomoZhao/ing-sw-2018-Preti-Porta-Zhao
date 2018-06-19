package porprezhas.model.cards;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import porprezhas.exceptions.diceMove.AdjacentRestrictionException;
import porprezhas.exceptions.diceMove.PatternColorRestrictionException;
import porprezhas.exceptions.diceMove.PatternNumericRestrictionException;
import porprezhas.model.dices.*;
import porprezhas.view.fx.gameScene.GuiSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static porprezhas.model.dices.CellPosition.MAX_COLUMN;
import static porprezhas.model.dices.CellPosition.MAX_ROW;
import static porprezhas.model.dices.CellPosition.MIN_ROW;

public class ToolCard3StrategyTest {

    private Pattern.TypePattern testPattern;
    private Board testBoard;
    private List<Integer> testIntegerParams;
    private ToolCardParam testParams;


    private ToolCard toolCard3;

    boolean bSuccess;


    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        testPattern = Pattern.TypePattern.SUNS_GLORY;
        testBoard = new Board(testPattern);
        testIntegerParams = new ArrayList<>();
        toolCard3 = new ToolCard(Card.Effect.TC3);

        bSuccess = false;
    }


    // test that it Works

    @Test
    public void justMoveTest() {
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


        System.out.println("\njustMoveTest: ");
        testBoard.print(GuiSettings.bFixedFont);

        // SUCCESS use
        bSuccess =toolCard3.getStrategy().use(testParams);

        assertEquals(true, bSuccess);
    }

    @Test
    public void ignorePatternNumberTest() {
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

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nignorePatternNumberTest: ");
        testBoard.print(GuiSettings.bFixedFont);

        // SUCCESS use
        bSuccess =toolCard3.getStrategy().use(testParams);

        assertEquals(true, bSuccess);
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

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nignoreNumberAdjacentTest: ");
        testBoard.print(GuiSettings.bFixedFont);

        // SUCCESS use
        bSuccess =toolCard3.getStrategy().use(testParams);

        assertEquals(true, bSuccess);
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

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nignorePatternColorTest: ");
        testBoard.print(GuiSettings.bFixedFont);

        exception.expect(PatternColorRestrictionException.class);
        bSuccess =toolCard3.getStrategy().use(testParams);

        assertEquals(false, bSuccess);
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

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nignoreColorAdjacentTest: ");
        testBoard.print(GuiSettings.bFixedFont);

        exception.expect(AdjacentRestrictionException.class);
        bSuccess = toolCard3.getStrategy().use(testParams);

        assertEquals(false, bSuccess);
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
        int toRow = 0;
        int toCol = 2;

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nrootMoveTest: ");
        testBoard.print(GuiSettings.bFixedFont);

//        exception.expect(AdjacentRestrictionException.class);
        bSuccess =toolCard3.getStrategy().use(testParams);

//        assertEquals(false, bSuccess);
        assertEquals(true, bSuccess);
    }

    @Test
    public void rootDeletionMoveTest() {
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

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nrootMoveTest: ");
        testBoard.print(GuiSettings.bFixedFont);

//        exception.expect(AdjacentRestrictionException.class);
        bSuccess =toolCard3.getStrategy().use(testParams);

//        assertEquals(false, bSuccess);
        assertEquals(true, bSuccess);
    }


    @Test
    public void farMoveTest() {
        testBoard.insertDice(
                new Dice(Dice.ColorDice.RED, 1, -1),
                0, 0);
        testBoard.insertDice(
                new Dice(Dice.ColorDice.PURPLE, 2, -1),
                0, 1);

        int fromRow = MIN_ROW;
        int fromCol = 1;
        int toRow = MIN_ROW;
        int toCol = 3;


        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nfarMoveTest: ");
        testBoard.print(GuiSettings.bFixedFont);

        exception.expect(AdjacentRestrictionException.class);
        bSuccess =toolCard3.getStrategy().use(testParams);

        assertEquals(false, bSuccess);
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

        // check dice quantity in the testBoard doesn't change
        // eight it successes and fails
        assertEquals(2, testBoard.getDiceQuantity());        // in these tests we always test with only 2 dice!!!
    }

}
