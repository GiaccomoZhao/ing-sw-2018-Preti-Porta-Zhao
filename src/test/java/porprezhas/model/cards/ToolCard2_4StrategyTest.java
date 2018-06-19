package porprezhas.model.cards;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import porprezhas.Useful;
import porprezhas.exceptions.GameAbnormalException;
import porprezhas.exceptions.diceMove.EdgeRestrictionException;
import porprezhas.exceptions.diceMove.IndexOutOfBoardBoundsException;
import porprezhas.exceptions.toolCard.DiceNotFoundInBoardException;
import porprezhas.exceptions.toolCard.IncorrectParamQuantityException;
import porprezhas.exceptions.toolCard.MoveToSelfException;
import porprezhas.exceptions.toolCard.ToolCardParameterException;
import porprezhas.model.GameConstants;
import porprezhas.model.dices.*;

import java.util.*;

import static org.junit.Assert.*;
import static porprezhas.model.dices.Board.COLUMN;
import static porprezhas.model.dices.Board.ROW;
import static porprezhas.model.dices.CellPosition.*;
import static porprezhas.model.dices.CellPosition.getRandomBorderValue;
import static porprezhas.model.dices.Dice.*;

@RunWith(Parameterized.class)
public class ToolCard2_4StrategyTest {
    @Parameterized.Parameter(0)
    public String parameterizedTestName;
    @Parameterized.Parameter(1)
    public Board parameterizedBoard;
    @Parameterized.Parameter(2)
    public Dice parameterizedDice;
    @Parameterized.Parameter(3)
    public List<Integer> parameterizedIntegerParams;
    @Parameterized.Parameter(4)
    public boolean parameterizedSuccess;
    @Parameterized.Parameter(5)
    public boolean parameterizedReturnValue;
    @Parameterized.Parameter(6)
    public Class<? extends RuntimeException> expectedException;


    private ToolCard toolCard2;
    private ToolCard toolCard3;
    private ToolCardParam param;
    private List<Integer> integerParams;

    private Dice genericDice;
    // used dice container
    private Board emptyBoard;

    // not used container
    private DiceBag diceBag;
    private DraftPool draftPool;
    private RoundTrack roundTrack;


    Board newBoard;         // a new testBoard that will be initialized by parameterizedBoard
                            // used to separate toolCard2 Test with toolCard3 Test
    boolean bSuccess;

    // CREATE SOME NOTE POSITIONS

    private static final Random random = new Random();    // used to choose an in range(correct) number arbitrarily
    // NB: this random should NOT influence the Result of tests

    // inside testBoard -Not border-
    private static final CellPosition innerPosition = getRandomInnerValue(random.nextLong());
    private static final CellPosition[] innerPositions = {innerPosition};

    // Corners
    private static final CellPosition topLeftPosition = new CellPosition(MIN_ROW, MIN_COLUMN);
    private static final CellPosition bottomRightPosition = new CellPosition(MAX_ROW, MAX_COLUMN);
    private static final CellPosition topRightPosition = new CellPosition(MIN_ROW, MAX_COLUMN);
    private static final CellPosition bottomLeftPosition = new CellPosition(MAX_ROW, MIN_COLUMN);
    private static final CellPosition[] cornersPositions = {topLeftPosition, bottomRightPosition, topRightPosition, bottomLeftPosition};


    // inner BORDER
    private static final CellPosition topBorderPosition = getRandomBorderValue(random.nextLong(), Bound.TOP, true);
    private static final CellPosition botBorderPosition = getRandomBorderValue(random.nextLong(), Bound.BOTTOM, true);
    private static final CellPosition leftBorderPosition = getRandomBorderValue(random.nextLong(), Bound.LEFT, true);
    private static final CellPosition rightBorderPosition = getRandomBorderValue(random.nextLong(), Bound.RIGHT, true);
    private static final CellPosition[] innerBorderPositions = {topBorderPosition, botBorderPosition, leftBorderPosition, rightBorderPosition};

    // second inner BORDER
    private static final CellPosition different_topBorderPosition = new CellPosition(
            Useful.getRandomNumberExcept(ROW, topBorderPosition.getRow(), MIN_ROW, MAX_ROW),
            CellPosition.MIN_COLUMN);

    private static final CellPosition different_botBorderPosition = new CellPosition(
            Useful.getRandomNumberExcept(ROW, botBorderPosition.getRow(), MIN_ROW, MAX_ROW),
            CellPosition.MAX_COLUMN);

    private static final CellPosition different_leftBorderPosition = new CellPosition(
            CellPosition.MIN_ROW,
            Useful.getRandomNumberExcept(COLUMN, leftBorderPosition.getCol(), MIN_COLUMN, MAX_COLUMN));

    private static final CellPosition different_rightBorderPosition = new CellPosition(
            CellPosition.MAX_ROW,
            Useful.getRandomNumberExcept(COLUMN, rightBorderPosition.getCol(), MIN_COLUMN, MAX_COLUMN));
    private static final CellPosition[] secondInnerBorderPositions = {different_topBorderPosition, different_botBorderPosition, different_leftBorderPosition, different_rightBorderPosition};


    // OUT OF BOUNDS
    private static final CellPosition over_topBoundPosition = new CellPosition(
            MIN_ROW - 1,
            Useful.getRandomNumber(COLUMN));

    private static final CellPosition over_botBoundPosition = new CellPosition(
            MAX_ROW + 1,
            Useful.getRandomNumber(COLUMN));

    private static final CellPosition over_leftBoundPosition = new CellPosition(
            Useful.getRandomNumber(ROW),
            MIN_COLUMN - 1);

    private static final CellPosition over_rightBoundPosition = new CellPosition(
            Useful.getRandomNumber(ROW),
            MAX_COLUMN + 1);

    private static final CellPosition outBoundPosition_topLeft = new CellPosition(MIN_ROW - 1, MIN_COLUMN - 1);
    private static final CellPosition outBoundPosition_bottomRight = new CellPosition(MAX_ROW + 1, MAX_COLUMN + 1);
    private static final CellPosition outBoundPosition_topRight = new CellPosition(MIN_ROW - 1, MAX_COLUMN + 1);
    private static final CellPosition outBoundPosition_bottomLeft = new CellPosition(MAX_ROW + 1, MIN_COLUMN - 1);

    private static final CellPosition[] outBoundsPositions = {
            over_topBoundPosition, outBoundPosition_topRight,
            over_rightBoundPosition, outBoundPosition_bottomRight,
            over_botBoundPosition, outBoundPosition_bottomLeft,
            over_leftBoundPosition, outBoundPosition_topLeft
    };

    private static final List<CellPosition[]> allPositions =
            Arrays.asList(
                    innerPositions,
                    cornersPositions,
                    innerBorderPositions,
                    secondInnerBorderPositions,
                    outBoundsPositions
            );

/*    public ToolCard2_4StrategyTest(Board parameterizedBoard, Dice parameterizedDice, List<Integer> parameterizedIntegerParams, boolean parameterizedSuccess, boolean parameterizedReturnValue) {
        this.parameterizedBoard = parameterizedBoard;
        this.parameterizedDice = parameterizedDice;
        this.parameterizedIntegerParams = parameterizedIntegerParams;
        this.parameterizedSuccess = parameterizedSuccess;
        this.parameterizedReturnValue = parameterizedReturnValue;
    }
*/

    @Parameterized.Parameters
    public static Collection<Object[]> data() {

        List<Object[]> dataList= new ArrayList<>();

        Object[][] data = new Object[][]{
                {"All Null Test",
                        null,
                        null,
                        null,
                        false,
                        false,
                        IncorrectParamQuantityException.class},
                {"Null Param Test",
                        new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        null,
                        false,
                        false,
                        IncorrectParamQuantityException.class},
                {"Null Board Test",
                        null,
                        null,
                        Arrays.asList(
                                topLeftPosition.getRow(),
                                topLeftPosition.getCol(),
                                topLeftPosition.getRow(),
                                topLeftPosition.getCol()),
                        false,
                        false,
                        GameAbnormalException.class},

                {"All Empty Test",
                        new Board(Pattern.TypePattern.VOID),
                        null,
                        new ArrayList(),
                        false,
                        false,
                        IncorrectParamQuantityException.class},
                {"Empty integer Parameter Test",
                        new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        new ArrayList(),
                        false,
                        false,
                        IncorrectParamQuantityException.class},
                {"Empty Board Test",
                        new Board(Pattern.TypePattern.VOID),
                        null,
                        Arrays.asList(
                                topLeftPosition.getRow(),
                                topLeftPosition.getCol(),
                                botBorderPosition.getRow(),
                                botBorderPosition.getCol()),
                        false,
                        false,
                        DiceNotFoundInBoardException.class},

                // on self - 6
                {"Move on self Test1",
                        new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(
                                topLeftPosition.getRow(),
                                topLeftPosition.getCol(),
                                topLeftPosition.getRow(),
                                topLeftPosition.getCol()),
                        false,
                        false,
                        MoveToSelfException.class},

                {"Move on self Test2",
                        new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(
                                bottomRightPosition.getRow(),
                                bottomRightPosition.getCol(),
                                bottomRightPosition.getRow(),
                                bottomRightPosition.getCol()),
                        false,
                        false,
                        MoveToSelfException.class},

/*              // Impossible initial situation!!! (to place inside inner testBoard position)
                {"Move on self Test3",
                        new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(
                                innerPosition.getRow(),
                                innerPosition.getCol(),
                                innerPosition.getRow(),
                                innerPosition.getCol()),
                        false,
                        false,
                        MoveToSelfException.class},
*/
                // not on border, not respect adjacent constraint
                // out of bound - 8
                {"Out of bound Test",
                        new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(
                                over_topBoundPosition.getRow(),
                                over_topBoundPosition.getCol(),
                                topBorderPosition.getRow(),
                                topBorderPosition.getCol()),
                        false,
                        false,
                        IndexOutOfBoardBoundsException.class},
                {"Out of bound Test",
                        new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(
                                outBoundPosition_bottomLeft.getRow(),
                                outBoundPosition_bottomLeft.getCol(),
                                leftBorderPosition.getRow(),
                                leftBorderPosition.getCol()),
                        false,
                        false,
                        IndexOutOfBoardBoundsException.class},

                {"Out of bound Test",
                        new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(
                                over_rightBoundPosition.getRow(),
                                over_rightBoundPosition.getCol(),
                                botBorderPosition.getRow(),
                                botBorderPosition.getCol()),
                        false,
                        false,
                        IndexOutOfBoardBoundsException.class},

                {"Out of bound Test",
                        new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(
                                over_rightBoundPosition.getRow(),
                                over_rightBoundPosition.getCol(),
                                outBoundPosition_topRight.getRow(),
                                outBoundPosition_topRight.getCol()),
                        false,
                        false,
                        IndexOutOfBoardBoundsException.class},

/*              // from inner testBoard to border
                {new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(MAX_ROW / 2, MAX_COLUMN + 2, MIN_ROW, MAX_COLUMN / 2),
                        false,
                        false},

                // from inner testBoard to border
                {new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(MAX_ROW / 2, MAX_COLUMN, MAX_ROW, MAX_COLUMN),
                        false,
                        false},
                {new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(MAX_ROW, MAX_COLUMN / 2, MAX_ROW, MAX_COLUMN),
                        false,
                        false},
                {new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(MAX_ROW / 2, MAX_COLUMN / 2, MAX_ROW, MAX_COLUMN),
                        false,
                        false},
*/
                // from border to inner testBoard - 11
                {"from border to inner testBoard Test",
                        new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(
                                topBorderPosition.getRow(),
                                topBorderPosition.getCol(),
                                MIN_ROW +1,
                                MAX_COLUMN /2),
                        false,
                        false,
                        EdgeRestrictionException.class},

                // from corner to inner testBoard
                {"from corner to inner testBoard Test",
                        new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(
                                bottomRightPosition.getRow(),
                                bottomRightPosition.getCol(),
                                MAX_ROW -1,
                                MAX_COLUMN -1),
                        false,
                        false,
                        EdgeRestrictionException.class},

                // from corner to inner Border
                {"from corner to inner border Test",
                        new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(
                                bottomLeftPosition.getRow(),
                                bottomLeftPosition.getCol(),
                                MIN_ROW +1,
                                MAX_COLUMN),
                        true,
                        true,
                        null},

                // from testBoard's border to an other border
                // corners exchange - 14
                {"corners exchange Test",
                        new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(
                                bottomRightPosition.getRow(),
                                bottomRightPosition.getCol(),
                                topLeftPosition.getRow(),
                                topLeftPosition.getCol()),
                        true,
                        true,
                        null},

                {"corners exchange Test",
                        new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(
                                topLeftPosition.getRow(),
                                topLeftPosition.getCol(),
                                bottomRightPosition.getRow(),
                                bottomRightPosition.getCol() ),
                        true,
                        true,
                        null},

                {"corners exchange Test",
                        new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(
                                bottomLeftPosition.getRow(),
                                bottomLeftPosition.getCol(),
                                topRightPosition.getRow(),
                                topRightPosition.getCol()),
                        true,
                        true,
                        null},

                {"corners exchange Test",
                        new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(MIN_ROW, MAX_COLUMN, MAX_ROW, MIN_COLUMN),
                        true,
                        true,
                        null},
/*
                //
                {new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(MIN_ROW, MAX_COLUMN, MAX_ROW, MAX_COLUMN / 2),
                        false,
                        false},

                {new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(MAX_ROW, MIN_COLUMN, MAX_ROW, MAX_COLUMN),
                        false,
                        false},


                {new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(MIN_ROW, MIN_COLUMN, MAX_ROW, MAX_COLUMN),
                        false,
                        false},

                {new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(MIN_ROW, MIN_COLUMN, ROW, COLUMN),
                        false,
                        false},

                {new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(0, 0, 1, 1),
                        false,
                        false},

                {new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(0, 0, 1, 1),
                        false,
                        false},

                {new Board(Pattern.TypePattern.VOID),
                        new Dice(ColorDice.RED, 6, -1),
                        Arrays.asList(0, 0, 1, 1),
                        false,
                        false},
*/        };

        for (Object[] obj : data) {
            dataList.add(obj);
        }


        CellPosition[] fromPositions = innerBorderPositions;
        for (CellPosition fromPosition : fromPositions) {
//            for (CellPosition[] toPositions : allPositions) {
//                if (!fromPositions.equals(toPositions)) {
            for (CellPosition toPosition : secondInnerBorderPositions) {    //toPositions) {
                dataList.add(
                        new Object[]{"inner border to inner border Test",
                                new Board(Pattern.TypePattern.VOID),
                                new Dice(ColorDice.RED, 6, -1),
                                Arrays.asList(
                                        fromPosition.getRow(),
                                        fromPosition.getCol(),
                                        toPosition.getRow(),
                                        toPosition.getCol()
                                ),
                                true,
                                true,
                                null}
                );
            }
            for (CellPosition toPosition : cornersPositions) {    //toPositions) {
                dataList.add(
                        new Object[]{"inner border to corner Test",
                                new Board(Pattern.TypePattern.VOID),
                                new Dice(ColorDice.RED, 6, -1),
                                Arrays.asList(
                                        fromPosition.getRow(),
                                        fromPosition.getCol(),
                                        toPosition.getRow(),
                                        toPosition.getCol()
                                ),
                                true,
                                true,
                                null}
                );
            }
        }

        fromPositions = cornersPositions;
        for (CellPosition fromPosition : fromPositions) {
                    for (CellPosition toPosition : innerBorderPositions) {    //toPositions) {
                        dataList.add(
                                new Object[]{"corner to inner border Test",
                                        new Board(Pattern.TypePattern.VOID),
                                        new Dice(ColorDice.RED, 6, -1),
                                        Arrays.asList(
                                                fromPosition.getRow(),
                                                fromPosition.getCol(),
                                                toPosition.getRow(),
                                                toPosition.getCol()
                                        ),
                                        true,
                                        true,
                                        null}
                        );
                    }
        }

        fromPositions = innerBorderPositions;
        for (CellPosition fromPosition : fromPositions) {
                    for (CellPosition toPosition : outBoundsPositions) {
                        dataList.add(
                                new Object[]{"inner border to Out Bound Test",
                                        new Board(Pattern.TypePattern.VOID),
                                        new Dice(ColorDice.RED, 6, -1),
                                        Arrays.asList(
                                                fromPosition.getRow(),
                                                fromPosition.getCol(),
                                                toPosition.getRow(),
                                                toPosition.getCol()
                                        ),
                                        false,
                                        false,
                                        IndexOutOfBoardBoundsException.class}
                        );
                    }
        }


//            } }

        // NB: Arrays.asList() returns AbstractList that hasn't implemented a correct add method
        // and now we are using ArrayList, so we haven't this problem
        return dataList;
    }


    @Before
    public void setUp() {
/*        emptyBoard = new Board(Pattern.TypePattern.VOID);
        testIntegerParams = new ArrayList<>();

        testDiceBag = new DiceBag();
        testDraftPool = new DraftPool(testDiceBag.GetRandomDices(random.nextInt(GameConstants.MAX_PLAYER_QUANTITY) + 1));
        testRoundTrack = new RoundTrack();

        testParams = new ToolCardParam(testRoundTrack, testDraftPool, testDiceBag, emptyBoard, testIntegerParams);

        ColorDice genericColor = ColorDice.values()[random.nextInt(ColorDice.values().length - 1)];
        int genericNumber = random.nextInt(MAX_DICE_NUMBER) + 1;
        genericDice = new Dice(genericColor, genericNumber, -1);
*/
        toolCard2 = new ToolCard(Card.Effect.TC2);
        toolCard3 = new ToolCard(Card.Effect.TC3);

        newBoard = null;
        if(null != parameterizedBoard) {
            newBoard = new Board( parameterizedBoard.getPattern().getTypePattern() );
        }




        System.out.print("\n" + parameterizedTestName + ": \t");
        boolean bImpossibleInitialSituation = false;


        // emulate the testBoard situation
        if(null != parameterizedDice && null != parameterizedIntegerParams && parameterizedIntegerParams.size()>=2) {
            try {
                newBoard.insertDice(parameterizedDice, parameterizedIntegerParams.get(0), parameterizedIntegerParams.get(1));
            } catch (Exception e) {
//                System.out.println("becomes Empty Board Test");
                //setup expected exception
//                bImpossibleInitialSituation = true;
//                thrown.expect(DiceNotFoundInBoardException.class);
            }
        }
        //setup expected exception
        if (expectedException != null  &&  !bImpossibleInitialSituation) {
            thrown.expect(expectedException);
        }


        param = new ToolCardParam(null, null, null, newBoard, parameterizedIntegerParams);

        // Check Before
        // do not check return, because it depends by previous use
        // PRINT the situation of testBoard
        if(null != parameterizedIntegerParams && parameterizedIntegerParams.size()>=4) {
            System.out.println("Move from (" + parameterizedIntegerParams.get(0) + ":" + parameterizedIntegerParams.get(1) +
                    ") \tto (" + parameterizedIntegerParams.get(2) + ":" + parameterizedIntegerParams.get(3) + ")");
        }
        if(null != newBoard) {
            System.out.println(newBoard.toString());
        }

        bSuccess = false;
    }


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parametrizedCard2Test() throws RuntimeException{
        try {
            bSuccess = toolCard2.getStrategy().use(param);      // USE

            // when we expect a exception but it does not be throw
            if(null != expectedException)
                fail();

        } catch (RuntimeException e) {
            System.out.println("\nException found: \t" + e.getClass().getCanonicalName());

            if(null == expectedException  ||  !e.getClass().getCanonicalName().equals( expectedException.getCanonicalName() )) {
                fail();
            }
            System.out.println(e.getMessage());
            System.out.flush();         // i can not understand why serr would not be printed immediately
            throw e;
        }

        assertEquals(parameterizedReturnValue, ((ToolCard2) toolCard2.getStrategy()).getReturn());

        assertEquals(1, newBoard.getDiceQuantity());        // in these tests we test with only 1 dice!!!
    }

    @Test
    public void parametrizedCard3Test() throws RuntimeException{
        try {
            bSuccess = toolCard3.getStrategy().use(param);      // USE

            // when we expect a exception but it does not be throw
            if(null != expectedException)
                fail();

        } catch (RuntimeException e) {
            System.out.println("\nException found: \t" + e.getClass().getCanonicalName());

            if(null == expectedException  ||  !e.getClass().getCanonicalName().equals( expectedException.getCanonicalName() )) {
                fail();
            }
            System.out.println(e.getMessage());
            System.out.flush();         // i can not understand why serr would not be printed immediately
            throw e;
        }

        assertEquals(parameterizedReturnValue, ((ToolCard3) toolCard3.getStrategy()).getReturn());

        assertEquals(1, newBoard.getDiceQuantity());        // in these tests we test with only 1 dice!!!
    }

    @After
    public void tearDown() throws Exception {

        // NB: if ToolCard.use() throws Exception, the print will be aborted
        // Check After -result-
        if(null != newBoard)
            System.out.println(newBoard.toString());

        if(bSuccess)
            System.out.println("\nSUCCESS!!!");
        else
            System.out.println("\nFAILURE!!!");
        System.out.println("_______________________________________________________________________\n\n");
        System.out.flush();

        assertEquals(parameterizedSuccess, bSuccess);
    }
/*
    @Test
    public void nullTest() {
        // test toolCard is constructed
        assertNotNull(toolCard2.getStrategy());

        // test with Null parameters
        assertFalse(toolCard2.getStrategy().use(null));

        // test with Empty parameters
        assertFalse(toolCard2.getStrategy().use(testParams));

        // test with all Null
        testParams = new ToolCardParam(null, null, null, null, null);
        assertFalse(toolCard2.getStrategy().use(testParams));

        // test with Null Integer Params
        testParams = new ToolCardParam(null, null, null, emptyBoard, null);
        assertFalse(toolCard2.getStrategy().use(testParams));


        // CORRECT TEST

        // test with Correct Container and testParams, but Null not used Container
        int fromRow = botBorderPosition.getRow();
        int fromCol = botBorderPosition.getCol();
        int toRow = topBorderPosition.getRow();
        int toCol = topBorderPosition.getCol();
        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        ColorDice genericColor = ColorDice.values()[random.nextInt(ColorDice.values().length - 1)];
        int genericNumber = random.nextInt(MAX_DICE_NUMBER) + 1;
        emptyBoard.insertDice(new Dice(genericColor, genericNumber, -1), fromRow, fromCol);

        testParams = new ToolCardParam(null, null, null, emptyBoard, testIntegerParams);   // correct Container

        // PRINT the Board, because we are testing a generic situation
        System.out.print("\n\nnullTest: ");
        System.out.println("\t Move from (" + parameterizedIntegerParams.get(0) + ":" + parameterizedIntegerParams.get(1) +
                ") \tto (" + parameterizedIntegerParams.get(2) + ":" + parameterizedIntegerParams.get(3) + ")");
        System.out.println(emptyBoard.toString());

        assertTrue(toolCard2.getStrategy().use(testParams));           // USE

        System.out.println(emptyBoard.toString());
    }

    @Test
    public void emptyTest() {
        // initialize empty containers and params
        RoundTrack emptyRoundTrack = new RoundTrack();
        DraftPool emptyDraftPool = new DraftPool();
        DiceBag emptyDiceBag = new DiceBag();
        Board emptyBoard = new Board(Pattern.TypePattern.VOID);
        List<Integer> emptyParams = new ArrayList<>();

        // test with All Empty
        testParams = new ToolCardParam(emptyRoundTrack, emptyDraftPool, emptyDiceBag, emptyBoard, emptyParams);
        assertFalse(toolCard2.getStrategy().use(testParams));

        // test with Null Dice Container and EMPTY Integer Params
        testParams = new ToolCardParam(null, null, null, null, emptyParams);
        assertFalse(toolCard2.getStrategy().use(testParams));

        // test with Correct Dice Container and EMPTY Integer Params
        testParams = new ToolCardParam(emptyRoundTrack, testDraftPool, testDiceBag, emptyBoard, emptyParams);
        assertFalse(toolCard2.getStrategy().use(testParams));

        // test with NULL Dice Container and Correct Integer Params
        testParams = new ToolCardParam(null, null, null, null, testIntegerParams);
        assertFalse(toolCard2.getStrategy().use(testParams));

        // test with EMPTY Dice Container and Correct Integer Params
        testParams = new ToolCardParam(emptyRoundTrack, emptyDraftPool, emptyDiceBag, emptyBoard, testIntegerParams);
        assertFalse(toolCard2.getStrategy().use(testParams));
    }

    @Test
    public void genericTest() {
        // setup
        CellPosition fromPosition = getRandomBorderValue(random.nextLong());
        CellPosition toPosition = getRandomBorderValue(random.nextLong());
        int fromRow = fromPosition.getRow();
        int fromCol = fromPosition.getCol();
        int toRow = toPosition.getRow();
        int toCol = toPosition.getCol();

        emptyBoard.insertDice(genericDice, fromRow, fromCol);

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        testParams = new ToolCardParam(null, null, null, emptyBoard, testIntegerParams);   // correct Container

        // PRINT the Board, because we are testing a generic situation
        System.out.print("\n\ngenericTest: ");
        System.out.println("\t Move from (" + parameterizedIntegerParams.get(0) + ":" + parameterizedIntegerParams.get(1) +
                ") \tto (" + parameterizedIntegerParams.get(2) + ":" + parameterizedIntegerParams.get(3) + ")");
        System.out.println(emptyBoard.toString());

        if (!fromPosition.equals(toPosition))
            assertTrue(toolCard2.getStrategy().use(testParams));           // USE
        else
            assertFalse(toolCard2.getStrategy().use(testParams));

        System.out.println(emptyBoard.toString());
    }



    @Test
    public void moveOnSelfTest() {
        // setup
        CellPosition fromPosition = getRandomBorderValue(random.nextLong());
        CellPosition toPosition = fromPosition;
        int fromRow = fromPosition.getRow();
        int fromCol = fromPosition.getCol();
        int toRow = toPosition.getRow();
        int toCol = toPosition.getCol();

        emptyBoard.insertDice(genericDice, fromRow, fromCol);

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        testParams = new ToolCardParam(null, null, null, emptyBoard, testIntegerParams);   // correct Container

        // use
        assertFalse(toolCard2.getStrategy().use(testParams));

        // check result
        assertFalse(((ToolCard2) toolCard2.getStrategy()).getReturn());
        assertEquals(genericDice, emptyBoard.getDice(fromRow, fromCol));
    }


    @Test
    public void adjacentConstraintTest_topBorder() {
        // setup
        int fromRow = topBorderPosition.getRow();
        int fromCol = topBorderPosition.getCol();
        int toRow = different_topBorderPosition.getRow();
        int toCol = different_topBorderPosition.getCol();


        emptyBoard.insertDice(genericDice, fromRow, fromCol);

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        testParams = new ToolCardParam(null, null, null, emptyBoard, testIntegerParams);   // correct Container

        // use
        assertFalse(toolCard2.getStrategy().use(testParams));

        // check result
        assertFalse(((ToolCard2) toolCard2.getStrategy()).getReturn());
        assertEquals(genericDice, emptyBoard.getDice(fromRow, fromCol));
    }

    @Test
    public void adjCentConstraintTest_botBorder() {
        // setup
        CellPosition fromPosition = getRandomBorderValue(random.nextLong());
        CellPosition toPosition = getRandomBorderValue(random.nextLong());
        int fromRow = fromPosition.getRow();
        int fromCol = fromPosition.getCol();
        int toRow = toPosition.getRow();
        int toCol = toPosition.getCol();

        emptyBoard.insertDice(genericDice, fromRow, fromCol);

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        testParams = new ToolCardParam(null, null, null, emptyBoard, testIntegerParams);   // correct Container

        assertFalse(toolCard2.getStrategy().use(testParams));
    }

    @Test
    public void adjCentConstraintTest_leftBorder() {
        // setup
        CellPosition fromPosition = getRandomBorderValue(random.nextLong());
        CellPosition toPosition = getRandomBorderValue(random.nextLong());
        int fromRow = fromPosition.getRow();
        int fromCol = fromPosition.getCol();
        int toRow = toPosition.getRow();
        int toCol = toPosition.getCol();

        emptyBoard.insertDice(genericDice, fromRow, fromCol);

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        testParams = new ToolCardParam(null, null, null, emptyBoard, testIntegerParams);   // correct Container

        assertFalse(toolCard2.getStrategy().use(testParams));
    }

    @Test
    public void adjCentConstraintTest_rightBorder() {
        // setup
        CellPosition fromPosition = getRandomBorderValue(random.nextLong());
        CellPosition toPosition = getRandomBorderValue(random.nextLong());
        int fromRow = fromPosition.getRow();
        int fromCol = fromPosition.getCol();
        int toRow = toPosition.getRow();
        int toCol = toPosition.getCol();

        emptyBoard.insertDice(genericDice, fromRow, fromCol);

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        testParams = new ToolCardParam(null, null, null, emptyBoard, testIntegerParams);   // correct Container

        assertFalse(toolCard2.getStrategy().use(testParams));
    }

    @Test
    public void adjacentConstraintTest_topBound() {
        // setup
        CellPosition fromPosition = getRandomBorderValue(random.nextLong());
        CellPosition toPosition = getRandomBorderValue(random.nextLong());
        int fromRow = fromPosition.getRow();
        int fromCol = fromPosition.getCol();
        int toRow = toPosition.getRow();
        int toCol = toPosition.getCol();

        emptyBoard.insertDice(genericDice, fromRow, fromCol);

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        testParams = new ToolCardParam(null, null, null, emptyBoard, testIntegerParams);   // correct Container

        assertFalse(toolCard2.getStrategy().use(testParams));
    }

    @Test
    public void adjCentConstraintTest_botBound() {
        // setup
        CellPosition fromPosition = getRandomBorderValue(random.nextLong());
        CellPosition toPosition = getRandomBorderValue(random.nextLong());
        int fromRow = fromPosition.getRow();
        int fromCol = fromPosition.getCol();
        int toRow = toPosition.getRow();
        int toCol = toPosition.getCol();

        emptyBoard.insertDice(genericDice, fromRow, fromCol);

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        testParams = new ToolCardParam(null, null, null, emptyBoard, testIntegerParams);   // correct Container

        assertFalse(toolCard2.getStrategy().use(testParams));
    }

    @Test
    public void adjCentConstraintTest_leftBound() {
        // setup
        CellPosition fromPosition = getRandomBorderValue(random.nextLong());
        CellPosition toPosition = getRandomBorderValue(random.nextLong());
        int fromRow = fromPosition.getRow();
        int fromCol = fromPosition.getCol();
        int toRow = toPosition.getRow();
        int toCol = toPosition.getCol();

        emptyBoard.insertDice(genericDice, fromRow, fromCol);

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        testParams = new ToolCardParam(null, null, null, emptyBoard, testIntegerParams);   // correct Container

        assertFalse(toolCard2.getStrategy().use(testParams));
    }

    @Test
    public void adjCentConstraintTest_rightBound() {
        // setup
        CellPosition fromPosition = getRandomBorderValue(random.nextLong());
        CellPosition toPosition = getRandomBorderValue(random.nextLong());
        int fromRow = fromPosition.getRow();
        int fromCol = fromPosition.getCol();
        int toRow = toPosition.getRow();
        int toCol = toPosition.getCol();

        emptyBoard.insertDice(genericDice, fromRow, fromCol);

        testIntegerParams.add(fromRow);
        testIntegerParams.add(fromCol);
        testIntegerParams.add(toRow);
        testIntegerParams.add(toCol);

        testParams = new ToolCardParam(null, null, null, emptyBoard, testIntegerParams);   // correct Container

        assertFalse(toolCard2.getStrategy().use(testParams));
    }
    */
}
