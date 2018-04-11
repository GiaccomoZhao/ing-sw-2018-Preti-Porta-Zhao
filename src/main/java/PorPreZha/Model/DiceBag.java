package PorPreZha.Model;


import java.util.ArrayList;
import java.util.List;

public class DiceBag {

    ArrayList<Dice> dices;

    public DiceBag() {
        this.dices = new ArrayList(90);
        for(int i=0; i<18; i++){
            dices.add( new Dice( Dice.ColorDice.RED));
            dices.add( new Dice( Dice.ColorDice.YELLOW));
            dices.add( new Dice( Dice.ColorDice.GREEN));
            dices.add( new Dice( Dice.ColorDice.BLUE));
            dices.add( new Dice( Dice.ColorDice.PURPLE));

        }
    }
}
