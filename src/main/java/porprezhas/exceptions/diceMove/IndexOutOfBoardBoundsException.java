package porprezhas.exceptions.diceMove;

import static porprezhas.model.dices.CellPosition.*;

public class IndexOutOfBoardBoundsException extends RuntimeException {
    public IndexOutOfBoardBoundsException(String message) {
        super(message);
    }

    public IndexOutOfBoardBoundsException(int row, int col) {
        super("\n" +
                (row < MIN_ROW ? "   Row value is too low! \t" : row > (MAX_ROW) ?
                                 "   Row value is too high!\t" :
                                 "                         \t") +
                "\t   row: \t" + MIN_ROW    + " <= " + row + " <= " + (MAX_ROW) + "\n" +

                (col < MIN_COLUMN ? "Column value is too low! \t" : col > (MAX_COLUMN) ?
                                 "Column value is too high!\t" :
                                 "                         \t") +
                "\tcolumn: \t" + MIN_COLUMN + " <= " + col + " <= " + (MAX_COLUMN) + "\n"
        );
    }
}
