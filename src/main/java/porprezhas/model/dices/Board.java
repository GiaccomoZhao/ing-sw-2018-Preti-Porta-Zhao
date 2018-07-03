package porprezhas.model.dices;

import porprezhas.Useful;
import porprezhas.exceptions.diceMove.*;

import java.io.Serializable;

import static porprezhas.Useful.appendSpaces;
import static porprezhas.Useful.isValueBetweenInclusive;
import static porprezhas.model.dices.CellPosition.*;

public class Board implements Serializable {

    public enum Restriction {
        NONE(0b000),
        COLOR(0b001),
        NUMBER(0b010),
        DICE(0b011),
        ADJACENT(0b100),
        ALL(0b111),
        WITHOUT_COLOR(0b110),    // same as ALL & ~COLOR
        WITHOUT_NUMBER(0b101),
        WITHOUT_ADJACENT(0b011);

        public int value;

        Restriction(int i) {
            value = i;
        }

        public boolean hasColorRestriction() {
            if (0 != (this.value & COLOR.value)) {
                return true;
            } else
                return false;
        }

        public boolean hasNumberRestriction() {
            if (0 != (this.value & NUMBER.value)) {
                return true;
            } else
                return false;
        }

        public boolean hasAdjacentRestriction() {
            if (0 != (this.value & ADJACENT.value)) {
                return true;
            } else
                return false;
        }

        public Restriction and(Restriction restriction) {
            for (Restriction r : Restriction.values()) {
                if (r.value == (this.value & restriction.value))
                    return r;
            }
            return NONE;
        }
    }



    private final Pattern pattern;
    private Dice[][] board;
    private int diceQuantity;
    public static final int ROW = 4;
    public static final int COLUMN = 5;


    public Board(Pattern.TypePattern typePattern) {
        this.pattern = new Pattern(typePattern);
        this.board = new Dice[ROW][COLUMN];
        this.diceQuantity = 0;
    }

    public Dice[][] getBoard() {
        return board;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public int getRow() {
        return ROW;
    }

    public int getColumn() {
        return COLUMN;
    }

    public int getHeight() {
        return ROW;
    }

    public int getWidth() {
        return COLUMN;
    }



    public Boolean isBoxOccupied(int row, int col) {

        if (board[row][col] == null)
            return false;
        else
            return true;
    }

    public Boolean compatibleDice(Dice dice1, Dice dice2) {
        return compatibleDice(dice1, dice2, Restriction.DICE);
    }

    //Return true if dice1 can be placed adjacent to dice2
    public Boolean compatibleDice(Dice dice1, Dice dice2, Restriction restriction) {

        if (restriction.hasNumberRestriction()) {
            if (!compatibleDiceWithNumberRestrictions(dice1, dice2)) {
                return false;
            }
        }

        if (restriction.hasColorRestriction()) {
            if (!compatibleDiceWithColorRestrictions(dice1, dice2)) {
                return false;
            }
        }

        return true;
    }

    //Return true if dice1 can be placed adjacent to dice2 ignoring number restrictions
    public Boolean compatibleDiceWithColorRestrictions(Dice dice1, Dice dice2) {
        if (dice1.getColorDice().equals(dice2.getColorDice()))
            return false;

        return true;
    }

    //Return true if dice1 can be placed adjacent to dice2 ignoring color restrictions
    public Boolean compatibleDiceWithNumberRestrictions(Dice dice1, Dice dice2) {
        if (dice1.getDiceNumber() == dice2.getDiceNumber())
            return false;

        return true;
    }


    public Boolean adjacentDice(Dice dice, int row, int col) {
        return adjacentDice(dice, row, col, Restriction.DICE);
    }

    public Boolean adjacentDice(Dice dice, int row, int col, Restriction restriction) {

        int aroundDiceCounter = 0;

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {

                // skip control on itself
                if (r == row && c == col) {
                    continue;
                } else {

                    if (isValueBetweenInclusive(r, MIN_ROW, MAX_ROW) &&
                            isValueBetweenInclusive(c, MIN_COLUMN, MAX_COLUMN)) {

                        if (isBoxOccupied(r, c)) {
                            aroundDiceCounter++;
                            if (r == row || c == col) {    // Orthogonal Adjacent
                                if (!compatibleDice(dice, board[r][c], restriction)) {
                                    return false;
                                }
                            } else {    // Diagonal Adjacent
                                ;       // just check that it exists, counter++
                            }
                        }
                    }
                }
            }
        }
        if (aroundDiceCounter > 0)
            return true;    // can place
        else
            return false;   // nobody around
    }


    /**
     * search the first not adjacent dice, and Return its Position if it exists
     * Used to find why the given Dice can not be placed in the given Position
     *
     * @param dice  the dice to check
     * @param row   the row position to check, where the specified dice will be able to place
     * @param col   the column position to check
     * @param restriction   check the constraint with some restriction, may be ignoring something
     *
     * @return  1. the first not adjacent dice Position: the given dice can NOT be placed here
     *          2. the given position:            the dice can be placed if at given position is not occurred
     *          3. null, means no dice is around: the dice can be placed if the given position is a border position and it is first place
     *
     * example use:
     *          CellPosition position = getNotAdjacentDicePosition(dice, row, col, Restriction.ALL);
     *          if(null != position) {
     *              if(null == board[position.getRow()] [position.getCol()]) {
     *                  szDescription = "has a Similar dice around";
     *              } else {
     *                  return true;    // can be placed
     *              }
     *
     *          // null == position
     *          } else {
     *              if(CellPosition.isBorderPosition(row, col) && diceQuantity == 0) {
     *                  return true;    // can be placed
     *              } else {
     *                  szDescription = "has NOT dice around";
     *              }
     *          }
     */
    public CellPosition getNotAdjacentDicePosition(Dice dice, int row, int col, Restriction restriction) {

        int aroundDiceCounter = 0;

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {

                // skip control on itself
                if (r == row && c == col) {
                    continue;
                } else {

                    if (    isValueBetweenInclusive(r, MIN_ROW,     MAX_ROW) &&
                            isValueBetweenInclusive(c, MIN_COLUMN,  MAX_COLUMN)) {

                        if (isBoxOccupied(r, c)) {
                            aroundDiceCounter++;
                            if (r == row || c == col) {    // Orthogonal Adjacent
                                if (!compatibleDice(dice, board[r][c], restriction)) {
                                    return new CellPosition (r, c);     // can NOT place for this position's Dice
                                }
                            } else {    // Diagonal Adjacent
                                ;       // just check that it exists, counter++
                            }
                        }
                    }
                }
            }
        }
        if (aroundDiceCounter > 0)
            return new CellPosition(row, col);    // you can place here if here is not occupied
        else
            return null;    // nobody is around
    }

    /**
     * Used to check the Adjacent constraint and
     * get the Information about the Not Adjacent Dice
     *
     * @param dice  the dice to check
     * @param row   the row position to check, where the specified dice will be able to place
     * @param col   the column position to check
     * @param restriction   check the constraint with some restriction, may be ignoring something
     *
     * @return  1. the first not adjacent Dice's object
     *          2. the occurred dice at given position in the board
     *          3. null, the given dice can be placed in the given position (row, col)
     *
     * @throws AdjacentRestrictionException  when you are trying the place the given dice directly
     *                                          in the center of board without a dice around
     */
    public Dice getNotAdjacentDice(Dice dice, int row, int col, Restriction restriction)
            throws AdjacentRestrictionException{

        int aroundDiceCounter = 0;

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {

                // skip control on center -itself-
                if (r == row && c == col) {
                    continue;
                } else {

                    if (isValueBetweenInclusive(r, MIN_ROW, MAX_ROW) &&
                            isValueBetweenInclusive(c, MIN_COLUMN, MAX_COLUMN)) {

                        if (isBoxOccupied(r, c)) {
                            aroundDiceCounter++;
                            if (r == row || c == col) {    // Orthogonal Adjacent
                                if (!compatibleDice(dice, board[r][c], restriction)) {
                                    return board[r][c];     // can NOT place for this position's Dice
                                }
                            } else {    // Diagonal Adjacent
                                ;       // just check that it exists, counter++
                            }
                        }
                    }
                }
            }
        }
        if (aroundDiceCounter > 0)
            return board[row][col];    // you can place here if here is not occupied -null-
        else {
            if( CellPosition.isBorderPosition(row, col) ) {
                return null;        // the given dice can be placed in the given position (row, col)
            } else
                throw new AdjacentRestrictionException("Can not insert a dice directly in the center of board");    // nobody is around
        }
    }


/*
    public Boolean aloneDice(Dice dice, int row, int col) {

        if (row > 0 && isBoxOccupied(row - 1, col))
            return false;
        if (row < 3 && isBoxOccupied(row + 1, col))
            return false;
        if (col > 0 && isBoxOccupied(row, col - 1))
            return false;
        if (col < 4 && isBoxOccupied(row, col + 1))
            return false;
        if (row > 0 && col > 0 && isBoxOccupied(row - 1, col - 1))
            return false;
        if (row < 3 && col > 0 && isBoxOccupied(row + 1, col - 1))
            return false;
        if (row > 0 && col < 4 && isBoxOccupied(row - 1, col + 1))
            return false;
        if (row < 3 && col < 4 && isBoxOccupied(row + 1, col + 1))
            return false;

        return true;
    }

    public Boolean adjacentDiceWithoutColorRestrictions(Dice dice, int row, int col){

        int counter=0;
        if(row>0 && isBoxOccupied(row-1, col) ){
            if(!compatibleDiceWithoutColorRestrictions(dice, board[row-1][col]))
                return false;
            else
                counter++;

        }
        if(row<3 && isBoxOccupied(row+1, col)){
            if(!compatibleDiceWithoutColorRestrictions(dice, board[row+1][col]))
                return false;
            else
                counter++;

        }
        if(col>0 && isBoxOccupied(row, col-1)){
            if(!compatibleDiceWithoutColorRestrictions(dice, board[row][col-1]))
                return false;
            else
                counter++;
        }
        if(col<4 && isBoxOccupied(row, col+1)){
            if(!compatibleDiceWithoutColorRestrictions(dice, board[row][col+1]))
                return false;
            else
                counter++;
        }

        if(counter>0)
            return true;

        if(row>0 && col>0 && isBoxOccupied(row-1, col-1))
            return true;
        if(row<3 && col>0 && isBoxOccupied(row+1, col-1))
            return true;
        if(row>0 && col<4 && isBoxOccupied(row-1, col+1))
            return true;
        if(row<3 && col<4 && isBoxOccupied(row+1, col+1))
            return true;

        return false;
    }



    public Boolean adjacentDiceWithoutNumberRestrictions(Dice dice, int row, int col){

        int counter=0;
        if(row>0 && isBoxOccupied(row-1, col) ){
            if(!compatibleDiceWithoutNumberRestrictions(dice, board[row-1][col]))
                return false;
            else
                counter++;

        }
        if(row<3 && isBoxOccupied(row+1, col)){
            if(!compatibleDiceWithoutNumberRestrictions(dice, board[row+1][col]))
                return false;
            else
                counter++;

        }
        if(col>0 && isBoxOccupied(row, col-1)){
            if(!compatibleDiceWithoutNumberRestrictions(dice, board[row][col-1]))
                return false;
            else
                counter++;
        }
        if(col<4 && isBoxOccupied(row, col+1)){
            if(!compatibleDiceWithoutNumberRestrictions(dice, board[row][col+1]))
                return false;
            else
                counter++;
        }

        if(counter>0)
            return true;

        if(row>0 && col>0 && isBoxOccupied(row-1, col-1))
            return true;
        if(row<3 && col>0 && isBoxOccupied(row+1, col-1))
            return true;
        if(row>0 && col<4 && isBoxOccupied(row-1, col+1))
            return true;
        if(row<3 && col<4 && isBoxOccupied(row+1, col+1))
            return true;

        return false;
    }
*/

/*
    public boolean insertDice(Dice dice, int row, int col) {

        if (validMove(dice, row, col)) {
            board[row][col] = dice;
            diceQuantity++;
            return true;
        }
        else
            return false;
    }
*/

    public boolean insertDice(Dice dice, int row, int col) {
        return insertDice(dice, row, col, Restriction.ALL);
    }

    public boolean insertDice(Dice dice, int row, int col, Restriction restriction) {
        if (validMove(dice, row, col, restriction)) {
            board[row][col] = dice;
            diceQuantity++;
            return true;
        } else
            return false;   // NOTE: invalid move always return exception, at moment
    }


    public Dice removeDice(int row, int col) {

        Dice auxDice;
//        if (canBeRemoved(row, col)) {
            auxDice = getDice(row, col);
            board[row][col] = null;
            diceQuantity--;
            return auxDice;
//        }
//        return null;
    }


    /*
        public boolean validMove(Dice dice, int row, int col){

            if(Useful.isValueBetweenInclusive(row, 0, ROW-1) &&
                    Useful.isValueBetweenInclusive(col, 0, COLUMN-1)) {

                //Check if the pattern constraint is respected by dice
                if (!pattern.getBox(row, col).checkConstraint(dice))
                    return Boolean.FALSE;

                // Check if dice is the first die of the player and if the position is an edge or corner space
                if (diceQuantity == 0) {
                    if (pattern.checkEdges(row, col))
                        return Boolean.TRUE;
                    else
                        return Boolean.FALSE;
                }

                //Check if the box is already occupied
                if (this.isBoxOccupied(row, col))
                    return Boolean.FALSE;

                //Check if the die is adjacent to a previously placed die

                if (!this.adjacentDice(dice, row, col))
                    return false;

                //valid Move
                return Boolean.TRUE;
            } else {
                throw new IndexOutOfBoundsException("row: 0 <= " +  row + " <= " + (ROW-1) + " \tcol:  0 <= " + col + "<= " + (COLUMN-1));
            }
        }
    */
    public boolean validMove(Dice dice, int row, int col) {
        return validMove(dice, row, col, Restriction.ALL);
    }


    public boolean validMove(Dice dice, int row, int col, Restriction restriction)
            throws IndexOutOfBoardBoundsException, // NotYourTurnException, AlreadyPickedException,
            BoardCellOccupiedException, EdgeRestrictionException, PatternColorRestrictionException, PatternNumericRestrictionException, AdjacentRestrictionException {
        // Check index bound
        if (!isValueBetweenInclusive(row, MIN_ROW, MAX_ROW) ||
                !isValueBetweenInclusive(col, MIN_COLUMN, MAX_COLUMN))
            throw new IndexOutOfBoardBoundsException( row, col );

        //Check if the box is already occupied
        if (this.isBoxOccupied(row, col))
            throw new BoardCellOccupiedException("\n" +
                    "The cell (" + row + "," + col + ") of board " + "\n" +   // board +
                    "has already been occupied by " + board[row][col] + "!"
            );

        //Check if the pattern constraint is respected by dice
        if (!pattern.getBox(row, col).checkConstraint(dice, restriction.and(Restriction.COLOR))) {
            throw new PatternColorRestrictionException(
                    "You must respect the Pattern Color constraint!\n" +
                            "You placed the " + dice + " " +
                            "in cell (" + row + "," + col + ")\n" +
                            "Pattern's cell constrain is Color: " +
                            (this.getPattern().getBox(row, col).getColor())
            );
        }

        if (!pattern.getBox(row, col).checkConstraint(dice, restriction.and(Restriction.NUMBER))) {
            throw new PatternNumericRestrictionException(
                    "You must respect the Pattern Numeric constraint!\n" +
                            "You placed the " + dice + " " +
                            "in cell (" + row + "," + col + ")\n" +
                            "Pattern's cell constrain is Number: " +
                            (this.getPattern().getBox(row, col).getNumber())
            );
        }

        // Check if dice is the first die of the player and if the position is an edge or corner space
        if (diceQuantity == 0) {

            if (pattern.checkEdges(row, col)) {
                return true;
            } else {
                throw new EdgeRestrictionException(
                        "First place should be in the edge's cell!\n" +
                                "You placed " + dice + " " +
                                "in cell (" + row + "," + col + ")"
                );
            }
        } else {

            // first insert has not Adjacent constriction
            if (restriction.hasAdjacentRestriction() &&
                    !this.adjacentDice(dice, row, col, restriction)) {

                CellPosition notAdjacentDicePosition = getNotAdjacentDicePosition(dice, row, col, Restriction.ALL);
//                Dice notAdjacentDice = getNotAdjacentDice(dice, row, col, Restriction.ALL);

                StringBuilder sbConstraint = new StringBuilder();
                if(null != notAdjacentDicePosition) {
                    Dice notAdjacentDice = board[notAdjacentDicePosition.getRow()] [notAdjacentDicePosition.getCol()];
                    if(null != notAdjacentDice) {
                        sbConstraint.append("has a dice with same ");
                        boolean bNumber = false;
                        if( dice.getDiceNumber() == notAdjacentDice.getDiceNumber() ) {
                            sbConstraint.append("NUMBER ");
                            bNumber = true;
                        }
                        if( dice.getColorDice().equals(notAdjacentDice.getColorDice())) {
                            if(bNumber)
                                sbConstraint.append("and ");
                            sbConstraint.append("COLOR ");
                        }
                    } else {
                        return true;    // can be placed
                    }

                // null == notAdjacentDicePosition: no dice is around
                } else {
                    if(CellPosition.isBorderPosition(row, col)  && diceQuantity == 0) {
                        return true;    // can be placed
                    } else {
                        sbConstraint.append( "is Isolate" );
                    }
                }

                throw new AdjacentRestrictionException(
                                "You placed the " + dice + " " +
                                "in cell (" + row + "," + col + ") that " + sbConstraint +": " + "\n" +
                                toString(dice, row, col)
                );
            }
        }

        //Valid Move
//        System.out.println("Mossa Valido!!!");
        return true;
    }


    public Dice getDice(int row, int col) {
        if (board[row][col] != null)
            return board[row][col];
        else
            return new Dice(Dice.ColorDice.WHITE, 0, -1);
    }

    public int getDiceQuantity() {
        return diceQuantity;
    }


    // save the date for recursion
    Boolean[][] dummyBoard = new Boolean[ROW][COLUMN];


    //given a certain dice positions, it returns true if the dice can be removed from the board, without breaking the rules
    public boolean canBeRemoved(int row, int col) {

        boolean flag = false;

        // initialize
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                dummyBoard[i][j] = Boolean.FALSE;
            }
        }


        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                if (!flag) {
                    //     one of inner Columns and all Rows
                    if (!((i == 1 && (Useful.isValueBetween(j, MIN_COLUMN, MAX_COLUMN))) || (i == 2 && Useful.isValueBetween(j, MIN_COLUMN, MAX_COLUMN)))) {
                        if (isBoxOccupied(i, j) && !(i == row && j == col)) {
                            dummyBoard[i][j] = true;
                            markCell(i, j, row, col);
                            flag = true;
                        }

                    }
                }
            }
        }


        for (int i = 0; i < ROW; i++) {

            for (int j = 0; j < COLUMN; j++) {
                if (!((i == row) && (j == col))) {
                    if ((!dummyBoard[i][j]) && (isBoxOccupied(i, j))) {

                        return false;

                    }


                }
            }
        }

        return true;


    }


    //this function "marks" the cells that are reachable from a certain cell
    public void markCell(int x, int y, int X, int Y) {

        if (x > 0 && y > 0)
            if (isBoxOccupied(x - 1, y - 1) && (x - 1 != X || y - 1 != Y)) {
                if (!dummyBoard[x - 1][y - 1]) {
                    dummyBoard[x - 1][y - 1] = true;
                    markCell(x - 1, y - 1, X, Y);
                }
            }

        if (x > 0)
            if (isBoxOccupied(x - 1, y) && (x - 1 != X || y != Y)) {
                if (!dummyBoard[x - 1][y]) {
                    dummyBoard[x - 1][y] = true;
                    markCell(x - 1, y, X, Y);
                }
            }

        if (x > 0 && y < 4)
            if (isBoxOccupied(x - 1, y + 1) && (x - 1 != X || y + 1 != Y)) {
                if (!dummyBoard[x - 1][y + 1]) {
                    dummyBoard[x - 1][y + 1] = true;
                    markCell(x - 1, y + 1, X, Y);
                }
            }

        if (y > 0)
            if (isBoxOccupied(x, y - 1) && (x != X || y - 1 != Y)) {
                if (!dummyBoard[x][y - 1]) {
                    dummyBoard[x][y - 1] = true;
                    markCell(x, y - 1, X, Y);
                }
            }

        if (y < 4)
            if (isBoxOccupied(x, y + 1) && (x != X || y + 1 != Y)) {
                if (!dummyBoard[x][y + 1]) {
                    dummyBoard[x][y + 1] = true;
                    markCell(x, y + 1, X, Y);
                }
            }

        if (x < 3 && y > 0)
            if (isBoxOccupied(x + 1, y - 1) && (x + 1 != X || y - 1 != Y)) {
                if (!dummyBoard[x + 1][y - 1]) {
                    dummyBoard[x + 1][y - 1] = true;
                    markCell(x + 1, y - 1, X, Y);
                }

            }

        if (x < 3)
            if (isBoxOccupied(x + 1, y) && (x + 1 != X || y != Y)) {
                if (!dummyBoard[x + 1][y]) {
                    dummyBoard[x + 1][y] = true;
                    markCell(x + 1, y, X, Y);
                }

            }

        if (x < 3 && y < 4) {
            if (isBoxOccupied(x + 1, y + 1) && (x + 1 != X || y + 1 != Y)) {
                if (!dummyBoard[x + 1][y + 1]) {
                    dummyBoard[x + 1][y + 1] = true;
                    markCell(x + 1, y + 1, X, Y);
                }
            }
        }
    }




//      ********************************************
//      ***************  >> PRINTS << **************
//      ********************************************


    // **
    // @Param  position    segment start position
    // @Param  size        segment length
    // @Param  lowerBound  lower limit
    // @Param  higherBound high limit
    // @Return dimension   of the segment allowed in the bound at given position
    //
    /* @requires position > 0  &&  size > 0  &&
     *           lowerBound > 0 && higherBound > 0  &&
     *           lowerBound < higherBound  &&  lowerBound > 0  &&  higherBound > 0
     * @ensure dimension <= size  &&
     *         isValueBetweenInclusive(position + dimension, lowerBound, higherBound)
     */
    private int calculateDimension(int position, int size, int lowerBound, int higherBound) {
        int dimension   =   size;
        if(higherBound - position < size) {    // row dimension is over the higher bound
            dimension = higherBound - position;
        }
        if(lowerBound - position > 0) {      // row is below lower bound
            dimension -= lowerBound - position;
        }
        return dimension;
    }


    // Build a board-only message
    @Override
    public String toString() {
        int dimensionRow    =   ROW;
        int dimensionColumn =   COLUMN;

//        String exampleString = new Dice(1, Dice.ColorDice.GREEN, -1).toString();
        String exampleString = new Dice(1, Dice.ColorDice.GREEN, -1).toString();
        String emptyDiceString = "Dice{empty}";
        int diceStringMaxLength = Integer.max(exampleString.length(), emptyDiceString.length());

        String diceString;
        StringBuilder  sbBoard = new StringBuilder("Board[" + dimensionRow + "x" + dimensionColumn + "]: \n" );

        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COLUMN; c++) {

                if (isValueBetweenInclusive(r, MIN_ROW, MAX_ROW) &&
                        isValueBetweenInclusive(c, MIN_COLUMN, MAX_COLUMN)) {

                    sbBoard.append("  ");

                    // get dice string
                    if (isBoxOccupied(r, c)) {
                        diceString = board[r][c].toString();

                    } else {
                        diceString = emptyDiceString;
                    }

                    // add dice string
                    sbBoard.append(diceString);
                    // tabulation
                    Useful.appendSpaces(sbBoard, diceStringMaxLength - diceString.length() +2);
                }
            }
            sbBoard.append("\n");
        }
        return sbBoard.toString();
    }


    // Build a board-only message
    // around the dice at position (row,col)
    // for adjacent constraint message
    public String toString(Dice dice, int row, int col) {
        int dimensionRow    =   calculateDimension(row-1, 3, 0, ROW);
        int dimensionColumn =   calculateDimension(col-1, 3, 0, COLUMN);

        CellPosition notAdjacentPosition;
        if(dice != null)
            notAdjacentPosition = getNotAdjacentDicePosition(dice, row, col, Restriction.ALL);
        else
            notAdjacentPosition = new CellPosition(row, col);

//        String exampleDiceString = new Dice(1, Dice.ColorDice.PURPLE).toString();   // take the longest dice string, at moment are all the same
        String exampleDiceString = "  Dice{empty}  ";   // take the longest dice string
        String strDice;
        StringBuilder  sbBoard = new StringBuilder("Board[" + dimensionRow + "x" + dimensionColumn + "] at (row:col)=(" + row + ":" + col + "): \n" );

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {

                // evidence the dice at position
                if (r == row && c == col) {
                    if (isBoxOccupied(r, c)) {
                        strDice = " <" + board[r][c] + "> ";
                    } else
                        strDice = ">>" + dice + "<<";

                } else {

                    if (isValueBetweenInclusive(r, MIN_ROW, MAX_ROW) &&
                            isValueBetweenInclusive(c, MIN_COLUMN, MAX_COLUMN)) {

                        if (isBoxOccupied(r, c)) {
                            if(notAdjacentPosition.equals(r, c)) {
                                strDice = "!!" + board[r][c].toString() + "!!";
                            } else {
                                strDice = "  " + board[r][c].toString() + "  ";
                            }
                        } else {
                            strDice = "  Dice{empty}  ";
                        }
                    } else {
//                        strDice = "";   // print nothing on outbound spaces
                        strDice = "Out of Bound ";
                    }
                }

//                sbBoard.append(" ");
                int lowHalf = (exampleDiceString.length() - strDice.length()) /2;
                int highHalf = exampleDiceString.length() - strDice.length() - lowHalf;

                sbBoard = appendSpaces(sbBoard, lowHalf);
                sbBoard.append(strDice);
                sbBoard = appendSpaces(sbBoard, highHalf);
//                sbBoard.append(" ");

            }
            sbBoard.append("\n");
        }
        return sbBoard.toString();
    }

    // print board + pattern
    // the print quality strongly depends by the font of the terminal:
    // @Param bFixedFont = true if the terminal has fixed size for every character!
    public boolean print(boolean bFixedFont) {
        // get information
        Pattern pattern = getPattern();
        char number;
        char color;

        // start printing
        //        ╔╗╚════║║║╝
        //        ╧ ╤ ╟┼╢──│

        // print TOP border
        if (bFixedFont) {
            System.out.print("╔══");
        } else {
            System.out.print("╔══");
        }
        for (int i = 0; i < this.getWidth() - 1; i++) {
            if (bFixedFont) {
                System.out.print("══╤══");
            } else {
                System.out.print("═══╤══");
            }
        }
        if (bFixedFont)
            System.out.println("══╗");
        else
            System.out.println("═══╗");


        // print all board and pattern LINEs
        for (int row = 0; row < this.getHeight(); row++) {

            // FIRST Line
            // print a part of LEFT border
            System.out.print("║");

            //       *****************
            // print **>>> BOARD <<<**
            for (int col = 0; col < this.getWidth(); col++) {
                // print DICE
                Dice dice = this.getDice(row, col);
                number = (char) ('0' + dice.getDiceNumber());
                color = dice.getColorDice().name().charAt(0);
                if (number == '0')
                    number = ' ';
                if (color == 'W')
                    color = ' ';
                System.out.format(" %c%C ", number, color);

                // print mid column separator
                if (col != this.getWidth() - 1) {
                    // switching between large and small separator to adapt the width size to 2 normal char + a space
                    if (col % 2 == 0) {
                        System.out.print("|");
                    } else {
                        System.out.print("│");
                    }
                }
            }
            System.out.println("║");

            // SECOND Line
            // print a part of LEFT border
            System.out.print("║");

            // print **>>> PATTERN <<<**
            //       *******************
            for (int x = 0; x < pattern.getWidth(); x++) {
                //
                Box box = pattern.getBox(row, x);
                number = (char) ('0' + box.getNumber());
                color = box.getColor().name().toLowerCase().charAt(0);
                if (number == '0')
                    number = ' ';
                if (color == 'w')
                    color = ' ';
                System.out.format(" %c%c ", number, color);

                // print mid column separator
                if (x != this.getWidth() - 1) {
                    // switching between large and small separator to adapt the width size to 2 normal char + a space
                    if (x % 2 == 0) {
                        System.out.print("|");
                    } else {
                        System.out.print("│");
                    }
                }
            }

            // print a part of RIGHT border
            System.out.println("║");

            // print horizontal separator + left and right border
            if (row != this.getHeight() - 1) {
                System.out.print("╟──");
                for (int x = 0; x < this.getWidth() - 1; x++) {
                    if (bFixedFont) {
                        System.out.print("──┼──");
                    } else {
                        System.out.print("───┼──");
                    }
                }
                if (bFixedFont) {
                    System.out.println("──╢");
                } else {
                    System.out.println("───╢");
                }
            }
        }

        // print BOTTOM border
        if (bFixedFont)
            System.out.print("╚══");
        else
            System.out.print("╚══");
        for (int i = 0; i < this.getWidth() - 1; i++) {
            if (bFixedFont)
                System.out.print("══╧══");
            else
                System.out.print("═══╧══");
        }
        if (bFixedFont)
            System.out.println("══╝");
        else
            System.out.println("═══╝");

        return true; // printed successfully
    }

}
