import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для всей функциональности работы с выражениями.
 */
class ExpressionTest {

    private final Map<String, Double> vars = Map.of("x", 2.0, "y", 4.0, "z", 5.0);

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
        // Производная переменной по себе
        assertEquals(new Number(1), new Variable("x").derivative("x"));
        // Производная переменной по другой переменной
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
        Expression de1 = e.derivative("x"); // 1
        Expression de2 = de1.derivative("x"); // 0
        assertEquals(new Number(1), de1);
        assertEquals(new Number(0), de2);
    }

    @Test
    void testChainRuleDerivative() {
        // f(x) = (x² + 3x)³
        // f'(x) = 3 * (x² + 3x)² * (2x + 3)
        Expression inner = new Add(
                new Mul(new Variable("x"), new Variable("x")),
                new Mul(new Number(3), new Variable("x"))
        );
        // f^3 = f * f * f
        Expression f_cubed = new Mul(inner, new Mul(inner, inner));

        // Ожидаемый результат d/dx(f^3) = 3 * f' * f^2
        Expression inner_derivative = inner.derivative("x").simplify(); // (2*x + 3)
        Expression expected = new Mul(
                new Number(3),
                new Mul(
                        inner_derivative,
                        new Mul(inner, inner)
                )
        );

        // Сравниваем упрощенные версии, так как структуры могут отличаться
        assertEquals(expected.simplify().toString(), f_cubed.derivative("x").simplify().toString());
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

    @Test
    void testParseVariablesRobustness() {
        // Лишние пробелы и разделители
        Map<String, Double> expected = Map.of("x", 3.0, "y", 4.0);
        assertEquals(expected, Main.parseVariables(" x = 3 ; y = 4 ; ; "));
    }

    @Test
    void testParseVariablesWithInvalidInput() {
        // Пустые значения
        assertThrows(IllegalArgumentException.class, () -> Main.parseVariables("x=; y=5"));
        assertThrows(IllegalArgumentException.class, () -> Main.parseVariables("x=5; =5"));

        // Не число
        assertThrows(IllegalArgumentException.class, () -> Main.parseVariables("x=abc"));

        // Нет разделителя
        assertThrows(IllegalArgumentException.class, () -> Main.parseVariables("x=5 y=3"));

        // Нестандартные разделители
        assertThrows(IllegalArgumentException.class, () -> Main.parseVariables("x=3, y=4"));
    }
}