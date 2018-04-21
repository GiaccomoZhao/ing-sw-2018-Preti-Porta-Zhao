package porprezha.model.cards;
import porprezha.model.dices.Dice;



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

    public void use(){};

    public void addTokens(){
        tokensQuantity=tokensQuantity+1;
    }


}
