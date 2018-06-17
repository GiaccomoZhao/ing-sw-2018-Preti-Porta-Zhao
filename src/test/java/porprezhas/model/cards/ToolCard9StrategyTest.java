package porprezhas.model.cards;

import org.junit.Before;
import org.junit.Test;
import porprezhas.model.dices.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class ToolCard9StrategyTest {
    ToolCard toolCard9;
    ToolCardParam param;
    List<Integer> params;
    DraftPool draftPool;
    Board board;


    Random random;  // used to choose an in range number arbitrarily


    @Before
    public void setUp() {
        random = new Random();
        RoundTrack roundTrack = new RoundTrack();
        draftPool = new DraftPool();
        DiceBag diceBag = new DiceBag();
        board = new Board(Pattern.TypePattern.AURORA_SAGRADIS);
        params = new ArrayList<>();

        Dice boardDiceTest1 = new Dice(1, Dice.ColorDice.RED,1);
        Dice boardDiceTest2 = new Dice(2, Dice.ColorDice.YELLOW,2);
        Dice boardDiceTest3 = new Dice(3, Dice.ColorDice.BLUE,3);
        Dice boardDiceTest4 = new Dice(4, Dice.ColorDice.GREEN,4);
        Dice boardDiceTest5 = new Dice(5, Dice.ColorDice.PURPLE,5);
        Dice boardDiceTest6 = new Dice(6, Dice.ColorDice.RED,6);

        Dice draftPoolDice1 = new Dice(1, Dice.ColorDice.BLUE,11);
        Dice draftPoolDice2 = new Dice(2, Dice.ColorDice.YELLOW,12);

        board.insertDice(boardDiceTest1,0,0);
        board.insertDice(boardDiceTest2,0,1);
        board.insertDice(boardDiceTest3,0,2);
        board.insertDice(boardDiceTest4,1,3);
        board.insertDice(boardDiceTest5,1,1);
        board.insertDice(boardDiceTest6,2,0);

        draftPool.addDice(draftPoolDice1);
        draftPool.addDice(draftPoolDice2);

        toolCard9 = new ToolCard(Card.Effect.TC9);
        param = new ToolCardParam(roundTrack, draftPool, diceBag, board, params);
    }

    @Test
    public void nullTest() {

        assertNotNull( toolCard9.getStrategy() );
        assertFalse( toolCard9.getStrategy().use(null) );
        assertFalse( toolCard9.getStrategy().use(param) );


        RoundTrack roundTrack = new RoundTrack();
        DraftPool draftPool = new DraftPool();
        DiceBag diceBag = new DiceBag();
        Board board = new Board(Pattern.TypePattern.VOID);
        List<Integer> params = null;

        toolCard9 = new ToolCard(Card.Effect.TC9);
        param = new ToolCardParam(roundTrack, draftPool, diceBag, board, params);

        ;
    }


    @Test
    public void placeDiceTest(){

       int idDiceDraftPool=11;
       int row=3;
       int col=4;


       params.add(idDiceDraftPool);
       params.add(row);
       params.add(col);



       assertTrue(toolCard9.getStrategy().use(param));

       assertEquals(board.getDice(3,4).getDiceNumber(),1);
       assertEquals(board.getDice(3,4).getColorDice(), Dice.ColorDice.BLUE);

       assertEquals(board.getDice(0,0).getDiceNumber(),1);
       assertEquals(board.getDice(0,0).getColorDice(),Dice.ColorDice.RED);

       assertEquals(board.getDice(0,1).getDiceNumber(),2);
       assertEquals(board.getDice(0,1).getColorDice(), Dice.ColorDice.YELLOW);

       assertEquals(board.getDice(0,2).getDiceNumber(),3);
       assertEquals(board.getDice(0,2).getColorDice(), Dice.ColorDice.BLUE);

       assertEquals(board.getDice(1,3).getDiceNumber(),4);
       assertEquals(board.getDice(1,3).getColorDice(), Dice.ColorDice.GREEN);

       assertEquals(board.getDice(1,1).getDiceNumber(),5);
       assertEquals(board.getDice(1,1).getColorDice(), Dice.ColorDice.PURPLE);

       assertEquals(board.getDice(2,0).getDiceNumber(),6);
       assertEquals(board.getDice(2,0).getColorDice(), Dice.ColorDice.RED);

       assertEquals(draftPool.getDiceByID(12).getColorDice(), Dice.ColorDice.YELLOW);
       assertEquals(draftPool.getDiceByID(12).getDiceNumber(), 2);


    }


}
