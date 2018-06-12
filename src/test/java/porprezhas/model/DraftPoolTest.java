package porprezhas.model;
import org.junit.*;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.DiceBag;
import porprezhas.model.dices.DraftPool;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class DraftPoolTest {

    Dice die1, die2, die3,die4,die5,die6,die7,die8,die9,testDie;
    ArrayList<Dice> diceList;
    DiceBag diceBag;
    DraftPool draftPool;

    @Before
    public void before() {
        long idCounter = 0;

        diceList= new ArrayList<>(9);
        die1 = new Dice(1, Dice.ColorDice.RED,      idCounter++);
        die2 = new Dice(2, Dice.ColorDice.BLUE,     idCounter++);
        die3 = new Dice(3, Dice.ColorDice.GREEN,    idCounter++);
        die4 = new Dice(4, Dice.ColorDice.YELLOW,   idCounter++);
        die5 = new Dice(5, Dice.ColorDice.PURPLE,   idCounter++);
        die6 = new Dice(6, Dice.ColorDice.RED,      idCounter++);
        die7 = new Dice(1, Dice.ColorDice.BLUE,     idCounter++);
        die8 = new Dice(2, Dice.ColorDice.GREEN,    idCounter++);
        die9 = new Dice(3, Dice.ColorDice.YELLOW,   idCounter++);

        testDie = new Dice(4, Dice.ColorDice.BLUE,  idCounter++);

        diceList.add(die1);
        diceList.add(die2);
        diceList.add(die3);
        diceList.add(die4);
        diceList.add(die5);
        diceList.add(die6);
        diceList.add(die7);
        diceList.add(die8);
        diceList.add(die9);

        diceBag= mock(DiceBag.class);
        when(diceBag.GetRandomDices(4)).thenReturn(diceList);

        draftPool= new DraftPool(diceBag, 4);

    }

    @Test
    public void chooseDiceTest(){
        assertEquals(draftPool.diceList().size(), 9);

        assertEquals(draftPool.chooseDice(0).getColorDice(), die1.getColorDice());

        assertEquals(draftPool.diceList().size(), 8);

        assertEquals(draftPool.chooseDice(0).getDiceNumber(), die2.getDiceNumber());

        assertEquals(draftPool.diceList().size(), 7);

    }

    @Test
    public void diceListTest(){

        assertEquals(draftPool.diceList().size(), 9);

    }

    @Test
    public void diceSubstituteTest(){
      assertEquals(draftPool.diceSubstitute(testDie,1),die1);
      assertEquals(draftPool.diceList().size(),9);
      assertEquals(draftPool.diceList().get(0).getDiceNumber(),testDie.getDiceNumber());
      assertEquals(draftPool.diceList().get(0).getColorDice(),testDie.getColorDice());

    }

    @Test
    public void addDiceTest(){
        draftPool.chooseDice(1);
        draftPool.addDice(testDie);
        assertEquals(draftPool.diceList().size(),9);
        assertEquals(draftPool.diceList().get(draftPool.diceList().size()-1).getDiceNumber(),testDie.getDiceNumber());
        assertEquals(draftPool.diceList().get(draftPool.diceList().size()-1).getColorDice(),testDie.getColorDice());
    }


}
