import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Юнит-тесты для класса Add.
 */
class AddTest {

    private Expression number2;
    private Expression number3;
    private Expression variableX;
    private Expression zero;

    @BeforeEach
    void setUp() {
        number2 = new Number(2);
        number3 = new Number(3);
        variableX = new Variable("x");
        zero = new Number(0);
    }

    @Test
    void testEval() {
        Expression addNumbers = new Add(number2, number3);
        assertEquals(5.0, addNumbers.eval(Map.of()), 1e-9);

        Expression addVarAndNum = new Add(variableX, number3);
        assertEquals(13.0, addVarAndNum.eval(Map.of("x", 10.0)), 1e-9);
    }

    @Test
    void testToString() {
        Expression add = new Add(variableX, number2);
        assertEquals("(x + 2)", add.toString());
    }

    @Test
    void testDerivative() {

        Expression add = new Add(variableX, number2);
        Expression derivative = add.derivative("x");

        assertTrue(derivative instanceof Add);
        assertEquals(new Number(1), ((Add) derivative).left);
        assertEquals(new Number(0), ((Add) derivative).right);

        assertEquals(new Number(1), derivative.simplify());
    }

    @Test
    void testSimplifyAddZero() {
        Expression addToZero = new Add(variableX, zero);
        assertEquals(variableX, addToZero.simplify());

        Expression addFromZero = new Add(zero, variableX);
        assertEquals(variableX, addFromZero.simplify());
    }

    @Test
    void testSimplifyConstantFolding() {
        Expression add = new Add(number2, number3);
        Expression simplified = add.simplify();

        assertTrue(simplified instanceof Number);
        assertEquals(5.0, ((Number) simplified).getValue(), 1e-9);
    }

    @Test
    void testSimplifyNoChange() {
        Expression addVars = new Add(new Variable("x"), new Variable("y"));
        Expression simplified = addVars.simplify();

        assertTrue(simplified instanceof Add);
        assertEquals(new Variable("x"), ((Add) simplified).left);
        assertEquals(new Variable("y"), ((Add) simplified).right);
    }
}