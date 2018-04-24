package porprezha.Model;
import org.junit.*;
import porprezha.model.dices.Dice;
import porprezha.model.dices.DiceBag;
import porprezha.model.dices.DraftPool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class DraftPoolTest {
    Dice dice1, dice2;
    DiceBag diceBag;
    DraftPool draftPool;

    @Before
    public void before() {

        diceBag= new DiceBag();

        draftPool= new DraftPool(diceBag, 4);

    }

    @Test
    public void chooseDiceTest(){

        dice2= draftPool.diceList().get(0);
        dice1= draftPool.chooseDice(0);

        assertEquals(draftPool.diceList().size(),8);
        assertTrue(dice1.colorDice.equals(dice2.colorDice));
        assertEquals(dice1.getDiceNumber(), dice2.getDiceNumber());
    }

    @Test
    public void diceListTest(){

        assertEquals(draftPool.diceList().size(), 9);

    }


}
