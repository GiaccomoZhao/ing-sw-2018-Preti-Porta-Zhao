package porprezhas.model.cards;

import org.junit.Before;
import org.junit.Test;
import porprezhas.model.GameConstants;
import porprezhas.model.dices.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import static porprezhas.model.cards.ToolCardParam.IncDec.*;

public class ToolCard1StrategyTest {
    ToolCard toolCard1;
    ToolCardParam param;
    List<Integer> integerParams;

    Random random;  // used to choose an in range number arbitrarily

    DraftPool draftPool;

    // not used container
    DiceBag diceBag;
    RoundTrack roundTrack;
    Board voidBoard;


    @Before
    public void setUp() {
        random = new Random();
        diceBag = new DiceBag();
        draftPool = new DraftPool(diceBag.GetRandomDices(random.nextInt(GameConstants.MAX_PLAYER_QUANTITY) + 1));
        integerParams = new ArrayList<>();

        roundTrack = new RoundTrack();
        voidBoard = new Board(Pattern.TypePattern.VOID);

        toolCard1 = new ToolCard(Card.Effect.TC1);
        param = new ToolCardParam(roundTrack, draftPool, diceBag, voidBoard, integerParams);
    }

    @Test
    public void nullTest() {
        // test toolCard is constructed
        assertNotNull(toolCard1.getStrategy());

        // test with Null parameters
        assertFalse(toolCard1.getStrategy().use(null));

        // test with Empty parameters
        assertFalse(toolCard1.getStrategy().use(param));

        // test with all Null
        param = new ToolCardParam(null, null, null, null, null);
        assertFalse(toolCard1.getStrategy().use(param));

        // test with Null Integer Params
        param = new ToolCardParam(null, draftPool, diceBag, null, null);
        assertFalse(toolCard1.getStrategy().use(param));

        // test with Correct Container and param, but Null not used Container
        integerParams.add(0);   // correct param, the first dice of the list
        integerParams.add(ToolCardParam.IncDec.DECREMENT.toInteger());  // correct param
        param = new ToolCardParam(null, draftPool, null, null, integerParams);   // correct Container
        assertTrue(toolCard1.getStrategy().use(param));     // CORRECT TEST
    }

    @Test
    public void emptyTest() {
        // initialize empty containers and params
        RoundTrack emptyRoundTrack = new RoundTrack();
        DraftPool emptyDraftPool = new DraftPool();
        DiceBag emptyDiceBag = new DiceBag();
        Board emptyBoard = new Board(Pattern.TypePattern.VOID);
        List<Integer> emptyParams = new ArrayList<>();

        // test with All Empty
        param = new ToolCardParam(emptyRoundTrack, emptyDraftPool, emptyDiceBag, emptyBoard, emptyParams);
        assertFalse(toolCard1.getStrategy().use(param));

        // test with Null Dice Container and EMPTY Integer Params
        param = new ToolCardParam(null, null, null, null, emptyParams);
        assertFalse(toolCard1.getStrategy().use(param));

        // test with Correct Dice Container and EMPTY Integer Params
        param = new ToolCardParam(emptyRoundTrack, draftPool, diceBag, emptyBoard, emptyParams);
        assertFalse(toolCard1.getStrategy().use(param));

        // test with NULL Dice Container and Correct Integer Params
        param = new ToolCardParam(null, null, null, null, integerParams);
        assertFalse(toolCard1.getStrategy().use(param));

        // test with EMPTY Dice Container and Correct Integer Params
        param = new ToolCardParam(emptyRoundTrack, emptyDraftPool, emptyDiceBag, emptyBoard, integerParams);
        assertFalse(toolCard1.getStrategy().use(param));
    }


    // Correct Tests

    @Test
    public void incrementFirstTest() {
        integerParams.add(0);   // the first dice of the draft pool dice list
        integerParams.add(INCREMENT.toInteger());       // INC

        // PRINT the Draft Pool, because we are testing a generic dices
        System.out.println("\n\nincrementFirstTest: ");
        System.out.println(draftPool.toString());

        assertTrue( toolCard1.getStrategy().use(param) );             // USE

        System.out.println(draftPool.toString());
    }

    @Test
    public void decrementFirstTest() {
        integerParams.add(0);       // first
        integerParams.add(DECREMENT.toInteger());       // DEC

        System.out.println("\n\ndecrementFirstTest: ");
        System.out.println(draftPool.toString());

        assertTrue( toolCard1.getStrategy().use(param) );             // USE

        System.out.println(draftPool.toString());
    }


    @Test
    public void incrementLastTest() {
        integerParams.add(draftPool.diceList().size() -1);
        integerParams.add(INCREMENT.toInteger());

        System.out.println("\n\nincrementLastTest: ");
        System.out.println(draftPool.toString());

        assertTrue( toolCard1.getStrategy().use(param) );      // USE

        System.out.println(draftPool.toString());
    }

    @Test
    public void decrementLastTest() {
        integerParams.add(draftPool.diceList().size() -1);
        integerParams.add(DECREMENT.toInteger());

        System.out.println("\n\ndecrementLastTest: ");
        System.out.println(draftPool.toString());

        assertTrue( toolCard1.getStrategy().use(param) );      // USE

        System.out.println(draftPool.toString());
    }


    @Test
    public void outLowBoundTest() {
        integerParams.add(-1);
        integerParams.add(ToolCardParam.IncDec.DECREMENT.toInteger());

        assertFalse( toolCard1.getStrategy().use(param) );      // USE


        integerParams.clear();
        integerParams.add(-1);
        integerParams.add(ToolCardParam.IncDec.INCREMENT.toInteger());

        assertFalse( toolCard1.getStrategy().use(param) );
    }

    @Test
    public void outHighBoundTest() {
        integerParams.add(draftPool.diceList().size());
        integerParams.add(ToolCardParam.IncDec.DECREMENT.toInteger());

        assertFalse( toolCard1.getStrategy().use(param) );      // USE

        integerParams.clear();
        integerParams.add(draftPool.diceList().size());
        integerParams.add(ToolCardParam.IncDec.DECREMENT.toInteger());

        assertFalse( toolCard1.getStrategy().use(param) );
    }

    @Test
    public void increment1Test() {
        // initialize the environment to Increment a Dice with Number = 1
        Dice dice1 = new Dice(1, Dice.ColorDice.values() [random.nextInt(Dice.ColorDice.values().length -1)], random.nextInt());
        List<Dice> diceList = new ArrayList<>();
        diceList.add( dice1 );
        draftPool = new DraftPool(diceList);
        integerParams.add(0);   // the first dice of the list
        integerParams.add(ToolCardParam.IncDec.INCREMENT.toInteger());
        param = new ToolCardParam(null, draftPool, null, null, integerParams);

        // check use() Success and Number Changes
        assertTrue( toolCard1.getStrategy().use(param) );       // USE
        assertEquals(2, draftPool.diceList().get(0).getDiceNumber() );

        // check remain property does not change
        // dice property
        assertEquals(dice1.getColorDice(), draftPool.diceList().get(0).getColorDice() );
        assertEquals(dice1.getId(), draftPool.diceList().get(0).getId() );
        // draft pool property
        assertEquals(1, draftPool.diceList().size());
    }

    @Test
    public void increment6Test() {
        // initialize the environment to Increment a Dice with Number = 6
        Dice dice6 = new Dice(6, Dice.ColorDice.values() [random.nextInt(Dice.ColorDice.values().length -1)], random.nextInt());
        List<Dice> diceList = new ArrayList<>();
        diceList.add( dice6 );
        draftPool = new DraftPool(diceList);
        integerParams.add(0);   // the first dice of the list
        integerParams.add(ToolCardParam.IncDec.INCREMENT.toInteger());
        param = new ToolCardParam(null, draftPool, null, null, integerParams);

        // check use() Fails and Number does Not Changes
        assertFalse( toolCard1.getStrategy().use(param) );      // USE
        assertEquals(6, draftPool.diceList().get(0).getDiceNumber() );

        // check remain property does not change
        // dice property
        assertEquals(dice6.getColorDice(), draftPool.diceList().get(0).getColorDice() );
        assertEquals(dice6.getId(), draftPool.diceList().get(0).getId() );
        // draft pool property
        assertEquals(1, draftPool.diceList().size());
    }


    @Test
    public void decrement6Test() {
        // initialize the environment to Decrement a Dice with Number = 6
        Dice dice6 = new Dice(6, Dice.ColorDice.values() [random.nextInt(Dice.ColorDice.values().length -1)], random.nextInt());
        List<Dice> diceList = new ArrayList<>();
        diceList.add( dice6 );
        draftPool = new DraftPool(diceList);
        integerParams.add(0);   // the first dice of the list
        integerParams.add(ToolCardParam.IncDec.DECREMENT.toInteger());
        param = new ToolCardParam(null, draftPool, null, null, integerParams);

        // check use() successful and Dice Number Change of -1
        assertTrue( toolCard1.getStrategy().use(param) );       // USE
        assertEquals(5, draftPool.diceList().get(0).getDiceNumber() );

        // check remain property does not changes
        // dice property
        assertEquals(dice6.getColorDice(), draftPool.diceList().get(0).getColorDice() );
        assertEquals(dice6.getId(), draftPool.diceList().get(0).getId() );
        // draft pool property
        assertEquals(1, draftPool.diceList().size());
    }

    @Test
    public void decrement1Test() {
        // initialize the environment to Decrement a Dice with Number = 1
        Dice dice1 = new Dice(1, Dice.ColorDice.values() [random.nextInt(Dice.ColorDice.values().length -1)], random.nextInt());
        List<Dice> diceList = new ArrayList<>();
        diceList.add( dice1 );
        draftPool = new DraftPool(diceList);
        integerParams.add(0);   // the first dice of the list
        integerParams.add(ToolCardParam.IncDec.DECREMENT.toInteger());
        param = new ToolCardParam(null, draftPool, null, null, integerParams);

        // check use() Fails and Number does not change
        assertFalse( toolCard1.getStrategy().use(param) );      // USE
        assertEquals(1, draftPool.diceList().get(0).getDiceNumber() );

        // check remain property does not changes
        // dice property
        assertEquals(dice1.getColorDice(), draftPool.diceList().get(0).getColorDice() );
        assertEquals(dice1.getId(), draftPool.diceList().get(0).getId() );
        // draft pool property
        assertEquals(1, draftPool.diceList().size());
    }

}
