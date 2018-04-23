
package porprezha.model.dices;
import java.util.Random;
public class Dice {



    private int number;
    public final ColorDice colorDice;

    public Dice( Dice dice) {
        this.number=dice.getDiceNumber();
        this.colorDice=dice.colorDice;
    }

    public enum ColorDice {RED, YELLOW, GREEN, BLUE, PURPLE, WHITE};

    public Dice(ColorDice colorDice) {

        this.colorDice = colorDice;
    }

    public Dice(ColorDice colorDice, int number) {
        this.colorDice = colorDice;
        this.number=number;
    }

    public void roll(){
        Random random = new Random();
        this.number=random.nextInt(6)+1;

    }

    public int getDiceNumber() {
        return this.number;
    }

    public ColorDice getColorDice() {
        return colorDice;
    }
}

