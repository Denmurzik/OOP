import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Юнит-тесты класса Sub.
 */
class SubTest {

    private Expression number5;
    private Expression number2;
    private Expression variableX;
    private Expression zero;

    @BeforeEach
    void setUp() {
        // Инициализируем общие для тестов выражения перед каждым тестом
        number5 = new Number(5);
        number2 = new Number(2);
        variableX = new Variable("x");
        zero = new Number(0);
    }

    @Test
    void testEval() {
        // Тест: 5 - 2 = 3
        Expression subNumbers = new Sub(number5, number2);
        assertEquals(3.0, subNumbers.eval(Map.of()), 1e-9);

        // Тест: x - 2 при x=10 -> 8
        Expression subVarAndNum = new Sub(variableX, number2);
        assertEquals(8.0, subVarAndNum.eval(Map.of("x", 10.0)), 1e-9);
    }

    @Test
    void testToString() {
        Expression sub = new Sub(variableX, number2);
        assertEquals("(x - 2)", sub.toString());
    }

    @Test
    void testDerivative() {
        // d/dx (x - 2) -> 1 - 0
        Expression sub = new Sub(variableX, number2);
        Expression derivative = sub.derivative("x");

        // Проверяем, что результат после упрощения равен 1
        assertEquals(1.0, derivative.simplify().eval(Map.of()), 1e-9);
    }

    @Test
    void testSimplifySubZero() {
        // Правило: x - 0 -> x
        Expression subZero = new Sub(variableX, zero);
        assertEquals(variableX, subZero.simplify());
    }

    @Test
    void testSimplifySubSame() {
        // Правило: x - x -> 0
        Expression subSame = new Sub(variableX, new Variable("x"));
        assertEquals(zero, subSame.simplify());

        // Проверка с более сложным выражением: (2*x) - (2*x) -> 0
        Expression complexExpr = new Mul(number2, variableX);
        Expression subComplexSame = new Sub(complexExpr, complexExpr);
        assertEquals(zero, subComplexSame.simplify());
    }

    @Test
    void testSimplifyConstantFolding() {
        // Правило: 5 - 2 -> 3
        Expression sub = new Sub(number5, number2);
        Expression simplified = sub.simplify();

        assertTrue(simplified instanceof Number);
        assertEquals(3.0, ((Number) simplified).getValue(), 1e-9);
    }

    @Test
    void testSimplifyNoChange() {
        Expression subVars = new Sub(new Variable("x"), new Variable("y"));
        Expression simplified = subVars.simplify();

        assertTrue(simplified instanceof Sub);
        assertEquals(new Variable("x"), ((Sub) simplified).left);
        assertEquals(new Variable("y"), ((Sub) simplified).right);
    }
}