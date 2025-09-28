import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты специально для класса Div.
 */
class DivTest {

    private Expression number6;
    private Expression number2;
    private Expression variableX;
    private Expression zero;
    private Expression one;

    @BeforeEach
    void setUp() {
        number6 = new Number(6);
        number2 = new Number(2);
        variableX = new Variable("x");
        zero = new Number(0);
        one = new Number(1);
    }

    @Test
    void testEval() {
        Expression divNumbers = new Div(number6, number2);
        assertEquals(3.0, divNumbers.eval(Map.of()), 1e-9);

        Expression divVarByNum = new Div(variableX, number2);
        assertEquals(5.0, divVarByNum.eval(Map.of("x", 10.0)), 1e-9);
    }

    @Test
    void testEvalDivisionByZero() {
        Expression divByZero = new Div(number6, zero);
        assertThrows(ArithmeticException.class, () -> divByZero.eval(Map.of()));

        Expression divVarByZero = new Div(variableX, new Sub(variableX, new Number(10)));
        assertThrows(ArithmeticException.class, () -> divVarByZero.eval(Map.of("x", 10.0)));
    }

    @Test
    void testToString() {
        Expression div = new Div(variableX, number2);
        assertEquals("(x / 2)", div.toString());
    }

    @Test
    void testDerivative() {
        // d/dx (x / 2) -> (1*2 - x*0) / 2^2
        Expression div = new Div(variableX, number2);
        Expression derivative = div.derivative("x");

        // Проверяем, что результат после упрощения равен 0.5
        assertEquals(0.5, derivative.simplify().eval(Map.of()), 1e-9);
    }

    @Test
    void testSimplifyZeroDividedByX() {
        Expression div = new Div(zero, variableX);
        assertEquals(zero, div.simplify());
    }

    @Test
    void testSimplifyXDividedByOne() {
        Expression div = new Div(variableX, one);
        assertEquals(variableX, div.simplify());
    }

    @Test
    void testSimplifyXDividedByX() {
        Expression div = new Div(variableX, new Variable("x"));
        assertEquals(one, div.simplify());

        Expression divZeroByZero = new Div(zero, zero);
        assertEquals(divZeroByZero, divZeroByZero.simplify());
    }

    @Test
    void testSimplifyConstantFolding() {
        Expression div = new Div(number6, number2);
        Expression simplified = div.simplify();

        assertTrue(simplified instanceof Number);
        assertEquals(3.0, ((Number) simplified).getValue(), 1e-9);
    }

    @Test
    void testSimplifyDivisionByZeroNoChange() {
        Expression divByZero = new Div(number6, zero);
        Expression simplified = divByZero.simplify();

        assertTrue(simplified instanceof Div);
        assertEquals(number6, ((Div) simplified).left);
        assertEquals(zero, ((Div) simplified).right);
    }
}