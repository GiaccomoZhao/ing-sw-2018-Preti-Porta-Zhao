package porprezhas.model.dices;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static porprezhas.model.dices.Dice.MAX_DICE_NUM;

public class BoxTest {
    List<Box> boxList;

    List<Dice> diceList;

/*
    Dice dieWhite, dieNoWhite;

    Dice dieSmall, dieBig;
    Dice dieTooSmall, dieTooBig;
*/

    @Before
    public void setUp() {
        boxList = new ArrayList<>();
        diceList = new ArrayList<>();

        for (int diceNum = 0; diceNum <= MAX_DICE_NUM; diceNum++) {
            for (Dice.ColorDice diceColor : Dice.ColorDice.values()) {
                if(!diceColor.equals(Dice.ColorDice.WHITE)  &&  0 != diceNum)
                    diceList.add( new Dice(diceColor, diceNum) );
                boxList. add(  new Box(diceColor, diceNum) );
            }
        }
    }

    // n square test
    @Test
    public void fullTest() {
        int checked = 0;
        int total = 0;
        for (Box box : boxList) {
            for (Dice dice : diceList) {
                total ++;
                if( box.checkConstraint(dice) ) {
                    checked ++;
                }

                if(box.freeBox()) {
                    assertEquals(true, box.checkConstraint(dice));
                }
                if(box.white()) {   // not colored
                    if(box.getNumber() == dice.getDiceNumber()) {
                        assertEquals(true, box.checkConstraint(dice));
                    }
                } else {    // colored
                    if(!dice.getColorDice().equals(box.getColor()) ) {
//                        System.out.println("box = " + box + " \tdice = " + dice);     // uncomment to understand where is error when test fails
                        assertEquals(false, box.checkConstraint(dice));
                    }
                    if( 0 != box.getNumber() &&
//                            System.out.println("box = " + box + " \tdice = " + dice);
                            box.getNumber() != dice.getDiceNumber()) {
                        assertEquals(false, box.checkConstraint(dice));
                    }
                }
            }
        }
//        System.out.println("total = " + total);       // 1260

        // 1-6 number, 1-5 colors -> 30 dices
        // 0-6 number, 0-5 colors -> ..boxes    // we don't care
        // 1 free box                   -> 30 dices true
        // 1 line num=0 boxes           -> 30 dices true
        // 1 line col=W boxes           -> 30 dices true
        // 1 table of 30 boxes == dices -> 30 dices true
        // so matching boxes with dices = 120
        assertEquals(120  , checked);
    }
}
