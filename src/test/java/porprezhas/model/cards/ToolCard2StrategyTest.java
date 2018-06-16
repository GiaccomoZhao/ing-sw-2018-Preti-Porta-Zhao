package porprezhas.model.cards;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.Parameterized;
import porprezhas.exceptions.diceMove.ColorRestrictionException;
import porprezhas.exceptions.diceMove.NumberRestrictionException;
import porprezhas.exceptions.toolCard.ToolCardParameterException;
import porprezhas.model.dices.Board;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.Pattern;
import porprezhas.view.fx.gameScene.GuiSettings;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ToolCard2StrategyTest {

    private Pattern.TypePattern testPattern;
    private Board testBoard;
    private List<Integer> testIntegerParams;
    private ToolCardParam testParams;


    private ToolCard toolCard2;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        testPattern = Pattern.TypePattern.SUNS_GLORY;
        testBoard = new Board(testPattern);
        testIntegerParams = new ArrayList<>();
        toolCard2 = new ToolCard(Card.Effect.TC2);
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
        assertTrue(toolCard2.getStrategy().use(testParams));

        testBoard.print(GuiSettings.bFixedFont);
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
        assertTrue(toolCard2.getStrategy().use(testParams));

        testBoard.print(GuiSettings.bFixedFont);
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
        assertTrue(toolCard2.getStrategy().use(testParams));

        testBoard.print(GuiSettings.bFixedFont);
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

        exception.expect(NumberRestrictionException.class);
        assertFalse(toolCard2.getStrategy().use(testParams));

        testBoard.print(GuiSettings.bFixedFont);
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

        exception.expect(NumberRestrictionException.class);
        assertFalse(toolCard2.getStrategy().use(testParams));

        testBoard.print(GuiSettings.bFixedFont);
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

        exception.expect(ToolCardParameterException.class);
        assertFalse(toolCard2.getStrategy().use(testParams));

        testBoard.print(GuiSettings.bFixedFont);
    }


//    Board TypePattern.SUNS_GLORY; // 1, purple, yello, white,
}
