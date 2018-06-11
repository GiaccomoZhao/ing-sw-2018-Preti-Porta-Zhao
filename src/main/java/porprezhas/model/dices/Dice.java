
package porprezhas.model.dices;
import java.io.Serializable;
import java.util.Random;
public class Dice implements Serializable {

    static public final int MAX_DICE_NUM = 6;

    public Dice setNumber(int number) {
        this.number = number;
        return this;
    }

    private int number;
    private final ColorDice colorDice;

    public Dice( Dice dice) {
        this.number=dice.getDiceNumber();
        this.colorDice=dice.colorDice;
    }

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

    public Dice(ColorDice colorDice) {

        this.colorDice = colorDice;
    }

    public Dice(int number, ColorDice colorDice) {
        this.colorDice = colorDice;
        this.number=number;
    }

    public Dice(ColorDice colorDice, int number) {
        this.colorDice = colorDice;
        this.number=number;
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

    public void roll(){
        Random random = new Random();
        this.number=random.nextInt(MAX_DICE_NUM)+1;

    }

    public int getDiceNumber() {
        return this.number;
    }

    public ColorDice getColorDice() {
        return colorDice;
    }
}

