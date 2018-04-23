package porprezha.Model;
import porprezha.model.dices.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class DiceBagTest {

    DiceBag diceBag= new DiceBag();
    ArrayList<Dice> list;


    @Test
    public void testGeRandomDices(){

            list =diceBag.GetRandomDices(4);
            int sizeL= list.size();
            int sizeB= diceBag.diceBagSize();
            assertEquals(9, sizeL);
            assertEquals(81, sizeB);
    }



}
