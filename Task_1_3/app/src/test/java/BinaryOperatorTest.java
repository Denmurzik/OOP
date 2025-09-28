import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


/**
 * Юнит-тесты для абстрактного класса BinaryOperator.
 * Тестирование проводится через его конкретные реализации (Add, Sub).
 */
class BinaryOperatorTest {

    private final Expression num1 = new Number(1);
    private final Expression num2 = new Number(2);
    private final Expression varX = new Variable("x");

    @Test
    void testConstructorThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Add(num1, null));
        assertThrows(NullPointerException.class, () -> new Add(null, num2));
        assertThrows(NullPointerException.class, () -> new Add(null, null));
    }

    @Test
    void testEqualsReflexivity() {
        Expression op = new Add(num1, num2);
        assertEquals(op, op);
    }

    @Test
    void testEqualsSymmetry() {
        Expression op1 = new Add(varX, num1);
        Expression op2 = new Add(varX, num1);
        assertTrue(op1.equals(op2) && op2.equals(op1));
        assertEquals(op1.hashCode(), op2.hashCode());
    }

    @Test
    void testEqualsWithDifferentOperands() {
        Expression op1 = new Add(varX, num1);
        Expression op2 = new Add(varX, num2);
        assertNotEquals(op1, op2);
    }

    @Test
    void testEqualsWithDifferentOperatorTypes() {
        Expression add = new Add(num1, num2);
        Expression sub = new Sub(num1, num2);
        assertNotEquals(add, sub);
    }

    @Test
    void testEqualsWithNull() {
        Expression op = new Add(num1, num2);
        assertNotEquals(null, op);
    }

    @Test
    void testEqualsWithDifferentClassObject() {
        Expression op = new Add(num1, num2);
        String otherObject = "(1 + 2)";
        assertNotEquals(op, otherObject);
    }
}