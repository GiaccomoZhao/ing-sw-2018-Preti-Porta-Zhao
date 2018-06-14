package porprezhas.model.cards;

import org.junit.Before;
import org.junit.Test;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static porprezhas.model.cards.ToolCardParam.IncDec.*;

public class IncDecTest {

    @Test
    public void toBooleanStaticTest() {
        assertTrue(toBoolean(INCREMENT));
        assertFalse(toBoolean(DECREMENT));
    }

    @Test
    public void toIntegerTest() {
        assertNotEquals(new Integer(0), INCREMENT.toInteger());
        assertEquals(new Integer(0), DECREMENT.toInteger());
    }

    @Test
    public void toBooleanTest() {
        assertEquals(TRUE,  INCREMENT.toBoolean());
        assertEquals(FALSE, DECREMENT.toBoolean());
    }

    @Test
    public void fromIntegerTest() {
        assertEquals( DECREMENT, fromInteger(0) );

        assertEquals( INCREMENT, fromInteger(1) );
        assertEquals( INCREMENT, fromInteger(-1) );
        assertEquals( INCREMENT, fromInteger(0xFFFFF) );
        assertEquals( INCREMENT, fromInteger(-0xFFFFFFFF) );
        assertEquals( INCREMENT, fromInteger(0xFFFFFFFF) );
        assertEquals( INCREMENT, fromInteger(Integer.MIN_VALUE) );
        assertEquals( INCREMENT, fromInteger(Integer.MAX_VALUE) );
    }

    @Test
    public void fromIntegerToBooleanTest() {
        assertEquals(FALSE, fromIntegerToBoolean(0) );

        assertEquals(TRUE, fromIntegerToBoolean(1) );
        assertEquals(TRUE, fromIntegerToBoolean(0xFFFF) );
        assertEquals(TRUE, fromIntegerToBoolean(0xFFFFFFFF) );
        assertEquals(TRUE, fromIntegerToBoolean(-0xFFFFFFFF) );
        assertEquals(TRUE, fromIntegerToBoolean(-1) );
        assertEquals(TRUE, fromIntegerToBoolean(Integer.MIN_VALUE) );
        assertEquals(TRUE, fromIntegerToBoolean(Integer.MAX_VALUE) );

    }
}
