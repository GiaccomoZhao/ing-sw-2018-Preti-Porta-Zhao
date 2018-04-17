package PorPreZha.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {

    private final Pattern pattern;
    private Dice[][] board;
    private int quantity;

    public Board(Pattern pattern) {
        this.pattern = pattern;
        this.board = new Dice[5][4];
        this.quantity=0;
    }

    public ArrayList<Point> move(Dice dice){
        ArrayList<Point> validMoves;
        if(quantity==0){
            validMoves=new ArrayList<Point>(14);
            int i=0;
            for(i=0; i < 5; i++ ) {
                if(pattern.getBox(i, 0).freeBox() || (!pattern.getBox(i, 0).white() && pattern.getBox(i, 0).getColor().equals(dice.colorDice) ) ||
                        (!pattern.getBox(i, 0).noNumber() && pattern.getBox(i, 0).getNumber()==(dice.getDiceNumber())) )
                    validMoves.add(new Point(i, 0));
                if(pattern.getBox(i, 3).freeBox() )
                    validMoves.add(new Point(i, 3));

            }
            for(i=0; i < 4; i++ ) {
                if(pattern.getBox(0, i).freeBox())
                    validMoves.add(new Point(0, i));
                if(pattern.getBox(4, i).freeBox() )
                    validMoves.add(new Point(4, i));

            }
            return validMoves;
        }
        validMoves=new ArrayList<Point>();
        return validMoves;
    }



    public boolean insertDice(Dice dice, Point point){
        //Sposta controllo in player
        if(this.move(dice).contains(point)) {
            this.board[point.getX()][point.getY()] = dice;
            quantity++;
            return true;
        }
        return false;
    }

}
