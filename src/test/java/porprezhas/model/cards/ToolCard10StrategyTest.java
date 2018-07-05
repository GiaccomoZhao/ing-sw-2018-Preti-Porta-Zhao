package porprezhas.model.cards;

import org.junit.Before;
import org.junit.Test;
import porprezhas.model.dices.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class ToolCard10StrategyTest {
    ToolCard toolCard10;
    ToolCardParam param;
    DraftPool draftPool;
    List<Integer> params;


    Random random;  // used to choose an in range number arbitrarily


    @Before
    public void setUp() {
        random = new Random();
        RoundTrack roundTrack = new RoundTrack();
        draftPool = new DraftPool();
        DiceBag diceBag = new DiceBag();
        Board board = new Board(Pattern.TypePattern.VOID);
        params  = new ArrayList<>();

        Dice draftPoolDiceTest0 = new Dice(6, Dice.ColorDice.PURPLE,10);
        Dice draftPoolDiceTest1 = new Dice(5, Dice.ColorDice.GREEN,11);
        Dice draftPoolDiceTest2 = new Dice(4, Dice.ColorDice.YELLOW,12);
        Dice draftPoolDiceTest3 = new Dice(3, Dice.ColorDice.BLUE,13);
        Dice draftPoolDiceTest4 = new Dice(2, Dice.ColorDice.RED,14);

        draftPool.addDice(draftPoolDiceTest0);
        draftPool.addDice(draftPoolDiceTest1);
        draftPool.addDice(draftPoolDiceTest2);
        draftPool.addDice(draftPoolDiceTest3);
        draftPool.addDice(draftPoolDiceTest4);

        toolCard10 = new ToolCard(Card.Effect.TC10);
        param = new ToolCardParam(roundTrack, draftPool, diceBag, board, params);
    }

    @Test
    public void nullTest() {
        assertNotNull( toolCard10.getStrategy() );
       // assertFalse( toolCard10.getStrategy().use(null) );
       // assertFalse( toolCard10.getStrategy().use(param) );


        RoundTrack roundTrack = new RoundTrack();
        DraftPool draftPool = new DraftPool();
        DiceBag diceBag = new DiceBag();
        Board board = new Board(Pattern.TypePattern.VOID);
        List<Integer> params = null;

        toolCard10 = new ToolCard(Card.Effect.TC10);
        param = new ToolCardParam(roundTrack, draftPool, diceBag, board, params);

        ;
    }


    @Test
    public void changeNumberDiceTest(){

        int idDiceDraftPool = 11;

        params.add(idDiceDraftPool);

        toolCard10.getStrategy().use(param);

        assertEquals(draftPool.getDiceByID(11).getDiceNumber(),2);
        assertEquals(draftPool.getDiceByID(11).getDiceColor(), Dice.ColorDice.GREEN);

        assertEquals(draftPool.getDiceByID(10).getDiceNumber(),6);
        assertEquals(draftPool.getDiceByID(10).getDiceColor(), Dice.ColorDice.PURPLE);

        assertEquals(draftPool.getDiceByID(12).getDiceNumber(),4);
        assertEquals(draftPool.getDiceByID(12).getDiceColor(), Dice.ColorDice.YELLOW);

        assertEquals(draftPool.getDiceByID(13).getDiceNumber(),3);
        assertEquals(draftPool.getDiceByID(13).getDiceColor(), Dice.ColorDice.BLUE);

        assertEquals(draftPool.getDiceByID(14).getDiceNumber(),2);
        assertEquals(draftPool.getDiceByID(14).getDiceColor(), Dice.ColorDice.RED);

    }



}
