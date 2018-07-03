package porprezhas.model.dices;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import static porprezhas.model.dices.CellPosition.MAX_ROW;
import static porprezhas.model.dices.CellPosition.MAX_COLUMN;
import static porprezhas.model.dices.CellPosition.MIN_ROW;
import static porprezhas.model.dices.CellPosition.MIN_COLUMN;



public class CellPositionTest {

     int row;
     int col;
     long seed;


    @Test
    public void setUpCellTest(){

         row = 3;
         col = 2;

         CellPosition cellPosition = new CellPosition(row,col);

         assertTrue(cellPosition.equals(row,col));
         assertFalse(cellPosition.equals(row-1,col-1));

         assertEquals(cellPosition.getCol(),col);
         assertEquals(cellPosition.getRow(),row);

         int row2 = 1;
         int col2 = 3;

         cellPosition.setCol(col2);
         cellPosition.setRow(row2);

         assertEquals(cellPosition.getRow(),row2);
         assertEquals(cellPosition.getCol(),col2);

         assertFalse(cellPosition.equals(row,col2));
         assertFalse(cellPosition.equals(row2,col));
    }

    @Test
    public void borderTestTrue(){

        row = 0;
        col = 2;

        CellPosition cellPosition = new CellPosition(row,col);
        assertTrue(cellPosition.isBorderPosition(row,col));

        row = 1;
        col = 4;

        CellPosition cellPosition2 = new CellPosition(row,col);
        assertTrue(cellPosition2.isBorderPosition(row,col));

    }

    @Test
    public void borderTestFalse(){

        row = 1;
        col = 2;

        CellPosition cellPosition = new CellPosition(row,col);
        assertFalse(cellPosition.isBorderPosition(row,col));
    }

    @Test
    public void getRandomInnerValueTest(){

        seed = 10;
        CellPosition position;

        position = CellPosition.getRandomInnerValue(seed);

       assertTrue((position.getRow()<MAX_ROW && position.getRow()>MIN_ROW));
       assertTrue((position.getCol()<MAX_COLUMN && position.getCol()>MIN_COLUMN));
    }

    @Test
    public void getRandomBorderValue(){

        seed = 10;
        CellPosition position;

        position = CellPosition.getRandomBorderValue(seed);

        assertTrue(position.getRow()==0 || position.getRow()==3
                || position.getCol()==0 || position.getCol()==4 );

    }

    @Test
    public void getTopBorderValue(){

        seed = 10;
        CellPosition position;

        position = CellPosition.getRandomBorderValue(seed, CellPosition.Bound.TOP);

        assertTrue(position.getRow() == MIN_ROW);

    }

    @Test
    public void getBottomBorderValue(){

        seed = 10;
        CellPosition position;

        position = CellPosition.getRandomBorderValue(seed, CellPosition.Bound.BOTTOM);

        assertTrue(position.getRow() == MAX_ROW);

    }

    @Test
    public void getLeftBorderValue(){

        seed = 10;
        CellPosition position;

        position = CellPosition.getRandomBorderValue(seed, CellPosition.Bound.LEFT);

        assertTrue(position.getCol() == MIN_COLUMN);

    }

    @Test
    public void getRightBorderValue(){

        seed = 10;
        CellPosition position;

        position = CellPosition.getRandomBorderValue(seed, CellPosition.Bound.RIGHT);

        assertTrue(position.getCol() == MAX_COLUMN);

    }
}
