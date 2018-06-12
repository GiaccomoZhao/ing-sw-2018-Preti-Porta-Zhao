
package porprezhas.model;
import porprezhas.Useful;
import porprezhas.model.dices.Dice;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class DiceTest {

     Dice die = new Dice(Dice.ColorDice.RED, -1);


    //Test of the die number value
    @Test
    public void testCasualDiceNumber(){
        // we can not guarantee it works
        // but for a very very high probability it works
        long fakeInfinity = 100000;
        for (int i = 0; i < fakeInfinity; i++) {
            die.roll();
            assertTrue(Useful.isValueBetweenInclusive(die.getDiceNumber(), 1, Dice.MAX_DICE_NUMBER));
        }
    }

    @Test
    public void testSetNumber() {
        assertNull( die.setNumber(0) );
        assertNull( die.setNumber(Dice.MAX_DICE_NUMBER +1) );
        assertNotNull( die.setNumber(1) );
        assertNotNull( die.setNumber(Dice.MAX_DICE_NUMBER ) );
    }

}
