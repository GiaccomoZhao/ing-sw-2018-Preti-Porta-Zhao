
package PorPreZha.Model;
import java.util.Random;
public class Dice {

    private int number;
    public final ColorDice colorDice;

    public enum ColorDice {RED, YELLOW, GREEN, BLUE, PURPLE};

    public Dice(ColorDice colorDice) {
        this.colorDice = colorDice;
    }

    public int roll(){
        Random random = new Random();
        this.number=random.nextInt(6)+1;
        return this.number;
    }

}

