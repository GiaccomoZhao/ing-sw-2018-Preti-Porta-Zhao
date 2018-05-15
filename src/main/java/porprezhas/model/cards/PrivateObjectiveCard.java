package porprezhas.model.cards;
import porprezhas.model.dices.Board;
import porprezhas.model.dices.Dice;

public class PrivateObjectiveCard  extends ObjectiveCard {

    public PrivateObjectiveCard(Effect effect) {

        super(effect);

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
