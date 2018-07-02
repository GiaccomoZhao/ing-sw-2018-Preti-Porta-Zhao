package porprezhas.model.cards;

import org.junit.Before;
import org.junit.Test;
import porprezhas.model.dices.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class ToolCard6StrategyTest {
    ToolCard toolCard6;
    ToolCardParam param;
    List<Integer> params;
    Board board;
    DraftPool draftPool;

    Dice draftPoolDiceTest1;
    Dice draftPoolDiceTest2;

    Random random;  // used to choose an in range number arbitrarily


    @Before
    public void setUp() {
        random = new Random();
        RoundTrack roundTrack = new RoundTrack();
        draftPool = new DraftPool();
        DiceBag diceBag = new DiceBag();
        board = new Board(Pattern.TypePattern.VOID);
        params = new ArrayList<>();

        Dice boardDiceTest1 = new Dice(6, Dice.ColorDice.YELLOW,1);
        Dice boardDiceTest2 = new Dice(4, Dice.ColorDice.BLUE,2);
        Dice boardDiceTest3 = new Dice(3, Dice.ColorDice.RED,3);
        Dice boardDiceTest4 = new Dice(2, Dice.ColorDice.PURPLE,4);
        Dice boardDiceTest5 = new Dice(1, Dice.ColorDice.RED,5);
        Dice boardDiceTest6 = new Dice(1, Dice.ColorDice.GREEN,6);
        Dice boardDiceTest7 = new Dice(2, Dice.ColorDice.PURPLE,7);
        Dice boardDiceTest8 = new Dice(5, Dice.ColorDice.YELLOW,8);
        Dice boardDiceTest9 = new Dice(3, Dice.ColorDice.GREEN,9);
        Dice boardDiceTest10 = new Dice(4, Dice.ColorDice.PURPLE,10);
        Dice boardDiceTest11 = new Dice(3, Dice.ColorDice.YELLOW,11);
        Dice boardDiceTest12 = new Dice(4, Dice.ColorDice.BLUE,12);
        Dice boardDiceTest13 = new Dice(1, Dice.ColorDice.RED,13);
        Dice boardDiceTest14 = new Dice(5, Dice.ColorDice.PURPLE,14);
        Dice boardDiceTest15 = new Dice(2, Dice.ColorDice.BLUE,15);
        Dice boardDiceTest16 = new Dice(3, Dice.ColorDice.GREEN,16);
        Dice boardDiceTest17 = new Dice(4, Dice.ColorDice.PURPLE,17);

        draftPoolDiceTest1 = new Dice(1, Dice.ColorDice.YELLOW,20);
        draftPoolDiceTest2 = new Dice(2, Dice.ColorDice.PURPLE,21);

        board.insertDice(boardDiceTest1,0,0);
        board.insertDice(boardDiceTest2,0,1);
        board.insertDice(boardDiceTest3,0,2);
        board.insertDice(boardDiceTest4,0,3);
        board.insertDice(boardDiceTest5,0,4);
        board.insertDice(boardDiceTest6,1,0);
        board.insertDice(boardDiceTest7,1,1);
        board.insertDice(boardDiceTest8,1,2);
        board.insertDice(boardDiceTest9,1,3);
        board.insertDice(boardDiceTest10,1,4);
        board.insertDice(boardDiceTest11,2,0);
        board.insertDice(boardDiceTest12,2,1);
        board.insertDice(boardDiceTest13,2,2);
        board.insertDice(boardDiceTest14,2,3);
        board.insertDice(boardDiceTest15,3,0);
        board.insertDice(boardDiceTest16,3,1);
        board.insertDice(boardDiceTest17,3,2);

        draftPool.addDice(draftPoolDiceTest1);
        draftPool.addDice(draftPoolDiceTest2);

        toolCard6 = new ToolCard(Card.Effect.TC6);
        param = new ToolCardParam(roundTrack, draftPool, diceBag, board, params);
    }

    @Test
    public void nullTest() {
        assertNotNull( toolCard6.getStrategy() );
      //  assertFalse( toolCard6.getStrategy().use(null) );
      //  assertFalse( toolCard6.getStrategy().use(param) );


        RoundTrack roundTrack = new RoundTrack();
        DraftPool draftPool = new DraftPool();
        DiceBag diceBag = new DiceBag();
        Board board = new Board(Pattern.TypePattern.VOID);
        List<Integer> params = null;

        toolCard6 = new ToolCard(Card.Effect.TC6);
        param = new ToolCardParam(roundTrack, draftPool, diceBag, board, params);

        ;
    }

    @Test
    public void reRollAndPlaceSuccessTest(){

        int idDiceDraftPoolTest = 20;
        params.add(idDiceDraftPoolTest);
        assertTrue(toolCard6.getStrategy().use(param));

    }

    @Test
    public void reRollAndPlaceFailureTest(){

        int idDiceDraftPoolTest = 21;
        params.add(idDiceDraftPoolTest);
        assertTrue(toolCard6.getStrategy().use(param));


    }


}
