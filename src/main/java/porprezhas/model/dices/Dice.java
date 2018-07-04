
package porprezhas.model.dices;
import porprezhas.Useful;

import java.io.Serializable;
import java.util.Random;
public class Dice implements Serializable {

    public enum ColorDice {RED, YELLOW, GREEN, BLUE, PURPLE, WHITE;
        public static ColorDice getByString(String string) {
            for (ColorDice color : ColorDice.values()) {
                if(0 == color.name().compareToIgnoreCase(string)) {
                    return color;
                }
            }
//            return null;
            return WHITE;
        }
    }

    static public final int MAX_DICE_NUMBER = 6;
    static public final int MIN_DICE_NUMBER = 1;



    private final long id;
    private final ColorDice colorDice;
    private int number;



    public Dice( Dice dice) {
        this.number=dice.getDiceNumber();
        this.colorDice=dice.colorDice;
        this.id = dice.id;
    }

    public Dice(int number, ColorDice colorDice, long id) {
        this.colorDice = colorDice;
        this.id = id;
        setNumber(number);
    }

    public Dice(ColorDice colorDice, int number, long id) {
        this.colorDice = colorDice;
        this.id = id;
        setNumber(number);
    }

    // need to roll or setNumber later
    public Dice(ColorDice colorDice, long id) {
        this.colorDice = colorDice;
        this.id = id;
    }



    public long getId() {
        return id;
    }

    public ColorDice getColorDice() {
        return colorDice;
    }

    public int getDiceNumber() {
        return this.number;
    }




    public Dice setNumber(int number) {
        if(Useful.isValueBetweenInclusive(number, 1, MAX_DICE_NUMBER)) {
            this.number = number;
            return this;
        } else
            return null;
    }


    public Dice roll(){
        Random random = new Random();
        this.number=random.nextInt(MAX_DICE_NUMBER)+1;
        return this;
    }

/*
    @Override
    public String toString() {
        return "Dice{" +
                "number=" + number +
                ", colorDice=" + colorDice +
                '}';
    }
*/

    @Override
    public String toString() {
        return "Dice{" +
                + number +
                "" + colorDice.toString().substring(0,1) +
                '}';
    }
}

