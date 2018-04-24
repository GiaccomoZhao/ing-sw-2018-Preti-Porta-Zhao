package porprezha.model.cards;
import porprezha.model.dices.Dice;
import porprezha.model.dices.DraftPool;
import porprezha.model.track.RoundTrack;


import java.util.ArrayList;
import java.util.List;

import static porprezha.model.dices.Dice.ColorDice.*;



public class ToolCard extends Card {


    private int tokensQuantity;

    private  Dice.ColorDice cardColor;

    Dice.ColorDice[] cardColors={PURPLE,BLUE,RED,YELLOW,GREEN,PURPLE,BLUE,RED,YELLOW,GREEN,PURPLE,BLUE};

    public ToolCard(Effect effect) {
        super(effect);
        //if(effect.ID<16) throw  new InvalidIdException();

        this.tokensQuantity = 0;
        this.cardColor = cardColors[this.effect.ID-16];
    }

    public void addTokens(){

        tokensQuantity = tokensQuantity + 1;
    }

    public void use(Board board, DraftPool draftPool, Dice dice, int  dicePosition, boolean operation, RoundTrack roundTrack) {

        switch (this.effect.ID-16) {

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
