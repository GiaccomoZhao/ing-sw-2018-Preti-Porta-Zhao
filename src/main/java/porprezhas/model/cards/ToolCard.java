package porprezhas.model.cards;
import porprezhas.model.dices.Board;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.DiceBag;
import porprezhas.model.dices.DraftPool;
import porprezhas.model.dices.RoundTrack;


import java.io.Serializable;

import static porprezhas.model.cards.Card.Effect.*;
import static porprezhas.model.dices.Board.COLUMN;
import static porprezhas.model.dices.Board.ROW;
import static porprezhas.model.dices.Dice.ColorDice.*;



public class ToolCard extends Card implements Serializable {
    public static final Dice.ColorDice[] cardColors = {PURPLE, BLUE, RED, YELLOW, GREEN, PURPLE, BLUE, RED, YELLOW, GREEN, PURPLE, BLUE};
    public static final boolean[] hasEffect = {false, false, false, false, false,
            true,       // TC6: Must Insert getReturn()
            false,
            true,       // TC8: Must Skip second Turn
            false, false,
            true,       // TC11: Must Insert getReturn().get(i), with i=0 as default
            false
    };     // this array has index start from 0

    private ToolCardStrategy strategy;

    private int tokensQuantity;

    private Dice.ColorDice cardColor;


    // NB: create a new ToolCard means get the instance of this card, it is NOT a NEW card
    public ToolCard(Effect effect) {
        super(effect);  //if(effect.ID<16) throw  new InvalidIdException();
        setStrategy();

        this.tokensQuantity = 0;
        this.cardColor = cardColors[this.effect.ID - 1];
    }

    public ToolCardStrategy getStrategy() {
        return strategy;
    }

    // Get Strategy from Array
    private void setStrategy() {
        strategy = ToolCardStrategy.list[effect.ID - 1];
    }


    public int getTokensQuantity() {
        return tokensQuantity;
    }

    public void addTokens() {
        tokensQuantity = tokensQuantity + 1;
    }

}
