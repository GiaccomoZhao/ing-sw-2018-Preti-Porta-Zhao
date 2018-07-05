package porprezhas.model.cards;

import org.junit.Before;
import org.junit.Test;
import porprezhas.model.dices.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class ToolCard7StrategyTest {
    ToolCard toolCard7;
    ToolCardParam param;
    Dice draftPoolDiceTest1;
    Dice draftPoolDiceTest2;
    Dice draftPoolDiceTest3;
    Dice draftPoolDiceTest4;
    Dice draftPoolDiceTest5;
    Dice draftPoolDiceTest6;
    List<Integer> params;
    DraftPool draftPool;


    Random random;  // used to choose an in range number arbitrarily


    @Before
    public void setUp() {
        random = new Random();
        RoundTrack roundTrack = new RoundTrack();
        draftPool = new DraftPool();
        DiceBag diceBag = new DiceBag();
        Board board = new Board(Pattern.TypePattern.VOID);
        params = new ArrayList<>();

        draftPoolDiceTest1 = new Dice(6, Dice.ColorDice.PURPLE,1);
        draftPoolDiceTest2 = new Dice(5, Dice.ColorDice.GREEN,2);
        draftPoolDiceTest3 = new Dice(4, Dice.ColorDice.YELLOW,3);
        draftPoolDiceTest4= new Dice(3, Dice.ColorDice.BLUE,4);
        draftPoolDiceTest5 = new Dice(2, Dice.ColorDice.RED,5);
        draftPoolDiceTest6 = new Dice(1, Dice.ColorDice.PURPLE,6);

        draftPool.addDice(draftPoolDiceTest1);
        draftPool.addDice(draftPoolDiceTest2);
        draftPool.addDice(draftPoolDiceTest3);
        draftPool.addDice(draftPoolDiceTest4);
        draftPool.addDice(draftPoolDiceTest5);
        draftPool.addDice(draftPoolDiceTest6);

        toolCard7 = new ToolCard(Card.Effect.TC7);
        param = new ToolCardParam(roundTrack, draftPool, diceBag, board, params);
    }

    @Test
    public void nullTest() {
        assertNotNull( toolCard7.getStrategy() );
      //  assertFalse( toolCard7.getStrategy().use(null) );
      //  assertFalse( toolCard7.getStrategy().use(param) );


        RoundTrack roundTrack = new RoundTrack();
        DraftPool draftPool = new DraftPool();
        DiceBag diceBag = new DiceBag();
        Board board = new Board(Pattern.TypePattern.VOID);
        List<Integer> params = null;

        toolCard7 = new ToolCard(Card.Effect.TC7);
        param = new ToolCardParam(roundTrack, draftPool, diceBag, board, params);

        ;
    }

    @Test
    public void reRollDraftPoolTest(){

        int previousDraftPoolSize = draftPool.diceList().size();

        Dice.ColorDice previousColorDice1 = draftPoolDiceTest1.getDiceColor();
        Dice.ColorDice previousColorDice2 = draftPoolDiceTest2.getDiceColor();
        Dice.ColorDice previousColorDice3 = draftPoolDiceTest3.getDiceColor();
        Dice.ColorDice previousColorDice4 = draftPoolDiceTest4.getDiceColor();
        Dice.ColorDice previousColorDice5 = draftPoolDiceTest5.getDiceColor();
        Dice.ColorDice previousColorDice6 = draftPoolDiceTest6.getDiceColor();

        assertTrue(toolCard7.getStrategy().use(param));



        assertEquals(draftPool.diceList().size(),previousDraftPoolSize);

        assertEquals(draftPool.getDiceByID(1).getDiceColor(),previousColorDice1);
        assertEquals(draftPool.getDiceByID(2).getDiceColor(),previousColorDice2);
        assertEquals(draftPool.getDiceByID(3).getDiceColor(),previousColorDice3);
        assertEquals(draftPool.getDiceByID(4).getDiceColor(),previousColorDice4);
        assertEquals(draftPool.getDiceByID(5).getDiceColor(),previousColorDice5);
        assertEquals(draftPool.getDiceByID(6).getDiceColor(),previousColorDice6);
    }

}
