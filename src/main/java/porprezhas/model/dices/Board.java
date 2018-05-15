package porprezhas.model.dices;

import java.util.ArrayList;
import porprezhas.model.dices.Board;

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


    public boolean insertDiceWithoutColorRestrictions(Dice dice, int x, int y) {

        if (validMoveWithoutColorRestrictions(dice, x, y)) {
            board[x][y] = dice;
            diceQuantity++;
            return true;
        }
        else
            return false;
    }


    public boolean insertDiceWithoutNumberRestrictions(Dice dice, int x, int y) {

        if (validMoveWithoutNumberRestrictions(dice, x, y)) {
            board[x][y] = dice;
            diceQuantity++;
            return true;
        }
        else
            return false;
    }

    public boolean insertDiceWithoutAdjacentRestrictions(Dice dice, int x, int y) {

        if (validMoveWithoutAdjacentRestrictions(dice, x, y)) {
            board[x][y] = dice;
            diceQuantity++;
            return true;
        }
        else
            return false;
    }


    public Dice removeDice(int x, int y){

        if(canBeRemoved(x,y)){
            board[x][y]=null;
            return getDice(x,y);
        }
        return null;
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

    public boolean validMoveWithoutColorRestrictions(Dice dice, int x, int y){

            if ((this.getPattern().getBox(x,y).freeBox())||(this.getPattern().getBox(x, y).getNumber() == dice.getDiceNumber())) {
                //Check if the box is already occupied
                if (this.occupiedBox(x, y))
                    return Boolean.FALSE;
                //Check if the die is adjacent to a previously placed die
                if (!this.adjacentDice(dice, x, y))
                    return false;
                //valid Move
                return Boolean.TRUE;
            }
        else
            return Boolean.FALSE;
    }


    public boolean validMoveWithoutNumberRestrictions(Dice dice, int x, int y){

        if ((this.getPattern().getBox(x,y).freeBox())||(!this.getPattern().getBox(x,y).white() && this.getPattern().getBox(x,y).getColor().equals(dice.colorDice))) {
            //Check if the box is already occupied
            if (this.occupiedBox(x, y))
                return Boolean.FALSE;
            //Check if the die is adjacent to a previously placed die
            if (!this.adjacentDice(dice, x, y))
                return false;
            //valid Move
            return Boolean.TRUE;
        }
        else
            return Boolean.FALSE;
    }

    public boolean validMoveWithoutAdjacentRestrictions(Dice dice, int x, int y){

        if ((this.getPattern().getBox(x,y).freeBox())||((!this.getPattern().getBox(x,y).white() && this.getPattern().getBox(x,y).getColor().equals(dice.colorDice))&&(!this.getPattern().getBox(x,y).white() && this.getPattern().getBox(x,y).getColor().equals(dice.colorDice)))) {
            //Check if the box is already occupied
            if (this.occupiedBox(x, y))
                return Boolean.FALSE;
            //valid Move
            return Boolean.TRUE;
        }
        else
            return Boolean.FALSE;
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


    Boolean[][] dummyBoard = new Boolean[4][5];


    //given a certain dice coordinates, it returns true if the dice can be removed from the board, without breaking the rules
    public boolean canBeRemoved(int x, int y){

        boolean flag=false;

        for(int i=0; i<4; i++){
            for(int j=0; j<5; j++){
                if(!flag) {
                    if (!((i == 1 && (0 < j && j < 4)) || (i == 2 && (0 < j && j < 4)))) {
                        if (occupiedBox(i, j)) {
                            dummyBoard[i][j] = true;
                            markCell(i, j, x, y);
                            flag=true;
                        }

                    }
                }
            }
        }

        for(int i=0;i<4;i++){
            for(int j=0;j<5;j++){

                if(i!=x||j!=y)
                    if(dummyBoard[i][j]==false&&occupiedBox(i,j))
                        return false;


            }
        }

        return true;



    }




    //this function marks the cells that are reachable from a certain cell
    public void markCell(int x,int y, int X, int Y){

        if(x>0&&y>0)
            if(occupiedBox(x-1,y-1)&&(x-1!=X||y-1!=Y)){
                dummyBoard[x-1][y-1]=true;
                markCell( x-1, y-1 ,X,Y);
            }

         if(x>0)
             if(occupiedBox(x-1,y)&&(x-1!=X||y!=Y)){
                dummyBoard[x-1][y]=true;
                markCell(x-1,y,X,Y);
             }

         if(x>0&&y<4)
             if(occupiedBox(x-1,y+1)&&(x-1!=X||y+1!=Y)){
                 dummyBoard[x-1][y+1]=true;
                 markCell(x-1,y+1,X,Y);
             }

         if(y>0)
             if(occupiedBox(x,y-1)&&(x!=X||y-1!=Y)){
                 dummyBoard[x][y-1]=true;
                 markCell(x,y-1,X,Y);
             }

         if(y<4)
             if(occupiedBox(x,y+1)&&(x!=X||y+1!=Y)){
                 dummyBoard[x][y+1]=true;
                 markCell(x,y+1,X,Y);
             }

         if(x<3&&y>0)
             if(occupiedBox(x+1,y-1)&&(x+1!=X||y-1!=Y)){
                 dummyBoard[x+1][y-1]=true;
                 markCell(x+1,y-1,X,Y);
             }

         if(x<3)
             if(occupiedBox(x+1,y)&&(x+1!=X||y!=Y)){
                 dummyBoard[x+1][y]=true;
                 markCell(x+1,y,X,Y);
             }

         if(x<3&&y<4)
             if(occupiedBox(x+1,y+1)&&(x+1!=X||y+1!=Y)){
                 dummyBoard[x+1][y+1]=true;
                 markCell(x+1,y+1,X,Y);
             }
     }


}
