package porprezhas.model.dices;

import java.util.Random;

import static porprezhas.model.dices.Board.ROW;
import static porprezhas.model.dices.Board.COLUMN;

public class CellPosition {
    public static final int MIN_ROW = 0;
    public static final int MIN_COLUMN = 0;
    public static final int MAX_ROW = ROW - 1;
    public static final int MAX_COLUMN = Board.COLUMN - 1;

    // Corner positions
    public static final CellPosition topLeftPosition = new CellPosition(MIN_ROW, MIN_COLUMN);
    public static final CellPosition bottomRightPosition = new CellPosition(MAX_ROW, MAX_COLUMN);
    public static final CellPosition topRightPosition = new CellPosition(MIN_ROW, MAX_COLUMN);
    public static final CellPosition bottomLeftPosition = new CellPosition(MAX_ROW, MIN_COLUMN);


    private int row;
    private int col;

    public enum Bound {
        TOP, BOTTOM, LEFT, RIGHT,
    }

    public CellPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean equals(int row, int col) {
        if (this.row == row && this.col == col) {
            return true;
        } else
            return false;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public CellPosition setRow(int row) {
        this.row = row;
        return this;
    }

    public CellPosition setCol(int col) {
        this.col = col;
        return this;
    }

    public static boolean isBorderPosition(int row, int col){
        if(     row == MIN_ROW    ||  row == MAX_ROW  ||
                col == MIN_COLUMN ||  col == MAX_COLUMN )
            return true;
        else
            return false;
    }

    public static CellPosition getRandomInnerValue(long seed) {
        Random seededRandom = new Random(seed);
        CellPosition position = new CellPosition(
                seededRandom.nextInt(MAX_ROW - MIN_ROW - 2) + 1,
                seededRandom.nextInt(MAX_COLUMN - MIN_COLUMN - 2) + 1);
        return position;
    }

    public static CellPosition getRandomBorderValue(long seed) {
        Random seededRandom = new Random(seed);
        Bound bound = Bound.values()[seededRandom.nextInt(Bound.values().length)];

        return getRandomBorderValue(seededRandom.nextLong(), bound);
    }

    public static CellPosition getRandomBorderValue(long seed, Bound bound) {
        return getRandomBorderValue(seed, bound, false);
    }
    public static CellPosition getRandomBorderValue(long seed, Bound bound, boolean bOnlyInner) {
        Random seededRandom = new Random(seed);
        int row = MIN_ROW;      // initialize with a default value, this will be changed for 100%
        int col = MIN_COLUMN;

        int anchorNumber = bOnlyInner? 2 : 0;
        int anchorOffset = bOnlyInner? 1 : 0;

        switch (bound) {
            // TOP
            case TOP:
                row = seededRandom.nextInt(ROW - anchorNumber) + anchorOffset;
                col = MIN_COLUMN;
                break;
            // BOTTOM
            case BOTTOM:
                row = seededRandom.nextInt(ROW - anchorNumber) + anchorOffset;
                col = MAX_COLUMN;
                break;
            // LEFT
            case LEFT:
                row = MIN_ROW;
                col = seededRandom.nextInt(COLUMN - anchorNumber) + anchorOffset;
                break;
            // RIGHT
            case RIGHT:
                row = MAX_ROW;
                col = seededRandom.nextInt(COLUMN - anchorNumber) + anchorOffset;
                break;
        }
        return new CellPosition(row, col);
    }
}
