import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Юнит-тесты класса Mul.
 */
class MulTest {

    private Expression number2;
    private Expression number3;
    private Expression variableX;
    private Expression zero;
    private Expression one;

    @BeforeEach
    void setUp() {
        number2 = new Number(2);
        number3 = new Number(3);
        variableX = new Variable("x");
        zero = new Number(0);
        one = new Number(1);
    }

    @Test
    void testEval() {
        // Тест: 2 * 3 = 6
        Expression mulNumbers = new Mul(number2, number3);
        assertEquals(6.0, mulNumbers.eval(Map.of()), 1e-9);

        // Тест: x * 3 при x=10 -> 30
        Expression mulVarAndNum = new Mul(variableX, number3);
        assertEquals(30.0, mulVarAndNum.eval(Map.of("x", 10.0)), 1e-9);
    }

    @Test
    void testToString() {
        Expression mul = new Mul(variableX, number2);
        assertEquals("(x * 2)", mul.toString());
    }

    @Test
    void testDerivative() {
        // d/dx (x * 2) -> (1 * 2) + (x * 0)
        Expression mul = new Mul(variableX, number2);
        Expression derivative = mul.derivative("x");

        // Проверяем, что результат после упрощения равен 2
        assertEquals(2.0, derivative.simplify().eval(Map.of()), 1e-9);
    }

    @Test
    void testSimplifyMulByZero() {
        // Правило: x * 0 -> 0
        Expression mulByZero = new Mul(variableX, zero);
        assertEquals(zero, mulByZero.simplify());

        // Правило: 0 * x -> 0
        Expression mulFromZero = new Mul(zero, variableX);
        assertEquals(zero, mulFromZero.simplify());
    }

    @Test
    void testSimplifyMulByOne() {
        // Правило: x * 1 -> x
        Expression mulByOne = new Mul(variableX, one);
        assertEquals(variableX, mulByOne.simplify());

        // Правило: 1 * x -> x
        Expression mulFromOne = new Mul(one, variableX);
        assertEquals(variableX, mulFromOne.simplify());
    }

    @Test
    void testSimplifyConstantFolding() {
        // Правило: 2 * 3 -> 6
        Expression mul = new Mul(number2, number3);
        Expression simplified = mul.simplify();

        assertTrue(simplified instanceof Number);
        assertEquals(6.0, ((Number) simplified).getValue(), 1e-9);
    }

    @Test
    void testSimplifyNoChange() {
        // Умножение двух переменных не упрощается
        Expression mulVars = new Mul(new Variable("x"), new Variable("y"));
        Expression simplified = mulVars.simplify();

        assertTrue(simplified instanceof Mul);
        assertEquals(new Variable("x"), ((Mul) simplified).left);
        assertEquals(new Variable("y"), ((Mul) simplified).right);
    }
}