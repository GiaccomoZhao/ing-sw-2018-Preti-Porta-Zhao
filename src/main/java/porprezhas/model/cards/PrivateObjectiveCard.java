package porprezhas.model.cards;
import porprezhas.model.dices.Board;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.DiceBag;
import porprezhas.model.dices.DraftPool;
import porprezhas.model.track.RoundTrack;

import java.io.Serializable;

public class PrivateObjectiveCard  extends ObjectiveCard implements Serializable {

    public PrivateObjectiveCard(Effect effect) {

        super(effect);

    }


    @Override
    public void use(Board board, DraftPool draftPool, int xStart1, int yStart1, int xDestination1, int yDestination1, int xStart2, int yStart2, int xDestination2, int yDestination2, Dice dice1, int number, boolean operation, RoundTrack roundTrack, DiceBag diceBag) {
        ;
    }

    @Override
    public int apply(Board board) {

        int i, j;
        int point=0;

        switch(this.effect.ID-11){

            case 0:

                for(i=0; i < 4; i++) {
                    for (j = 0; j < 5; j++){
                        if(board.getDice(i, j).getColorDice()==(Dice.ColorDice.RED))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;


            case 1:

                for(i=0; i < 4; i++) {
                    for (j = 0; j < 5; j++){
                        if(board.getDice(i, j).getColorDice()==(Dice.ColorDice.YELLOW))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;


            case 2:

                for(i=0; i < 4; i++) {
                    for (j = 0; j < 5; j++){
                        if(board.getDice(i, j).getColorDice()==(Dice.ColorDice.GREEN))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;


            case 3:

                for(i=0; i < 4; i++) {
                    for (j = 0; j < 5; j++){
                        if(board.getDice(i, j).getColorDice()==(Dice.ColorDice.BLUE))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;


            case 4:

                for(i=0; i < 4; i++) {
                    for (j = 0; j < 5; j++){
                        if(board.getDice(i, j).getColorDice()==(Dice.ColorDice.PURPLE))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;





        }
        return 0;
    }
}
