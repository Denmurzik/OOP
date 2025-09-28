import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для класса ExpressionParser.
 */
class ExpressionParserTest {

    @Test
    void testSimpleAddition() {
        Expression e = ExpressionParser.parse("3 + 5");
        assertEquals("(3 + 5)", e.toString());
    }

    @Test
    void testOperatorPrecedence() {
        Expression e = ExpressionParser.parse("3 + 2 * x");
        assertEquals("(3 + (2 * x))", e.toString());
    }

    @Test
    void testLeftAssociativity() {
        Expression e = ExpressionParser.parse("10 - 5 + 3");
        assertEquals("((10 - 5) + 3)", e.toString());
    }

    @Test
    void testParenthesesOverridePrecedence() {
        Expression e = ExpressionParser.parse("(3 + 2) * x");
        assertEquals("((3 + 2) * x)", e.toString());
    }

    @Test
    void testComplexExpression() {
        String exprStr = "10 * (x + 3) - y / 2";
        Expression e = ExpressionParser.parse(exprStr);
        assertEquals("((10 * (x + 3)) - (y / 2))", e.toString());
    }

    @Test
    void testUnaryMinusAtStart() {
        Expression e = ExpressionParser.parse("-5 + x");
        assertEquals("((0 - 5) + x)", e.toString());
    }

    @Test
    void testUnaryMinusAfterOperator() {
        Expression e = ExpressionParser.parse("10 * -x");
        assertEquals("(10 * (0 - x))", e.toString());
    }

    @Test
    void testUnaryMinusInParentheses() {
        Expression e = ExpressionParser.parse("10 * (-x + 3)");
        assertEquals("(10 * ((0 - x) + 3))", e.toString());
    }

    @Test
    void testParseJustNumber() {
        Expression e = ExpressionParser.parse("123.45");
        assertTrue(e instanceof Number);
        assertEquals(123.45, ((Number) e).getValue(), 1e-9);
    }

    @Test
    void testParseJustVariable() {
        Expression e = ExpressionParser.parse("myVar");
        assertTrue(e instanceof Variable);
        assertEquals("myVar", ((Variable) e).getName());
    }

    @Test
    void testThrowsExceptionForMismatchedParentheses() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("(3 + 2 * x"));
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("3 + 2) * x"));
    }

    @Test
    void testThrowsExceptionForInvalidToken() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("3 # 5"));
    }

    @Test
    void testThrowsExceptionForConsecutiveOperators() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("3 + * 5"));
    }

    @Test
    void testThrowsExceptionForEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse(""));
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("   "));
    }

    @Test
    void testThrowsExceptionForIncompleteExpression() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse("5 +"));
    }
}