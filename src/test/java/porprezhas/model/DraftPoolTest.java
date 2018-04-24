package porprezhas.model;
import org.junit.*;
import porprezhas.model.dices.Dice;
import porprezhas.model.dices.DiceBag;
import porprezhas.model.dices.DraftPool;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class DraftPoolTest {
    Dice dice1, dice2;
    Dice die1, die2, die3;
    ArrayList<Dice> diceList;
    DiceBag diceBag;
    DraftPool draftPool;

    @Before
    public void before() {

        diceList= new ArrayList<>(3);
        die1 = mock(Dice.class);
        die2 = mock(Dice.class);
        die3 = mock(Dice.class);

        diceList.add(die1);
        diceList.add(die2);
        diceList.add(die3);

        when(die1.getDiceNumber()).thenReturn(1);
        when(die2.getDiceNumber()).thenReturn(2);
        when(die3.getDiceNumber()).thenReturn(3);
        when(die1.getColorDice()).thenReturn(Dice.ColorDice.RED);
        when(die2.getColorDice()).thenReturn(Dice.ColorDice.BLUE);
        when(die3.getColorDice()).thenReturn(Dice.ColorDice.GREEN);



        diceBag= mock(DiceBag.class);
        when(diceBag.GetRandomDices(1)).thenReturn(diceList);

        draftPool= new DraftPool(diceBag, 1);

    }

    @Test
    public void chooseDiceTest(){
        assertEquals(draftPool.diceList().size(), 3);

        assertEquals(draftPool.chooseDice(0).getColorDice(), die1.getColorDice());

        assertEquals(draftPool.diceList().size(), 2);

        assertEquals(draftPool.chooseDice(0).getDiceNumber(), die2.getDiceNumber());

        assertEquals(draftPool.diceList().size(), 1);

    }

    @Test
    public void diceListTest(){

        assertEquals(draftPool.diceList().size(), 3);

    }


}
