import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для класса Number.
 */
class NumberTest {

    @Test
    void testGetValue() {
        Number num = new Number(42.5);
        assertEquals(42.5, num.getValue(), 1e-9);
    }

    @Test
    void testEval() {
        Number num = new Number(123.0);
        assertEquals(123.0, num.eval(Map.of()), 1e-9);
        assertEquals(123.0, num.eval(Map.of("x", 10.0)), 1e-9);
    }

    @Test
    void testDerivative() {
        Number num = new Number(50);
        Expression derivative = num.derivative("x");

        assertTrue(derivative instanceof Number);
        assertEquals(0.0, ((Number) derivative).getValue(), 1e-9);
    }

    @Test
    void testSimplify() {
        Number num = new Number(7);
        assertSame(num, num.simplify(), "Simplify не должен изменять объект Number");
    }

    @Test
    void testToStringFormatting() {
        Number intNum = new Number(15.0);
        assertEquals("15", intNum.toString());

        Number floatNum = new Number(15.5);
        assertEquals("15.5", floatNum.toString());

        Number zero = new Number(0);
        assertEquals("0", zero.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        Number numA = new Number(10.0);
        Number numB = new Number(10.0);
        Number numC = new Number(20.0);
        Number numD = new Number(10.000000001);

        assertEquals(numA, numA);

        assertTrue(numA.equals(numB) && numB.equals(numA));
        assertEquals(numA.hashCode(), numB.hashCode());

        assertNotEquals(numA, numC);
        assertNotEquals(numA.hashCode(), numC.hashCode());

        assertNotEquals(numA, numD);

        assertNotEquals(null, numA);
        assertNotEquals("10.0", numA);
    }
}