package porprezhas.model.cards;
import porprezhas.model.dices.Board;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.DiceBag;
import porprezhas.model.dices.DraftPool;
import porprezhas.model.track.RoundTrack;

import java.io.Serializable;

import static porprezhas.model.cards.Card.Effect.*;
import static porprezhas.model.dices.Board.COLUMN;
import static porprezhas.model.dices.Board.ROW;

public class PrivateObjectiveCard  extends ObjectiveCard implements Serializable {

    public PrivateObjectiveCard(Effect effect) {

        super(effect);

    }


    public void use(Board board, DraftPool draftPool, int xStart1, int yStart1, int xDestination1, int yDestination1, int xStart2, int yStart2, int xDestination2, int yDestination2, Dice dice1, int number, boolean operation, RoundTrack roundTrack, DiceBag diceBag) {
    }

    @Override
    public int apply(Board board) {

        int i, j;
        int point=0;

        switch(this.effect){

            case PRC1:

                for(i=0; i < ROW; i++) {
                    for (j = 0; j < COLUMN; j++){
                        if(board.getDice(i, j).getColorDice()==(Dice.ColorDice.RED))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;


            case PRC2:

                for(i=0; i <ROW; i++) {
                    for (j = 0; j <COLUMN; j++){
                        if(board.getDice(i, j).getColorDice()==(Dice.ColorDice.YELLOW))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;


            case PRC3:

                for(i=0; i <ROW; i++) {
                    for (j = 0; j <COLUMN; j++){
                        if(board.getDice(i, j).getColorDice()==(Dice.ColorDice.GREEN))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;


            case PRC4:

                for(i=0; i <ROW; i++) {
                    for (j = 0; j <COLUMN; j++){
                        if(board.getDice(i, j).getColorDice()==(Dice.ColorDice.BLUE))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;


            case PRC5:

                for(i=0; i <ROW; i++) {
                    for (j = 0; j <COLUMN; j++){
                        if(board.getDice(i, j).getColorDice()==(Dice.ColorDice.PURPLE))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;





        }
        return 0;
    }
}
