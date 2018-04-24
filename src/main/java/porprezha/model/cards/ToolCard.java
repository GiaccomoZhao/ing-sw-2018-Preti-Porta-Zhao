package porprezha.model.cards;
import porprezha.model.dices.Dice;
import porprezha.model.dices.DraftPool;
import porprezha.model.track.RoundTrack;


import java.util.ArrayList;
import java.util.List;

import static porprezha.model.dices.Dice.ColorDice.*;



public class ToolCard extends Card {

    private static int tokensQuantity;

    private static Dice.ColorDice cardColor;
    Dice.ColorDice[] cardColors={PURPLE,BLUE,RED,YELLOW,GREEN,PURPLE,BLUE,RED,YELLOW,GREEN,PURPLE,BLUE};

    public ToolCard(porprezha.model.cards.Card.Effect effect){

        super(effect);
        int i=0;
        while(15<effect.ID){
           ToolCard.tokensQuantity=0;
           ToolCard.cardColor=cardColors[i];
           i++;
        }
    }

    public void addTokens(){
        tokensQuantity=tokensQuantity+1;
    }

    public void use(Board board, DraftPool draftPool, Dice dice, int  dicePosition, boolean operation, RoundTrack roundTrack) {

        switch (this.effect.ID) {

            case 0:

                if(dice.getDiceNumber()!=6&&operation)
                    draftPool.chooseDice(dicePosition).setNumber(draftPool.chooseDice(dicePosition).getDiceNumber()+1);

                if(dice.getDiceNumber()!=1&&!operation)
                        draftPool.chooseDice(dicePosition).setNumber(draftPool.chooseDice(dicePosition).getDiceNumber()-1);

                break;

            case 4:



            case 9:

                if((draftPool.chooseDice(dicePosition).getDiceNumber()-6)<0){
                    draftPool.chooseDice(dicePosition).setNumber(7-draftPool.chooseDice(dicePosition).getDiceNumber());
                }

                else
                    draftPool.chooseDice(dicePosition).setNumber(draftPool.chooseDice(dicePosition).getDiceNumber()-5);

                break;


        }

    }



}
