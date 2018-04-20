package PorPreZha.Model;

import com.sun.tools.internal.xjc.reader.xmlschema.BindPurple;

import java.util.ArrayList;
import java.util.List;

import static PorPreZha.Model.Dice.ColorDice.*;



public class ToolCard extends Card{

    private static int tokensQuantity;

    private static Dice.ColorDice cardColor;
    Dice.ColorDice[] cardColors={PURPLE,BLUE,RED,YELLOW,GREEN,PURPLE,BLUE,RED,YELLOW,GREEN,PURPLE,BLUE};

    public ToolCard(Effect effect){

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
