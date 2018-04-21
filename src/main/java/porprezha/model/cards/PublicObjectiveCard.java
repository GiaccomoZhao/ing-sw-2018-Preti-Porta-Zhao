package porprezha.model.cards;
import porprezha.model.dices.Dice;





import java.util.ArrayList;

public class PublicObjectiveCard extends ObjectiveCard{

    public PublicObjectiveCard(Effect effect) {
        super(effect);
    }

    @Override
    public int apply(Board board) {

      switch(this.effect.ID){

          case 0:
             int counter=0;
              int i, j;
              int point=0;
              int helper=1;

              for(i=0; i < 4; i++) {
                  for (j = 0; j < 5; j++){
                      if(board.getDice(i, j).colorDice.equals(Dice.ColorDice.WHITE))
                          counter=100;

                      counter = counter + (board.getDice(i, j).colorDice.ordinal()+1);
                        helper= helper*(board.getDice(i, j).colorDice.ordinal()+1);
                  }
                  if(counter==15 && helper==120)
                      point++;
              }



                return this.getNumScore()*point;




      }
      return 0;
    }
}
