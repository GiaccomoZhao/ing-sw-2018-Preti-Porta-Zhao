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
        integerParams.add(0);   // correct param
        integerParams.add(ToolCardParam.IncDec.DECREMENT.toInteger());  // correct param
        param = new ToolCardParam(null, draftPool, diceBag, null, integerParams);   // correct Container
        assertTrue(toolCard1.getStrategy().use(param));
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
        integerParams.add(0);
        integerParams.add(INCREMENT.toInteger());

        System.out.println("\n\nincrementFirstTest: ");
        System.out.println(draftPool.toString());

        toolCard1.getStrategy().use(param);

        System.out.println(draftPool.toString());
    }

    @Test
    public void decrementFirstTest() {
        integerParams.add(0);
        integerParams.add(DECREMENT.toInteger());       // DEC

        System.out.println("\n\ndecrementFirstTest: ");
        System.out.println(draftPool.toString());

        toolCard1.getStrategy().use(param);     // USE

        System.out.println(draftPool.toString());
    }


    @Test
    public void incrementLastTest() {
        integerParams.add(draftPool.diceList().size() -1);
        integerParams.add(INCREMENT.toInteger());

        System.out.println("\n\nincrementLastTest: ");
        System.out.println(draftPool.toString());

        toolCard1.getStrategy().use(param);

        System.out.println(draftPool.toString());
    }

    @Test
    public void decrementLastTest() {
        integerParams.add(draftPool.diceList().size() -1);
        integerParams.add(DECREMENT.toInteger());

        System.out.println("\n\ndecrementLastTest: ");
        System.out.println(draftPool.toString());

        toolCard1.getStrategy().use(param);

        System.out.println(draftPool.toString());
    }


    @Test
    public void outBoundTest() {
        DraftPool draftPool = new DraftPool(diceBag.GetRandomDices(random.nextInt(GameConstants.MAX_PLAYER_QUANTITY) + 1));
        List<Integer> integerParams = new ArrayList<>();

        integerParams.add(draftPool.diceList().size() -1);
        integerParams.add(ToolCardParam.IncDec.DECREMENT.toInteger());

        toolCard1 = new ToolCard(Card.Effect.TC1);
        param = new ToolCardParam(null, draftPool, diceBag, null, integerParams);

        System.out.println("\n\nincrement9Test: ");
        System.out.println(draftPool.toString());

        toolCard1.getStrategy().use(param);

        System.out.println(draftPool.toString());


    }
}
