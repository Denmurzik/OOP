import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.util.Map;


/**
 * Юнит-тесты для всей функциональности работы с выражениями.
 */
class ExpressionTest {

    private final Map<String, Double> vars = Map.of("x", 2.0,
            "y", 4.0, "z", 5.0);

    @Test
    void testParsingAndToString() {
        String exprStr = "10 * (x + 3) - y / 2";
        Expression e = ExpressionParser.parse(exprStr);
        assertEquals("((10 * (x + 3)) - (y / 2))", e.toString());
    }

    @Test
    void testUnaryMinusParsing() {
        Expression unary = ExpressionParser.parse("-5 + x");
        assertEquals("((0 - 5) + x)", unary.toString());
    }

    @Test
    void testEvaluation() {
        Expression e = ExpressionParser.parse("10 * (x + 3) - y / 2");
        assertEquals(48.0, e.eval(vars), 1e-9);

        Expression unary = ExpressionParser.parse("-5 + x");
        assertEquals(-3.0, unary.eval(vars), 1e-9);
    }

    @Test
    void testEvaluationWithMissingVariable() {
        Expression e = ExpressionParser.parse("x + a");
        assertThrows(IllegalArgumentException.class, () -> e.eval(vars));
    }

    @Test
    void testDivisionByZeroEvaluation() {
        Expression e = ExpressionParser.parse("x / (y - 4)");
        assertThrows(ArithmeticException.class, () -> e.eval(vars));
    }

    @Test
    void testDerivative() {
        // d/dx (3 + 2*x) = 0 + (0*x + 2*1) = 2
        Expression e = ExpressionParser.parse("3 + 2 * x");
        Expression derivative = e.derivative("x");
        assertEquals("(0 + ((0 * x) + (2 * 1)))", derivative.toString());
    }

    @Test
    void testSimplifyDerivative() {
        // d/dx (3 + 2*x) simplifies to 2
        Expression e = ExpressionParser.parse("3 + 2 * x");
        Expression simplifiedDerivative = e.derivative("x").simplify();
        assertEquals("2", simplifiedDerivative.toString());
        assertTrue(simplifiedDerivative instanceof Number);
        assertEquals(2.0, ((Number) simplifiedDerivative).getValue(), 1e-9);
    }

    @Test
    void testSimplifyRuleConstantFolding() {
        // 5 * 4 + 3 * 2 -> 20 + 6 -> 26
        Expression e = ExpressionParser.parse("5 * 4 + 3 * 2");
        assertEquals("26", e.simplify().toString());
    }

    @Test
    void testSimplifyRuleMulByZero() {
        // (x + 5) * 0 -> 0
        Expression e = ExpressionParser.parse("(x + 5) * 0");
        assertEquals("0", e.simplify().toString());
    }

    @Test
    void testSimplifyRuleMulByOne() {
        // 1 * (y - 2) -> (y - 2)
        Expression e = ExpressionParser.parse("1 * (y - 2)");
        assertEquals("(y - 2)", e.simplify().toString());
    }

    @Test
    void testSimplifyRuleSubSame() {
        // (2 * z) - (2 * z) -> 0
        Expression e = ExpressionParser.parse("(2 * z) - (2 * z)");
        assertEquals("0", e.simplify().toString());
    }

    @Test
    void testComplexSimplification() {
        // d/dx (x * y) = 1 * y + x * 0 -> y + 0 -> y
        Expression e = ExpressionParser.parse("x * y");
        Expression derivative = e.derivative("x");
        assertEquals("((1 * y) + (x * 0))", derivative.toString());

        Expression simplified = derivative.simplify();
        assertEquals("y", simplified.toString());
    }

    @Test
    void testInvalidExpressionParsing() {
        // Несогласованные скобки
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("(x + 5"));
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("x + 5)"));
        // Неверный оператор
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("x ++ 5"));
    }

    @Test
    void testParsingWithNegativeNumber() {
        Expression e = ExpressionParser.parse("x + (-3)");
        assertEquals("(x + (0 - 3))", e.toString());
        assertEquals(-1.0, e.eval(vars), 1e-9);
    }

    @Test
    void testDirectVariableDerivative() {
        assertEquals(new Number(1), new Variable("x").derivative("x"));
        assertEquals(new Number(0), new Variable("x").derivative("y"));
    }

    @Test
    void testDerivativeOfConstantExpression() {
        Expression e = new Add(new Number(3), new Number(5));
        Expression derivative = e.derivative("x");
        assertEquals(new Number(0), derivative.simplify());
    }

    @Test
    void testMultipleDifferentiation() {
        Expression e = new Variable("x");
        Expression de1 = e.derivative("x");
        Expression de2 = de1.derivative("x");
        assertEquals(new Number(1), de1);
        assertEquals(new Number(0), de2);
    }

    @Test
    void testChainRuleDerivative() {
        // f(x) = (x^2 + 3x)
        Expression inner = ExpressionParser.parse("x*x + 3*x");

        // f(x)^3
        Expression fCubed = new Mul(inner, new Mul(inner, inner));

        Expression actualDerivative = fCubed.derivative("x");

        Map<String, Double> vars = Map.of("x", 2.0);


        double expectedValue = 2100.0;


        assertEquals(expectedValue, actualDerivative.eval(vars), 1e-9);
    }

    @Test
    void testEvaluationScenarios() {
        Expression e = ExpressionParser.parse("x*y + z/2"); // (x*y) + (z/2)

        // Стандартный тест
        assertEquals(17.0, e.eval(Map.of("x", 3.0, "y", 4.0, "z", 10.0)), 1e-9);
        assertEquals(4.0, e.eval(Map.of("x", 0.0, "y", 100.0, "z", 8.0)), 1e-9);

        // Переменные в разном порядке
        assertEquals(17.0, e.eval(Map.of("z", 10.0, "x", 3.0, "y", 4.0)), 1e-9);

        // Лишние переменные
        assertEquals(17.0, e.eval(Map.of("x", 3.0, "y", 4.0, "z", 10.0, "w", 100.0)), 1e-9);

        // Недостаточно переменных
        assertThrows(IllegalArgumentException.class, () -> e.eval(Map.of("x", 3.0, "y", 4.0)));
    }
}