package porprezhas.model.dices;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RestrictionTest {

    @Test
    public void RestrictionAllTest() {
        assertTrue(Board.Restriction.ALL.hasAdjacentRestriction());
        assertTrue(Board.Restriction.ALL.hasNumberRestriction());
        assertTrue(Board.Restriction.ALL.hasColorRestriction());
    }


    @Test
    public void RestrictionDiceTest() {
        assertTrue(Board.Restriction.DICE.hasColorRestriction());
        assertTrue(Board.Restriction.DICE.hasNumberRestriction());
        assertFalse(Board.Restriction.DICE.hasAdjacentRestriction());
    }

    @Test
    public void RestrictionColorTest() {
        assertTrue(Board.Restriction.COLOR.hasColorRestriction());
        assertFalse(Board.Restriction.COLOR.hasAdjacentRestriction());
        assertFalse(Board.Restriction.COLOR.hasNumberRestriction());
    }
    @Test
    public void RestrictionNoColorTest() {
        assertFalse(Board.Restriction.WITHOUT_COLOR.hasColorRestriction());
        assertTrue( Board.Restriction.WITHOUT_COLOR.hasAdjacentRestriction());
        assertTrue( Board.Restriction.WITHOUT_COLOR.hasNumberRestriction());
    }

    @Test
    public void RestrictionAdjacentTest() {
        assertTrue( Board.Restriction.ADJACENT.hasAdjacentRestriction());
        assertFalse(Board.Restriction.ADJACENT.hasColorRestriction());
        assertFalse(Board.Restriction.ADJACENT.hasNumberRestriction());
    }

    @Test
    public void RestrictionNoAdjacentTest() {
        assertFalse(Board.Restriction.WITHOUT_ADJACENT.hasAdjacentRestriction());
        assertTrue(Board.Restriction.WITHOUT_ADJACENT.hasColorRestriction());
        assertTrue(Board.Restriction.WITHOUT_ADJACENT.hasNumberRestriction());
    }

    @Test
    public void RestrictionNoneTest() {
        assertFalse(Board.Restriction.NONE.hasAdjacentRestriction());
        assertFalse(Board.Restriction.NONE.hasNumberRestriction());
        assertFalse(Board.Restriction.NONE.hasColorRestriction());
    }

    @Test
    public void RestrictionAndOperationTest() {
        assertEquals(Board.Restriction.ADJACENT, Board.Restriction.ALL.     and( Board.Restriction.ADJACENT ) );
        assertEquals(Board.Restriction.ADJACENT, Board.Restriction.ADJACENT.and( Board.Restriction.ADJACENT ) );
        assertEquals(Board.Restriction.NONE,     Board.Restriction.COLOR.   and( Board.Restriction.ADJACENT ) );
        assertEquals(Board.Restriction.ALL,      Board.Restriction.ALL.     and( Board.Restriction.ALL ) );
        assertEquals(Board.Restriction.NONE,     Board.Restriction.NONE.    and( Board.Restriction.NONE ) );
        assertEquals(Board.Restriction.NONE,     Board.Restriction.DICE.    and( Board.Restriction.ADJACENT ) );
        assertEquals(Board.Restriction.NUMBER,     Board.Restriction.DICE.    and( Board.Restriction.NUMBER ) );
    }

}
