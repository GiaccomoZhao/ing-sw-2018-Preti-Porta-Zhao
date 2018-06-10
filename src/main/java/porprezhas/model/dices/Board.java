package porprezhas.model.dices;

import porprezhas.Useful;
import porprezhas.exceptions.diceMove.*;

import java.io.Serializable;

public class Board implements Serializable {

    public enum Restriction {
        NONE    (0b000),
        COLOR   (0b001),
        NUMBER  (0b010),
        DICE    (0b011),
        ADJACENT(0b100),
        ALL     (0b111),
        WITHOUT_COLOR   (0b110),    // same as ALL & ~COLOR
        WITHOUT_NUMBER  (0b101),
        WITHOUT_ADJACENT(0b011);

        public int value;

        Restriction(int i) {
            value = i;
        }

        public boolean hasColorRestriction() {
            if( 0 != (this.value & COLOR.value) ) {
                return true;
            } else
                return false;
        }

        public boolean hasNumberRestriction() {
            if( 0 != (this.value & NUMBER.value) ) {
                return true;
            } else
                return false;
        }

        public boolean hasAdjacentRestriction() {
            if( 0 != (this.value & ADJACENT.value) ) {
                return true;
            } else
                return false;
        }
    }

    private final Pattern pattern;
    private Dice[][] board;
    private int diceQuantity;
    public static final int ROW = 4;
    public static final int COLUMN = 5;


    public Board(Pattern.TypePattern typePattern) {
        this.pattern = new Pattern (typePattern);
        this.board = new Dice[ROW][COLUMN];
        this.diceQuantity=0;
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

    public Boolean occupiedBox(int row, int col){

        if (board[row][col]==null)
            return false;
        else
            return true;
    }

    //Return true if dice1 can be placed adjacent to dice2
    public Boolean compatibleDice(Dice dice1, Dice dice2){

        if(dice1.getColorDice().equals(dice2.getColorDice()))
            return false;
        if(dice1.getDiceNumber()==dice2.getDiceNumber())
            return false;

        return true;
    }

    //Return true if dice1 can be placed adjacent to dice2 ignoring color restrictions
    public Boolean compatibleDiceWithoutColorRestrictions(Dice dice1, Dice dice2){

        if(dice1.getDiceNumber()==dice2.getDiceNumber())
            return false;

        return true;
    }

    //Return true if dice1 can be placed adjacent to dice2 ignoring number restrictions
    public Boolean compatibleDiceWithoutNumberRestrictions(Dice dice1, Dice dice2){

        if(dice1.getColorDice()==dice2.getColorDice())
            return false;

        return true;

    }

    public Boolean adjacentDice(Dice dice, int row, int col){

        int counter=0;
        if(row>0 && occupiedBox(row-1, col) ){
            if(!compatibleDice(dice, board[row-1][col]))
                return false;
            else
                counter++;

        }
        if(row<3 && occupiedBox(row+1, col)){
            if(!compatibleDice(dice, board[row+1][col]))
                return false;
            else
                counter++;

        }
        if(col>0 && occupiedBox(row, col-1)){
            if(!compatibleDice(dice, board[row][col-1]))
                return false;
            else
                counter++;
        }
        if(col<4 && occupiedBox(row, col+1)){
            if(!compatibleDice(dice, board[row][col+1]))
                return false;
            else
                counter++;
        }

        if(counter>0)
            return true;

        if(row>0 && col>0 && occupiedBox(row-1, col-1))
            return true;
        if(row<3 && col>0 && occupiedBox(row+1, col-1))
            return true;
        if(row>0 && col<4 && occupiedBox(row-1, col+1))
            return true;
        if(row<3 && col<4 && occupiedBox(row+1, col+1))
            return true;

        return false;
    }

    public Boolean aloneDice(Dice dice, int row, int col) {

        if(row>0 && occupiedBox(row-1, col) )
            return false;
        if(row<3 && occupiedBox(row+1, col))
            return false;
        if(col>0 && occupiedBox(row, col-1))
            return false;
        if(col<4 && occupiedBox(row, col+1))
            return false;
        if(row>0 && col>0 && occupiedBox(row-1, col-1))
            return false;
        if(row<3 && col>0 && occupiedBox(row+1, col-1))
            return false;
        if(row>0 && col<4 && occupiedBox(row-1, col+1))
            return false;
        if(row<3 && col<4 && occupiedBox(row+1, col+1))
            return false;

        return true;
    }

    public Boolean adjacentDiceWithoutColorRestrictions(Dice dice, int row, int col){

        int counter=0;
        if(row>0 && occupiedBox(row-1, col) ){
            if(!compatibleDiceWithoutColorRestrictions(dice, board[row-1][col]))
                return false;
            else
                counter++;

        }
        if(row<3 && occupiedBox(row+1, col)){
            if(!compatibleDiceWithoutColorRestrictions(dice, board[row+1][col]))
                return false;
            else
                counter++;

        }
        if(col>0 && occupiedBox(row, col-1)){
            if(!compatibleDiceWithoutColorRestrictions(dice, board[row][col-1]))
                return false;
            else
                counter++;
        }
        if(col<4 && occupiedBox(row, col+1)){
            if(!compatibleDiceWithoutColorRestrictions(dice, board[row][col+1]))
                return false;
            else
                counter++;
        }

        if(counter>0)
            return true;

        if(row>0 && col>0 && occupiedBox(row-1, col-1))
            return true;
        if(row<3 && col>0 && occupiedBox(row+1, col-1))
            return true;
        if(row>0 && col<4 && occupiedBox(row-1, col+1))
            return true;
        if(row<3 && col<4 && occupiedBox(row+1, col+1))
            return true;

        return false;
    }

    public Boolean adjacentDiceWithoutNumberRestrictions(Dice dice, int row, int col){

        int counter=0;
        if(row>0 && occupiedBox(row-1, col) ){
            if(!compatibleDiceWithoutNumberRestrictions(dice, board[row-1][col]))
                return false;
            else
                counter++;

        }
        if(row<3 && occupiedBox(row+1, col)){
            if(!compatibleDiceWithoutNumberRestrictions(dice, board[row+1][col]))
                return false;
            else
                counter++;

        }
        if(col>0 && occupiedBox(row, col-1)){
            if(!compatibleDiceWithoutNumberRestrictions(dice, board[row][col-1]))
                return false;
            else
                counter++;
        }
        if(col<4 && occupiedBox(row, col+1)){
            if(!compatibleDiceWithoutNumberRestrictions(dice, board[row][col+1]))
                return false;
            else
                counter++;
        }

        if(counter>0)
            return true;

        if(row>0 && col>0 && occupiedBox(row-1, col-1))
            return true;
        if(row<3 && col>0 && occupiedBox(row+1, col-1))
            return true;
        if(row>0 && col<4 && occupiedBox(row-1, col+1))
            return true;
        if(row<3 && col<4 && occupiedBox(row+1, col+1))
            return true;

        return false;
    }


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
        }
        else
            return false;   // NOTE: invalid move always return exception, at moment
    }



    public Dice removeDice(int row, int col){

        Dice auxDice;
        if(canBeRemoved(row,col)){
            auxDice=getDice(row,col);
            board[row][col]=null;
            return auxDice;
        }
        return null;
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
            if (this.occupiedBox(row, col))
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
            throws IndexOutOfBoundsException, // NotYourTurnException, AlreadyPickedException,
            BoardCellOccupiedException, EdgeRestrictionException, ColorRestrictionException, NumberRestrictionException, AdjacentRestrictionException
    {
        // Check index bound
        if (!Useful.isValueBetweenInclusive(row, 0, ROW - 1) ||
                !Useful.isValueBetweenInclusive(col, 0, COLUMN - 1))
            throw new IndexOutOfBoundsException(
                    "\n" +
                    (row < 0 ? "   Row value is too low!\t" : row > (ROW - 1)  ? "   Row value is too high!\t" : "") +
                            "\trow: \t0 <= " + row + " <= " + (ROW-1) + "\n" +
                    (col < 0 ? "Column value is too low!\t" : col > (COLUMN-1) ? "Column value is too high!\t" : "") +
                            "\tcolumn: \t0 <= " + col + " <= " + (COLUMN-1) + "\n"
            );

        //Check if the box is already occupied
        if (this.occupiedBox(row, col))
            throw new BoardCellOccupiedException(
                    "The cell (" + row + "," + col + ") of board " +    // board +
                            "has already been occupied by " + board[row][col] + "!"
            );

        //Check if the pattern constraint is respected by dice
        if (!pattern.getBox(row, col).checkConstraint(dice, Restriction.COLOR)) {
            throw new ColorRestrictionException(
                    "You must respect the Color constraint!\n" +
                            "You placed the " + dice + " " +
                            "in cell (" + row + "," + col + ")\n" +
                            "Pattern's cell constrain is Color: " +
                            (this.getPattern().getBox(row, col).getColor())
            );
        }

        if (!pattern.getBox(row, col).checkConstraint(dice, Restriction.NUMBER)) {
            throw new NumberRestrictionException(
                    "You must respect the Numeric constraint!\n" +
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
            }
            else {
                System.err.println("dice quantity = "  + diceQuantity);
                throw new EdgeRestrictionException(
                        "First place should be in the edge's cell!\n" +
                        "You placed " + dice + " " +
                                "in cell (" + row + "," + col + ")"
                );
            }
        } else {

            // first insert has not Adjacent constriction
            if (restriction.hasAdjacentRestriction() &&
                    !this.adjacentDiceWithoutNumberRestrictions(dice, row, col)) {
                throw new AdjacentRestrictionException(
                        "You must respect the Adjacent constraint!\n" +
                        "You placed the " + dice + " " +
                                "in cell (" + row + "," + col + ") that is isolate\n"
                );
            }
        }

        //Valid Move
//        System.out.println("Mossa Valido!!!");
        return true;
    }



    public Dice getDice(int row, int col){
        if(board[row][col]!=null)
            return board[row][col];
        else
            return new Dice(Dice.ColorDice.WHITE, 0);

    }

    public int getDiceQuantity() {
        return diceQuantity;
    }

    public void setDiceQuantity(int diceQuantity) {
        this.diceQuantity = diceQuantity;
    }


    Boolean[][] dummyBoard = new Boolean[ROW][COLUMN];


    //given a certain dice coordinates, it returns true if the dice can be removed from the board, without breaking the rules
    public boolean canBeRemoved(int row, int col){

        boolean flag=false;

        for(int i=0;i<ROW;i++){
            for(int j=0;j<COLUMN;j++){
                dummyBoard[i][j]=Boolean.FALSE;
            }
        }


        for(int i=0; i<ROW; i++){
            for(int j=0; j<COLUMN; j++){
                if(!flag) {
                    if (!((i == 1 && (0 < j && j < COLUMN-1)) || (i == 2 && (0 < j && j < COLUMN-1)))) {
                        if (occupiedBox(i, j)&&!(i==row&&j==col)) {
                            dummyBoard[i][j] = true;
                            markCell(i, j, row, col);
                            flag=true;
                        }

                    }
                }
            }
        }


        for(int i=0;i<ROW;i++){

            for(int j=0;j<COLUMN;j++){
                if(!((i==row)&&(j==col))) {
                    if ((!dummyBoard[i][j])&&(occupiedBox(i, j))){

                        return false;

                    }


                }
            }
        }

        return true;



    }




    //this function "marks" the cells that are reachable from a certain cell
    public void markCell(int x,int y, int X, int Y){

        if(x>0&&y>0)
            if(occupiedBox(x-1,y-1)&&(x-1!=X||y-1!=Y)){
                if(!dummyBoard[x-1][y-1]){
                    dummyBoard[x-1][y-1]=true;
                    markCell( x-1, y-1 ,X,Y);
                }
            }

         if(x>0)
             if(occupiedBox(x-1,y)&&(x-1!=X||y!=Y)){
                 if(!dummyBoard[x-1][y]) {
                     dummyBoard[x - 1][y] = true;
                     markCell(x - 1, y, X, Y);
                 }
             }

         if(x>0&&y<4)
             if(occupiedBox(x-1,y+1)&&(x-1!=X||y+1!=Y)){
                 if(!dummyBoard[x-1][y+1]) {
                     dummyBoard[x - 1][y + 1] = true;
                     markCell(x - 1, y + 1, X, Y);
                 }
             }

         if(y>0)
             if(occupiedBox(x,y-1)&&(x!=X||y-1!=Y)){
                 if(!dummyBoard[x][y-1]){
                     dummyBoard[x][y-1]=true;
                     markCell(x,y-1,X,Y);
                 }
             }

         if(y<4)
             if(occupiedBox(x,y+1)&&(x!=X||y+1!=Y)){
                 if(!dummyBoard[x][y+1]) {
                     dummyBoard[x][y + 1] = true;
                     markCell(x, y + 1, X, Y);
                 }
             }

         if(x<3&&y>0)
             if(occupiedBox(x+1,y-1)&&(x+1!=X||y-1!=Y)){
                 if(!dummyBoard[x+1][y-1]){
                     dummyBoard[x+1][y-1]=true;
                     markCell(x+1,y-1,X,Y);
                 }

             }

         if(x<3)
             if(occupiedBox(x+1,y)&&(x+1!=X||y!=Y)){
                 if(!dummyBoard[x+1][y]){
                     dummyBoard[x+1][y]=true;
                     markCell(x+1,y,X,Y);
                 }

             }

         if(x<3&&y<4) {
             if (occupiedBox(x + 1, y + 1) && (x + 1 != X || y + 1 != Y)) {
                 if(!dummyBoard[x+1][y+1]){
                     dummyBoard[x + 1][y + 1] = true;
                     markCell(x + 1, y + 1, X, Y);
                 }
             }
         }
     }

     // for TEST use, now that we haven't view yet.
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
         if(bFixedFont) {
             System.out.print("╔══");
         } else {
             System.out.print("╔══");
         }
         for (int i = 0; i < this.getWidth() - 1; i++) {
             if(bFixedFont) {
                 System.out.print("══╤══");
             } else {
                 System.out.print("═══╤══");
             }
         }
         if(bFixedFont)
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
                 if(number == '0')
                     number = ' ';
                 if(color == 'W')
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
                 if(number == '0')
                     number = ' ';
                 if(color == 'w')
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
                     if(bFixedFont) {
                         System.out.print("──┼──");
                     } else {
                         System.out.print("───┼──");
                     }
                 }
                 if(bFixedFont) {
                     System.out.println("──╢");
                 } else {
                     System.out.println("───╢");
                 }
             }
         }

         // print BOTTOM border
         if(bFixedFont)
             System.out.print("╚══");
         else
             System.out.print("╚══");
         for (int i = 0; i < this.getWidth() - 1; i++) {
             if(bFixedFont)
                 System.out.print("══╧══");
             else
                 System.out.print("═══╧══");
         }
         if(bFixedFont)
             System.out.println("══╝");
         else
             System.out.println("═══╝");

         return true; // printed successfully
     }

}
