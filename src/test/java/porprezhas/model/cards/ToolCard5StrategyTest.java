package porprezhas.model.cards;

import org.junit.Before;
import org.junit.Test;
import porprezhas.model.dices.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class ToolCard5StrategyTest {
    ToolCard toolCard5;
    ToolCardParam param;
    List<Integer> params;
    DraftPool draftPool;
    RoundTrack roundTrack;


    Random random;  // used to choose an in range number arbitrarily


    @Before
    public void setUp() {
        random = new Random();
        roundTrack = new RoundTrack();
        draftPool = new DraftPool();
        DiceBag diceBag = new DiceBag();
        Board board = new Board(Pattern.TypePattern.KALEIDOSCOPIC_DREAM);
        params = new ArrayList<>();

        Dice roundTrackDiceTest0 = new Dice(1, Dice.ColorDice.RED,0);
        Dice roundTrackDiceTest1 = new Dice(2, Dice.ColorDice.BLUE,1);
        Dice roundTrackDiceTest2 = new Dice(3, Dice.ColorDice.YELLOW,2);
        Dice roundTrackDiceTest3 = new Dice(4, Dice.ColorDice.GREEN,3);
        Dice roundTrackDiceTest4 = new Dice(5, Dice.ColorDice.PURPLE,4);

        roundTrack.addDice(1,roundTrackDiceTest0);
        roundTrack.addDice(1,roundTrackDiceTest1);
        roundTrack.addDice(2,roundTrackDiceTest2);
        roundTrack.addDice(3,roundTrackDiceTest3);
        roundTrack.addDice(4,roundTrackDiceTest4);

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

        toolCard5 = new ToolCard(Card.Effect.TC5);

        param = new ToolCardParam(roundTrack, draftPool, diceBag, board, params);


    }

    @Test
    public void nullTest() {
        assertNotNull( toolCard5.getStrategy() );
        assertFalse( toolCard5.getStrategy().use(null) );
        assertFalse( toolCard5.getStrategy().use(param) );


        RoundTrack roundTrack = new RoundTrack();
        DraftPool draftPool = new DraftPool();
        DiceBag diceBag = new DiceBag();
        Board board = new Board(Pattern.TypePattern.VOID);
        List<Integer> params = null;

        toolCard5 = new ToolCard(Card.Effect.TC5);
        param = new ToolCardParam(roundTrack, draftPool, diceBag, board, params);


    }

    @Test
    public void switchDiceTest(){

        int idDiceDraftPoolTest = 10;
        int indexRoundTest = 1;
        int indexDiceRoundTrack = 1;

        params.add(idDiceDraftPoolTest);
        params.add(indexRoundTest);
        params.add(indexDiceRoundTrack);

        assertTrue(toolCard5.getStrategy().use(param));

        assertEquals(draftPool.getDiceByID(1).getColorDice(), Dice.ColorDice.BLUE);
        assertEquals(draftPool.getDiceByID(1).getDiceNumber(), 2);

        assertEquals(roundTrack.getRoundDice(1).get(1).getColorDice(), Dice.ColorDice.PURPLE);
        assertEquals(roundTrack.getRoundDice(1).get(1).getDiceNumber(), 6);


        assertEquals(draftPool.getDiceByID(11).getColorDice(), Dice.ColorDice.GREEN);
        assertEquals(draftPool.getDiceByID(11).getDiceNumber(), 5);

        assertEquals(draftPool.getDiceByID(12).getColorDice(), Dice.ColorDice.YELLOW);
        assertEquals(draftPool.getDiceByID(12).getDiceNumber(), 4);

        assertEquals(draftPool.getDiceByID(13).getColorDice(), Dice.ColorDice.BLUE);
        assertEquals(draftPool.getDiceByID(13).getDiceNumber(), 3);

        assertEquals(draftPool.getDiceByID(14).getColorDice(), Dice.ColorDice.RED);
        assertEquals(draftPool.getDiceByID(14).getDiceNumber(), 2);

        assertEquals(roundTrack.getRoundDice(1).get(0).getColorDice(), Dice.ColorDice.RED);
        assertEquals(roundTrack.getRoundDice(1).get(0).getDiceNumber(), 1);

        assertEquals(roundTrack.getRoundDice(2).get(0).getColorDice(), Dice.ColorDice.YELLOW);
        assertEquals(roundTrack.getRoundDice(2).get(0).getDiceNumber(), 3);

        assertEquals(roundTrack.getRoundDice(3).get(0).getColorDice(), Dice.ColorDice.GREEN);
        assertEquals(roundTrack.getRoundDice(3).get(0).getDiceNumber(), 4);

        assertEquals(roundTrack.getRoundDice(4).get(0).getColorDice(), Dice.ColorDice.PURPLE);
        assertEquals(roundTrack.getRoundDice(4).get(0).getDiceNumber(), 5);



    }

}
