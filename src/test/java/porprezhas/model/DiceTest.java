
package porprezhas.model;
import porprezhas.model.dices.Dice;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class DiceTest {

     Dice die = new Dice(Dice.ColorDice.RED);


    //Test of the die number value
    @Test
    public void testCasualDiceNumber(){
        die.roll();
        assertTrue(7>die.getDiceNumber() && 0 < die.getDiceNumber());
    }

}
