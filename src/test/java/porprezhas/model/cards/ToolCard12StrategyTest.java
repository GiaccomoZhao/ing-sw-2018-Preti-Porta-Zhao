package porprezhas.model.cards;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import porprezhas.exceptions.GameAbnormalException;
import porprezhas.exceptions.diceMove.AdjacentRestrictionException;
import porprezhas.exceptions.diceMove.PatternColorRestrictionException;
import porprezhas.exceptions.diceMove.PatternNumericRestrictionException;
import porprezhas.exceptions.toolCard.CorlorNotFoundInRoundTrackException;
import porprezhas.exceptions.toolCard.DiceNotFoundInBoardException;
import porprezhas.exceptions.toolCard.DifferentColorException;
import porprezhas.exceptions.toolCard.IncorrectParamQuantityException;
import porprezhas.model.dices.*;
import porprezhas.view.fx.gameScene.GuiSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

public class ToolCard12StrategyTest {
    private Pattern.TypePattern testPattern;

    private ToolCard toolCard12;
    private ToolCardParam testParams;
    private List<Integer> testIntegerParams;


    private Random random;  // used to choose an in range number arbitrarily

    // used dice container
    private RoundTrack testRoundTrack;
    private Board testBoard;

    // not used container
    private DraftPool testDraftPool;
    private DiceBag testDiceBag;


    boolean bSuccess;

    @Rule
    public final ExpectedException exception = ExpectedException.none();


    @Before
    public void setUp() {
        random = new Random();
        testDraftPool = new DraftPool();
        testDiceBag = new DiceBag();

        testPattern = Pattern.TypePattern.SUNS_GLORY;
        testBoard = new Board(testPattern);
        testRoundTrack = new RoundTrack();
        testIntegerParams = new ArrayList<>();

        toolCard12= new ToolCard(Card.Effect.TC12);
        testParams = new ToolCardParam(testRoundTrack, testDraftPool, testDiceBag, testBoard, testIntegerParams);
    }

    @Test
    public void nullTest() {
        Class<IncorrectParamQuantityException> excepted = IncorrectParamQuantityException.class;
        int iExcepted = 0;

        // test toolCard is constructed
        assertNotNull( toolCard12.getStrategy() );

        // test Null pointer on parameters
        try {
            toolCard12.getStrategy().use(null);
        } catch (IncorrectParamQuantityException e) {
            iExcepted++;
        }
        if(iExcepted < 1)
            fail();

        // test with Empty parameters
        try {
            toolCard12.getStrategy().use(testParams);
        } catch (IncorrectParamQuantityException e) {
            iExcepted++;
        }
        if(iExcepted < 2)
            fail();

        // test with all Null
        testParams = new ToolCardParam(null, null, null, null, null);
        try {
            toolCard12.getStrategy().use(testParams);
        } catch (IncorrectParamQuantityException e) {
            iExcepted++;
        }
        if(iExcepted < 3)
            fail();

        // test with Only Dice Container, but Null Integer Params
        testParams = new ToolCardParam(testRoundTrack, null, null, testBoard, null);
        try {
            toolCard12.getStrategy().use(testParams);
        } catch (IncorrectParamQuantityException e) {
            iExcepted++;
        }
        if(iExcepted < 4)
            fail();

        // test all empty
        testParams = new ToolCardParam(testRoundTrack, testDraftPool, testDiceBag, testBoard, testIntegerParams);
        try {
            toolCard12.getStrategy().use(testParams);
        } catch (IncorrectParamQuantityException e) {
            iExcepted++;
        }
        if(iExcepted < 5)
            fail();
    }


    @Test
    public void emptyTest() {
        Class<IncorrectParamQuantityException> excepted = IncorrectParamQuantityException.class;
        int iExcepted = 0;
        // initialize empty containers and params
        RoundTrack emptyRoundTrack = new RoundTrack();
        DraftPool emptyDraftPool = new DraftPool();
        DiceBag emptyDiceBag = new DiceBag();
        Board emptyBoard = new Board(Pattern.TypePattern.VOID);
        List<Integer> emptyParams = new ArrayList<>();

        // test with All Empty
        testParams = new ToolCardParam(emptyRoundTrack, emptyDraftPool, emptyDiceBag, emptyBoard, emptyParams);
        try {
            toolCard12.getStrategy().use(testParams);
        } catch (IncorrectParamQuantityException e) {
            iExcepted++;
        }
        if(iExcepted < 1)
            fail();

        // test with Null Dice Container and EMPTY Integer Params
        testParams = new ToolCardParam(null, null, null, null, emptyParams);
        try {
            toolCard12.getStrategy().use(testParams);
        } catch (IncorrectParamQuantityException e) {
            iExcepted++;
        }
        if(iExcepted < 2)
            fail();

        // test with Correct Dice Container and EMPTY Integer Params
        testParams = new ToolCardParam(emptyRoundTrack, testDraftPool, testDiceBag, emptyBoard, emptyParams);
        try {
            toolCard12.getStrategy().use(testParams);
        } catch (IncorrectParamQuantityException e) {
            iExcepted++;
        }
        if(iExcepted < 3)
            fail();



        // test with NULL Dice Container and Correct Integer Params
        testIntegerParams.add(0);
        testIntegerParams.add(0);
        testIntegerParams.add(0);
        testIntegerParams.add(0);
        testIntegerParams.add(0);
        testIntegerParams.add(0);
        testIntegerParams.add(0);
        testIntegerParams.add(0);
        testParams = new ToolCardParam(null, null, null, null, testIntegerParams);
        try {
            toolCard12.getStrategy().use(testParams);
        } catch (GameAbnormalException e) {
            iExcepted++;
        }
        if(iExcepted < 4)
            fail();

        // test with EMPTY Dice Container and Correct Integer Params
        // throw Dice not found in Empty dice container
        testParams = new ToolCardParam(emptyRoundTrack, emptyDraftPool, emptyDiceBag, emptyBoard, testIntegerParams);
        try {
            toolCard12.getStrategy().use(testParams);
        } catch (DiceNotFoundInBoardException e) {
            iExcepted++;
        }
        if(iExcepted < 5)
            fail();

        // test with EMPTY Dice Container and Correct Integer Params, and Null unused container
        testParams = new ToolCardParam(emptyRoundTrack, null, null, emptyBoard, testIntegerParams);
        try {
            toolCard12.getStrategy().use(testParams);
        } catch (DiceNotFoundInBoardException e) {
            iExcepted++;
        }
        if(iExcepted < 6)
            fail();
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
        bSuccess = toolCard12.getStrategy().use(testParams);

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
        bSuccess = toolCard12.getStrategy().use(testParams);

        assertEquals(false, bSuccess);
    }

    @Test(expected = CorlorNotFoundInRoundTrackException.class)
    public void moveWithNoDiceInTrackTest() {
        int fromRow = 0;
        int fromCol = 0;
        int toRow = 2;
        int toCol = 1;

        int fromRow2 = 1;
        int fromCol2 = 1;
        int toRow2 = 1;
        int toCol2 = 0;

        testBoard.insertDice(
                new Dice(Dice.ColorDice.YELLOW, 1, -1),
                fromRow, fromCol);
        testBoard.insertDice(
                new Dice(Dice.ColorDice.YELLOW, 2, -1),
                fromRow2, fromCol2);

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(testRoundTrack, null, null, testBoard, testIntegerParams);


        System.out.println("\nmoveWithNoDiceInTrackTest: \t");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        // SUCCESS use
        bSuccess = toolCard12.getStrategy().use(testParams);

        assertEquals(true, bSuccess);
    }

    @Test
    public void correctMoveTest() {
        int fromRow = 0;
        int fromCol = 0;
        int toRow = 2;
        int toCol = 0;

        int fromRow2 = 1;
        int fromCol2 = 1;
        int toRow2 = 3;
        int toCol2 = 1;

        Dice dice1 = new Dice(Dice.ColorDice.YELLOW, 1, -1);
        Dice dice2 = new Dice(Dice.ColorDice.YELLOW, 5, -1);

        testBoard.insertDice(
                dice1,
                fromRow, fromCol);
        testBoard.insertDice(
                dice2,
                fromRow2, fromCol2);
        testDraftPool = new DraftPool(
                Arrays.asList(
                        new Dice(1, Dice.ColorDice.YELLOW, -1)
                )
        );
        testRoundTrack.addDice(testDraftPool);

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(testRoundTrack, null, null, testBoard, testIntegerParams);


        System.out.println("\ncorrectMoveTest: \t");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        // SUCCESS use
        bSuccess = toolCard12.getStrategy().use(testParams);

        testBoard.print(GuiSettings.bFixedFont);

        assertEquals(true, bSuccess);
        assertEquals(2, testBoard.getDiceQuantity());
        assertEquals(dice1, testBoard.getDice(toRow, toCol));
        assertEquals(dice2, testBoard.getDice(toRow2, toCol2));
    }



    @Test (expected = PatternNumericRestrictionException.class)
    public void wrongPatternNumberMoveTest() {
        int fromRow = 0;
        int fromCol = 0;
        int toRow = 2;
        int toCol = 0;

        int fromRow2 = 1;
        int fromCol2 = 1;
        int toRow2 = 3;
        int toCol2 = 1;

        Dice dice1 = new Dice(Dice.ColorDice.YELLOW, 1, -1);
        Dice dice2 = new Dice(Dice.ColorDice.YELLOW, 2, -1);

        testBoard.insertDice(
                dice1,
                fromRow, fromCol);
        testBoard.insertDice(
                dice2,
                fromRow2, fromCol2);
        testDraftPool = new DraftPool(
                Arrays.asList(
                        new Dice(1, Dice.ColorDice.YELLOW, -1)
                )
        );
        testRoundTrack.addDice(testDraftPool);

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(testRoundTrack, null, null, testBoard, testIntegerParams);


        System.out.println("\nwrongPatternNumberMoveTest: \t");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        // SUCCESS use
        bSuccess = toolCard12.getStrategy().use(testParams);

        testBoard.print(GuiSettings.bFixedFont);

        assertEquals(true, bSuccess);
        assertEquals(2, testBoard.getDiceQuantity());
        assertEquals(dice1, testBoard.getDice(toRow, toCol));
        assertEquals(dice2, testBoard.getDice(toRow2, toCol2));
    }

    @Test (expected = AdjacentRestrictionException.class)
    public void notNumericAdjacentMoveTest() {
        int fromRow = 0;
        int fromCol = 0;
        int toRow = 2;
        int toCol = 0;

        int fromRow2 = 1;
        int fromCol2 = 1;
        int toRow2 = 3;
        int toCol2 = 0;

        Dice dice1 = new Dice(Dice.ColorDice.YELLOW, 1, -1);
        Dice dice2 = new Dice(Dice.ColorDice.YELLOW, 1, -1);

        testBoard.insertDice(
                dice1,
                fromRow, fromCol);
        testBoard.insertDice(
                dice2,
                fromRow2, fromCol2);
        testDraftPool = new DraftPool(
                Arrays.asList(
                        new Dice(1, Dice.ColorDice.YELLOW, -1)
                )
        );
        testRoundTrack.addDice(testDraftPool);

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(testRoundTrack, null, null, testBoard, testIntegerParams);


        System.out.println("\nnotNumericAdjacentMoveTest: \t");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        // SUCCESS use
        bSuccess = toolCard12.getStrategy().use(testParams);

        testBoard.print(GuiSettings.bFixedFont);

        assertEquals(true, bSuccess);
        assertEquals(2, testBoard.getDiceQuantity());
        assertEquals(dice1, testBoard.getDice(toRow, toCol));
        assertEquals(dice2, testBoard.getDice(toRow2, toCol2));
    }


    @Test (expected = PatternColorRestrictionException.class)
    public void wrongPatternColorTest() {
        int fromRow = 0;
        int fromCol = 0;
        int toRow = 1;
        int toCol = 0;

        int fromRow2 = 1;
        int fromCol2 = 1;
        int toRow2 = 2;
        int toCol2 = 1;

        Dice dice1 = new Dice(Dice.ColorDice.YELLOW, 1, -1);
        Dice dice2 = new Dice(Dice.ColorDice.YELLOW, 5, -1);

        testBoard.insertDice(
                dice1,
                fromRow, fromCol);
        testBoard.insertDice(
                dice2,
                fromRow2, fromCol2);
        testDraftPool = new DraftPool(
                Arrays.asList(
                        new Dice(1, Dice.ColorDice.YELLOW, -1)
                )
        );
        testRoundTrack.addDice(testDraftPool);

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(testRoundTrack, null, null, testBoard, testIntegerParams);


        System.out.println("\nwrongPatternColorTest: \t");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        // SUCCESS use
        bSuccess = toolCard12.getStrategy().use(testParams);

        testBoard.print(GuiSettings.bFixedFont);

        assertEquals(true, bSuccess);
        assertEquals(2, testBoard.getDiceQuantity());
        assertEquals(dice1, testBoard.getDice(toRow, toCol));
        assertEquals(dice2, testBoard.getDice(toRow2, toCol2));
    }

    @Test (expected = AdjacentRestrictionException.class)
    public void notColorAdjacentMoveTest() {
        int fromRow = 0;
        int fromCol = 0;
        int toRow = 2;
        int toCol = 0;

        int fromRow2 = 1;
        int fromCol2 = 1;
        int toRow2 = 2;
        int toCol2 = 1;

        Dice dice1 = new Dice(Dice.ColorDice.YELLOW, 1, -1);
        Dice dice2 = new Dice(Dice.ColorDice.YELLOW, 5, -1);

        testBoard.insertDice(
                dice1,
                fromRow, fromCol);
        testBoard.insertDice(
                dice2,
                fromRow2, fromCol2);
        testDraftPool = new DraftPool(
                Arrays.asList(
                        new Dice(1, Dice.ColorDice.YELLOW, -1)
                )
        );
        testRoundTrack.addDice(testDraftPool);

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(testRoundTrack, null, null, testBoard, testIntegerParams);


        System.out.println("\nnotColorAdjacentMoveTest: \t");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        // SUCCESS use
        bSuccess = toolCard12.getStrategy().use(testParams);

        testBoard.print(GuiSettings.bFixedFont);

        assertEquals(true, bSuccess);
        assertEquals(2, testBoard.getDiceQuantity());
        assertEquals(dice1, testBoard.getDice(toRow, toCol));
        assertEquals(dice2, testBoard.getDice(toRow2, toCol2));
    }

    @Test (expected = DifferentColorException.class)
    public void notSameColorMoveTest() {
        int fromRow = 0;
        int fromCol = 0;
        int toRow = 2;
        int toCol = 0;

        int fromRow2 = 1;
        int fromCol2 = 0;
        int toRow2 = 2;
        int toCol2 = 1;

        Dice dice1 = new Dice(Dice.ColorDice.YELLOW, 1, -1);
        Dice dice2 = new Dice(Dice.ColorDice.PURPLE, 5, -1);

        testBoard.insertDice(
                dice1,
                fromRow, fromCol);
        testBoard.insertDice(
                dice2,
                fromRow2, fromCol2);
        testDraftPool = new DraftPool(
                Arrays.asList(
                        new Dice(1, Dice.ColorDice.YELLOW, -1)
                )
        );
        testRoundTrack.addDice(testDraftPool);

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(testRoundTrack, null, null, testBoard, testIntegerParams);


        System.out.println("\nnotColorAdjacentMoveTest: \t");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        // SUCCESS use
        bSuccess = toolCard12.getStrategy().use(testParams);

        testBoard.print(GuiSettings.bFixedFont);

        assertEquals(true, bSuccess);
        assertEquals(2, testBoard.getDiceQuantity());
        assertEquals(dice1, testBoard.getDice(toRow, toCol));
        assertEquals(dice2, testBoard.getDice(toRow2, toCol2));
    }


    @Test (expected = CorlorNotFoundInRoundTrackException.class)
    public void differentColorInTrackMoveTest() {
        int fromRow = 0;
        int fromCol = 1;
        int toRow = 1;
        int toCol = 2;

        int fromRow2 = 1;
        int fromCol2 = 0;
        int toRow2 = 2;
        int toCol2 = 3;

        Dice dice1 = new Dice(Dice.ColorDice.PURPLE, 1, -1);
        Dice dice2 = new Dice(Dice.ColorDice.PURPLE, 5, -1);

        testBoard.insertDice(
                dice1,
                fromRow, fromCol);
        testBoard.insertDice(
                dice2,
                fromRow2, fromCol2);
        testDraftPool = new DraftPool(
                Arrays.asList(
                        new Dice(1, Dice.ColorDice.YELLOW, -1)
                )
        );
        testRoundTrack.addDice(testDraftPool);

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);
        testIntegerParams.add(fromRow2);
        testIntegerParams.add(fromCol2);
        testIntegerParams.add(toRow2);
        testIntegerParams.add(toCol2);

        testParams = new ToolCardParam(testRoundTrack, null, null, testBoard, testIntegerParams);


        System.out.println("\nnotColorAdjacentMoveTest: \t");
        System.out.println("Move from (" + fromRow + ":" + fromCol +
                ") \tto (" + toRow + ":" + toCol + ") \t");
        System.out.println("and  from (" + fromRow2 + ":" + fromCol2 +
                ") \tto (" + toRow2 + ":" + toCol2 + ")");
        testBoard.print(GuiSettings.bFixedFont);

        // SUCCESS use
        bSuccess = toolCard12.getStrategy().use(testParams);

        testBoard.print(GuiSettings.bFixedFont);

        assertEquals(true, bSuccess);
        assertEquals(2, testBoard.getDiceQuantity());
        assertEquals(dice1, testBoard.getDice(toRow, toCol));
        assertEquals(dice2, testBoard.getDice(toRow2, toCol2));
    }



}
