package porprezha.model.cards;

import porprezha.model.dices.Dice;

public class Board {

    private final Pattern pattern;
    private Dice[][] board;
    private int diceQuantity;

    public Board(Pattern pattern) {

        this.pattern = pattern;
        this.board = new Dice[4][5];
        this.diceQuantity=0;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Boolean occupiedBox(int x, int y){

        if (board[x][y]==null)
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

    public Boolean adjacentDice(Dice dice, int x, int y){

        int counter=0;
        if(x>0 && occupiedBox(x-1, y) ){
            if(!compatibleDice(dice, board[x-1][y]))
                return false;
            else
                counter++;

        }
        if(x<3 && occupiedBox(x+1, y)){
            if(!compatibleDice(dice, board[x+1][y]))
                return false;
            else
                counter++;

        }
        if(y>0 && occupiedBox(x, y-1)){
            if(!compatibleDice(dice, board[x][y-1]))
                return false;
            else
                counter++;
        }
        if(y<4 && occupiedBox(x, y+1)){
            if(!compatibleDice(dice, board[x][y+1]))
                return false;
            else
                counter++;
        }

        if(counter>0)
            return true;

        if(x>0 && y>0 && occupiedBox(x-1, y-1))
            return true;
        if(x<3 && y>0 && occupiedBox(x+1, y-1))
            return true;
        if(x>0 && y<4 && occupiedBox(x-1, y+1))
            return true;
        if(x<3 && y<4 && occupiedBox(x+1, y+1))
            return true;

        return false;
    }





    public boolean insertDice(Dice dice, int x, int y) {

        if (validMove(dice, x, y)) {
            board[x][y] = dice;
            diceQuantity++;
            return true;
        }
        else
            return false;
    }

    public boolean validMove(Dice dice, int x, int y){

        //Check if the pattern constraint is respected by dice
       if(!pattern.getBox(x, y).checkCostraint(dice) )
           return Boolean.FALSE ;

        // Check if dice is the first die of the player and if the position is an edge or corner space
        if(diceQuantity==0 ){
            if(pattern.checkEdges(x, y))
                return Boolean.TRUE;
            else
                return Boolean.FALSE;
        }

        //Check if the box is already occupied
        if( this.occupiedBox(x, y))
            return Boolean.FALSE;

        //Check if the die is adjacent to a previously placed die

        if(!this.adjacentDice(dice, x, y))
            return false;

        //valid Move
        return Boolean.TRUE;

    }

    public Dice getDice(int x, int y){
        if(board[x][y]!=null)
            return board[x][y];
        else
            return new Dice(Dice.ColorDice.WHITE, 0);

    }

    public int getDiceQuantity() {
        return diceQuantity;
    }
}
