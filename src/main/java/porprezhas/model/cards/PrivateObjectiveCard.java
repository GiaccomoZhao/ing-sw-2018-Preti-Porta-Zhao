package porprezhas.model.cards;
import porprezhas.model.dices.Board;
import porprezhas.model.dices.Dice;

import java.io.Serializable;

import static porprezhas.model.dices.Board.COLUMN;
import static porprezhas.model.dices.Board.ROW;

public class PrivateObjectiveCard  extends ObjectiveCard implements Serializable {

    public PrivateObjectiveCard(Effect effect) {

        super(effect);

    }


    /**
     * Apply the private Objective card to the player's board and return the point gained
     *
     * @param board board of the player
     * @return
     */

    @Override
    public int apply(Board board) {

        int i, j;
        int point=0;

        switch(this.effect){

            //Shadow of RED
            case PRC1:

                for(i=0; i < ROW; i++) {
                    for (j = 0; j < COLUMN; j++){
                        if(board.getDice(i, j).getDiceColor()==(Dice.ColorDice.RED))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;

            //Shadow of YELLOW
            case PRC2:

                for(i=0; i <ROW; i++) {
                    for (j = 0; j <COLUMN; j++){
                        if(board.getDice(i, j).getDiceColor()==(Dice.ColorDice.YELLOW))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;

            //Shadow of GREEN
            case PRC3:

                for(i=0; i <ROW; i++) {
                    for (j = 0; j <COLUMN; j++){
                        if(board.getDice(i, j).getDiceColor()==(Dice.ColorDice.GREEN))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;

            //Shadow of BLUE
            case PRC4:

                for(i=0; i <ROW; i++) {
                    for (j = 0; j <COLUMN; j++){
                        if(board.getDice(i, j).getDiceColor()==(Dice.ColorDice.BLUE))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;

            //Shadow of PURPLE
            case PRC5:

                for(i=0; i <ROW; i++) {
                    for (j = 0; j <COLUMN; j++){
                        if(board.getDice(i, j).getDiceColor()==(Dice.ColorDice.PURPLE))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;





        }
        return 0;
    }
}
