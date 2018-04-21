package porprezha.model.cards;
import porprezha.model.dices.Dice;


import javax.tools.Tool;

public class PrivateObjectiveCard  extends ObjectiveCard {

    public PrivateObjectiveCard(Effect effect) {
        super(effect);
    }

    @Override
    public int apply(Board board) {

        int i, j;
        int point=0;
        switch(this.effect.ID){

            case 0:

                for(i=0; i < 4; i++) {
                    for (j = 0; j < 5; j++){
                        if(board.getDice(i, j).colorDice.equals(Dice.ColorDice.RED))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;


            case 1:

                for(i=0; i < 4; i++) {
                    for (j = 0; j < 5; j++){
                        if(board.getDice(i, j).colorDice.equals(Dice.ColorDice.YELLOW))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;


            case 2:

                for(i=0; i < 4; i++) {
                    for (j = 0; j < 5; j++){
                        if(board.getDice(i, j).colorDice.equals(Dice.ColorDice.GREEN))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;


            case 3:

                for(i=0; i < 4; i++) {
                    for (j = 0; j < 5; j++){
                        if(board.getDice(i, j).colorDice.equals(Dice.ColorDice.BLUE))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;


            case 4:

                for(i=0; i < 4; i++) {
                    for (j = 0; j < 5; j++){
                        if(board.getDice(i, j).colorDice.equals(Dice.ColorDice.PURPLE))
                            point=point+board.getDice(i,j).getDiceNumber();
                    }

                }
                return point;





        }
        return 0;
    }
}
