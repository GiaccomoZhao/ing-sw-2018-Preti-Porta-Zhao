package porprezhas.model.cards;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import porprezhas.exceptions.diceMove.AdjacentRestrictionException;
import porprezhas.exceptions.diceMove.PatternColorRestrictionException;
import porprezhas.exceptions.diceMove.PatternNumericRestrictionException;
import porprezhas.exceptions.toolCard.ToolCardParameterException;
import porprezhas.model.dices.Board;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.Pattern;
import porprezhas.view.fx.gameScene.GuiSettings;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ToolCard2StrategyTest {

    private Pattern.TypePattern testPattern;
    private Board testBoard;
    private List<Integer> testIntegerParams;
    private ToolCardParam testParams;


    private ToolCard toolCard2;

    boolean bSuccess;


    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        testPattern = Pattern.TypePattern.SUNS_GLORY;
        testBoard = new Board(testPattern);
        testIntegerParams = new ArrayList<>();
        toolCard2 = new ToolCard(Card.Effect.TC2);

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
        bSuccess = toolCard2.getStrategy().use(testParams);

        assertEquals(true, bSuccess);
    }

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

        // SUCCESS use
        bSuccess = toolCard2.getStrategy().use(testParams);

        assertEquals(true, bSuccess);
    }

    @Test
    public void ignoreColorAdjacentTest() {
        testBoard.insertDice(
                new Dice(Dice.ColorDice.YELLOW, 1, -1),
                0, 0);
        testBoard.insertDice(
                new Dice(Dice.ColorDice.YELLOW, 2, -1),
                1, 1);

        int fromRow = 1;
        int fromCol = 1;
        int toRow = 0;
        int toCol = 1;

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nignoreColorAdjacentTest: ");
        testBoard.print(GuiSettings.bFixedFont);

        // SUCCESS use
        bSuccess = toolCard2.getStrategy().use(testParams);

        assertEquals(true, bSuccess);
    }



    // test that we have Number Constraint

    @Test
    public void ignorePatternNumberTest() {
        testBoard.insertDice(
                new Dice(Dice.ColorDice.PURPLE, 1, -1),
                1, 0);
        testBoard.insertDice(
                new Dice(Dice.ColorDice.YELLOW, 2, -1),
                1, 1);

        int fromRow = 1;
        int fromCol = 1;
        int toRow = 0;
        int toCol = 0;

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nignorePatternNumberTest: ");
        testBoard.print(GuiSettings.bFixedFont);

        exception.expect(PatternNumericRestrictionException.class);
//        exception.expect(ToolCardParameterException.class);
//        exception.expectMessage("Numeric");
        bSuccess = toolCard2.getStrategy().use(testParams);

        assertEquals(false, bSuccess);
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

        exception.expect(AdjacentRestrictionException.class);
//        exception.expect(ToolCardParameterException.class);
//        exception.expectMessage("Numeric");
        bSuccess = toolCard2.getStrategy().use(testParams);

        assertEquals(false, bSuccess);
    }




    // test that we have the Adjacent Constraint

    @Test
    public void farMoveTest() {
        testBoard.insertDice(
                new Dice(Dice.ColorDice.RED, 1, -1),
                0, 0);
        testBoard.insertDice(
                new Dice(Dice.ColorDice.PURPLE, 2, -1),
                0, 1);

        int fromRow = 0;
        int fromCol = 1;
        int toRow = 0;
        int toCol = 2;


        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        testParams = new ToolCardParam(null, null, null, testBoard, testIntegerParams);


        System.out.println("\nfarMoveTest: ");
        testBoard.print(GuiSettings.bFixedFont);

        exception.expect(AdjacentRestrictionException.class);
        bSuccess = toolCard2.getStrategy().use(testParams);

        assertEquals(false, bSuccess);
    }


    @After
    public void tearDown() {
        // Check After -result-
        if(null != testBoard)
            System.out.println(testBoard.toString());

        if(bSuccess)
            System.out.println("\nSUCCESS!!!");
        else
            System.out.println("\nFAILURE!!!");
        System.out.println("_______________________________________________________________________\n\n");
        System.out.flush();

        // check dice quantity in the board doesn't change
        // eight it successes and fails
        assertEquals(2, testBoard.getDiceQuantity());        // in these tests we always test with only 2 dice!!!
    }

}
