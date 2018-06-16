package porprezhas.model.dices;

import org.junit.Before;
import org.junit.Test;
import porprezhas.exceptions.diceMove.*;

import java.util.Random;

import porprezhas.model.dices.Dice.ColorDice;
import porprezhas.view.fx.gameScene.GuiSettings;

import static org.junit.Assert.*;
import static porprezhas.model.dices.Board.COLUMN;
import static porprezhas.model.dices.Board.ROW;
import static porprezhas.model.dices.Dice.MAX_DICE_NUMBER;
import static porprezhas.model.dices.Pattern.*;

public class BoardTestZX {

    Board testBoard;
    Board voidBoard;

    Dice dieSmall, dieBig;
    Dice dieTooSmall, dieTooBig;

    // the random values in test, stand for a correct value
    Dice dieGeneric;
    Random random;      // we'll assign a random value to the attribute that we don't care about


    int correctRow_notBound;
    int correctColumn_notBound;

    final int ROW_LowerBound = 0;
    final int COLUMN_LowerBound = 0;
    final int ROW_HigherBound = ROW-1;
    final int COLUMN_HigherBound = Board.COLUMN-1;

    // this pattern has:
    // in (0,0) number = 1
    // in (0,1) color  = purple
    // in (1,0) color  = purple
    // in (1,1) color  = yellow
    final TypePattern testPatternType = TypePattern.SUNS_GLORY;
    final Dice die1;
    final Dice dieNot1;
    final Dice diePurple;
    final Dice dieNotPurple;

    final Dice die1Purple;
    final Dice die1NotPurple;
    final Dice dieNot1ButPurple;
    final Dice dieNot1NotPurple;

    final Dice dieYellow;
    final Dice die1Yellow;

    long idCounter = 0;


    public BoardTestZX() {
        random = new Random();
        die1 = new Dice(1,
                ColorDice.values() [random.nextInt(ColorDice.values().length -1)],
                idCounter++);  // -1 to skip white color that is last position // NOT -1 and +1 to skip white color that is at first position
        diePurple = new Dice( random.nextInt(MAX_DICE_NUMBER) +1,
                ColorDice.PURPLE,
                idCounter++);
        dieNot1 = new Dice(7 - 1,
                ColorDice.values() [random.nextInt(ColorDice.values().length -1)],
                idCounter++);
        dieNotPurple = new Dice( random.nextInt(MAX_DICE_NUMBER) +1,
                ColorDice.RED,
                idCounter++);

        dieNot1ButPurple = new Dice (7-1 , ColorDice.PURPLE,
                idCounter++);
        die1NotPurple = new Dice (1, ColorDice.GREEN,
                idCounter++);
        die1Purple = new Dice (1, ColorDice.PURPLE,
                idCounter++);
        dieNot1NotPurple = new Dice( 7 - 1,  ColorDice.BLUE,
                idCounter++);

        dieYellow = new Dice( random.nextInt(MAX_DICE_NUMBER) +1,
                ColorDice.YELLOW,
                idCounter++);
        die1Yellow = new Dice( 1, ColorDice.YELLOW,
                idCounter++);
    }

    @Before
    public void setUp() {

        correctRow_notBound = random.nextInt(ROW -2) +1;
        correctColumn_notBound = random.nextInt(Board.COLUMN -2) +1;

        // a random normal dice
        dieGeneric = new Dice(
                ColorDice.values() [random.nextInt(ColorDice.values().length -1)],
                random.nextInt(MAX_DICE_NUMBER) +1,
                idCounter++);

        dieSmall = new Dice(
                ColorDice.values() [random.nextInt(ColorDice.values().length -1)],
                1,
                idCounter++
        );
        dieBig = new Dice(
                ColorDice.values() [random.nextInt(ColorDice.values().length -1)],
                MAX_DICE_NUMBER,
                idCounter++
        );

        dieTooSmall = new Dice(
                ColorDice.values() [random.nextInt(ColorDice.values().length -1)],
                0,
                idCounter++
        );
        dieTooBig = new Dice(
                ColorDice.values() [random.nextInt(ColorDice.values().length -1)],
                MAX_DICE_NUMBER +1,
                idCounter++
        );


        testBoard = new Board(testPatternType);
        voidBoard = new Board(Pattern.TypePattern.VOID);
    }





    // ***********************************
    // ********** Insert Test  ***********
    // ***********************************


    // check all the corns and inner borders
    public void edgeTest() {
        int[] rows = {ROW_LowerBound, correctRow_notBound, ROW_HigherBound};
        int[] cols = {COLUMN_LowerBound, correctColumn_notBound, COLUMN_HigherBound};

        for (int row : rows) {
            for (int col : cols) {
                // check all the border, so exclude not border position
                if( row != correctRow_notBound  &&  col != correctColumn_notBound) {
                    voidBoard = new Board(Pattern.TypePattern.VOID);
                    assertTrue(voidBoard.insertDice(dieGeneric, row, col));
                }
            }
        }
/*
        voidBoard = new Board(Pattern.TypePattern.VOID);
        assertTrue( voidBoard.insertDice(dieGeneric, ROW_LowerBound,     COLUMN_LowerBound) );

        voidBoard = new Board(Pattern.TypePattern.VOID);
        assertTrue( voidBoard.insertDice(dieGeneric, correctRow_notBound,COLUMN_LowerBound) );

        voidBoard = new Board(Pattern.TypePattern.VOID);
        assertTrue( voidBoard.insertDice(dieGeneric, ROW_LowerBound,     correctColumn_notBound) );

        voidBoard = new Board(Pattern.TypePattern.VOID);
        assertTrue( voidBoard.insertDice(dieGeneric, correctRow_notBound,correctColumn_notBound) );

        voidBoard = new Board(Pattern.TypePattern.VOID);
        assertTrue( voidBoard.insertDice(dieGeneric, correctRow_notBound,COLUMN_HigherBound) );

        voidBoard = new Board(Pattern.TypePattern.VOID);
        assertTrue( voidBoard.insertDice(dieGeneric, ROW_HigherBound,    correctColumn_notBound) );

        voidBoard = new Board(Pattern.TypePattern.VOID);
        assertTrue( voidBoard.insertDice(dieGeneric, ROW_HigherBound,    COLUMN_HigherBound) );
        */
    }

    @Test
    public void IndexOutOfBoundsTest() {
        edgeTest();
    }

    // check board.insert out of bounds
    @Test (expected = IndexOutOfBoardBoundsException.class)
    public void IndexOutOfBounds_RowColumnUnderTest() {
        voidBoard.insertDice(dieGeneric, -1, -1);
    }
    @Test (expected = IndexOutOfBoardBoundsException.class)
    public void IndexOutOfBounds_RowColumnOverTest() {
        voidBoard.insertDice(dieGeneric, ROW, Board.COLUMN);
    }
    @Test (expected = IndexOutOfBoardBoundsException.class)
    public void IndexOutOfBounds_RowUnderTest() {
        int correctColumn = random.nextInt(Board.COLUMN);
        voidBoard.insertDice(dieGeneric, -1, correctColumn);
    }
    @Test (expected = IndexOutOfBoardBoundsException.class)
    public void IndexOutOfBounds_ColumnUnderTest() {
        int correctRow = random.nextInt(ROW);
        voidBoard.insertDice(dieGeneric, correctRow, -1);
    }
    @Test (expected = IndexOutOfBoardBoundsException.class)
    public void IndexOutOfBounds_RowOverTest() {
        int correctColumn = random.nextInt(Board.COLUMN);
        voidBoard.insertDice(dieGeneric, ROW, correctColumn);
    }
    @Test (expected = IndexOutOfBoardBoundsException.class)
    public void IndexOutOfBounds_ColumnOverTest() {
        int correctRow = random.nextInt(ROW);
        voidBoard.insertDice(dieGeneric, correctRow, Board.COLUMN);
    }




    @Test (expected = BoardCellOccupiedException.class)
    public void BoardCellOccupiedTest() {
        int row = ROW_LowerBound;       // first insert must be on the edge
        int col = COLUMN_LowerBound;
        assertTrue( voidBoard.insertDice(dieGeneric, row, col) );
        voidBoard.insertDice(dieGeneric, row, col);
    }

    @Test (expected = EdgeRestrictionException.class)
    public void EdgeRestrictionTest() {
        voidBoard.insertDice(dieGeneric, correctRow_notBound, correctColumn_notBound);
    }



    @Test
    public void ColorRestrictionTest() {
        assertTrue( testBoard.insertDice(diePurple, 0, 1) );  // correct action
    }
    @Test (expected = ColorRestrictionException.class)
    public void ColorRestrictionFailureTest() {
        testBoard.insertDice(dieNotPurple, 0, 1);   // not correct Action
    }



    @Test
    public void NumberRestrictionTest() {
        assertTrue( testBoard.insertDice(die1, 0, 0) );
    }
    @Test (expected = NumberRestrictionException.class)
    public void NumberRestrictionFailureTest() {
        testBoard.insertDice(dieNot1, 0, 0);
    }



    // 2 opposite angle position
    @Test (expected = AdjacentRestrictionException.class)
    public void AdjacentRestrictionFailure1Test() {
        assertTrue( voidBoard.insertDice(dieGeneric, 0, 0) );
        voidBoard.insertDice(dieGeneric, ROW_HigherBound, COLUMN_HigherBound);
    }
    // 2 opposite position on one border
    @Test (expected = AdjacentRestrictionException.class)
    public void AdjacentRestrictionFailure2Test() {
        assertTrue( voidBoard.insertDice(dieGeneric, 0, 0) );
        voidBoard.insertDice(dieGeneric, ROW_HigherBound, 0);
    }
    // 2 position, with one in center
    @Test (expected = AdjacentRestrictionException.class)
    public void AdjacentRestrictionFailure3Test() {
        assertTrue( voidBoard.insertDice(dieGeneric, 0, COLUMN_HigherBound) );
        voidBoard.insertDice(dieGeneric, ROW_HigherBound/2, COLUMN_HigherBound/2);
    }

    @Test
    public void AdjacentRestrictionOnlyTest() {
        assertTrue( testBoard.insertDice(dieGeneric, 0, 0, Board.Restriction.ADJACENT) );
        assertTrue( testBoard.insertDice(dieGeneric, 1, 1, Board.Restriction.ADJACENT) );
        assertTrue( testBoard.insertDice(dieGeneric, 1, 0, Board.Restriction.ADJACENT) );
        assertTrue( testBoard.insertDice(dieGeneric, 0, 1, Board.Restriction.ADJACENT) );

        assertTrue( voidBoard.insertDice(dieGeneric, 0, 0, Board.Restriction.ADJACENT) );
        assertTrue( voidBoard.insertDice(dieGeneric, 1, 1, Board.Restriction.ADJACENT) );
        assertTrue( voidBoard.insertDice(dieGeneric, 1, 0, Board.Restriction.ADJACENT) );
        assertTrue( voidBoard.insertDice(dieGeneric, 0, 1, Board.Restriction.ADJACENT) );
        assertTrue( voidBoard.insertDice(dieGeneric, 2, 2, Board.Restriction.ADJACENT) );
        assertTrue( voidBoard.insertDice(dieGeneric, 3, 3, Board.Restriction.ADJACENT) );
        assertTrue( voidBoard.insertDice(dieGeneric, 3, 4, Board.Restriction.ADJACENT) );
        assertTrue( voidBoard.insertDice(dieGeneric, 2, 4, Board.Restriction.ADJACENT) );
        assertTrue( voidBoard.insertDice(dieGeneric, 1, 4, Board.Restriction.ADJACENT) );
        assertTrue( voidBoard.insertDice(dieGeneric, 2, 1, Board.Restriction.ADJACENT) );
        assertTrue( voidBoard.insertDice(dieGeneric, 2, 3, Board.Restriction.ADJACENT) );
        assertTrue( voidBoard.insertDice(dieGeneric, 3, 2, Board.Restriction.ADJACENT) );
    }


//    @Test
    public void patternTest() {
        assertTrue( testBoard.insertDice(die1NotPurple, 0, 0) );
        assertTrue( testBoard.insertDice(dieNot1ButPurple, 1, 0) );
        assertTrue( testBoard.insertDice(dieNot1ButPurple, 0, 1) );     // we can place the same dice in the diagonal adjacent position
        assertTrue( testBoard.insertDice(die1Yellow, 1, 1) );
    }

    @Test
    public void boardTest() {
        patternTest();
        assertEquals(4, testBoard.getDiceQuantity());
        assertEquals( die1Yellow, testBoard.getDice(1, 1));

        Boolean bBoardCellOccupied = false;
        try { testBoard.validMove(dieGeneric, 1, 1);
        } catch (BoardCellOccupiedException e) {
            bBoardCellOccupied = true;
        }
        assertTrue( bBoardCellOccupied );
    }

    // do not care if this fails
    // be happy if this works
    @Test
    public void fillTest() {
        assertEquals(0, voidBoard.getDiceQuantity());

        for (int r = ROW_LowerBound; r <= ROW_HigherBound; r++) {
            for (int c = COLUMN_LowerBound; c <= COLUMN_HigherBound; c++) {
                Dice dieDifferent = new Dice(
                        (r+c)% MAX_DICE_NUMBER +1,
                        ColorDice.values()[(r+c) %COLUMN],
                        idCounter++ );
                assertTrue( voidBoard.insertDice(dieDifferent, r, c) );
            }
        }
        assertEquals(ROW * Board.COLUMN, voidBoard.getDiceQuantity());
        assertEquals(20, voidBoard.getDiceQuantity());


        System.out.println("\n\nNow, Print 3 boards: \n");

        voidBoard.print(GuiSettings.bFixedFont);
        System.out.println( voidBoard.toString() );
        System.out.println( voidBoard.toString(dieGeneric, 0, 0) );

        voidBoard.removeDice(ROW/2, COLUMN/2);
        System.out.println( voidBoard.toString(dieGeneric, ROW/2, COLUMN/2) );
    }


    // ***********************************
    // ********** Remove Test  ***********
    // ***********************************

    @Test
    public void removableTest() {
        // SUCCESS test
        patternTest();
        assertTrue( testBoard.canBeRemoved(0, 0) );

        testBoard = new Board(testPatternType);
        patternTest();
        assertTrue( testBoard.canBeRemoved(1, 1) );

        // FAILURE test
        assertTrue( voidBoard.insertDice(dieGeneric, 0, 1, Board.Restriction.NONE) );
        assertTrue( voidBoard.insertDice(dieGeneric, 1, 2, Board.Restriction.NONE) );
        assertFalse( voidBoard.canBeRemoved(0, 1) );

    }

    @Test
    public void forceRemoveTest() {
        patternTest();
        assertTrue( testBoard.insertDice(dieNot1ButPurple, 2, 2) );
        assertFalse( testBoard.canBeRemoved(1, 1) );
        assertNull( testBoard.removeDice(1, 1) );
    }
}
