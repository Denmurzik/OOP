import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
        // Конструктор должен выбрасывать исключение, если один из операндов null
        assertThrows(NullPointerException.class, () -> new Add(num1, null));
        assertThrows(NullPointerException.class, () -> new Add(null, num2));
        assertThrows(NullPointerException.class, () -> new Add(null, null));
    }

    @Test
    void testEqualsReflexivity() {
        // Объект должен быть равен самому себе
        Expression op = new Add(num1, num2);
        assertEquals(op, op);
    }

    @Test
    void testEqualsSymmetry() {
        // Два одинаковых объекта должны быть равны в обе стороны
        Expression op1 = new Add(varX, num1);
        Expression op2 = new Add(varX, num1);
        assertTrue(op1.equals(op2) && op2.equals(op1));
        assertEquals(op1.hashCode(), op2.hashCode());
    }

    @Test
    void testEqualsWithDifferentOperands() {
        // Объекты одного класса, но с разными операндами, не равны
        Expression op1 = new Add(varX, num1);
        Expression op2 = new Add(varX, num2); // другой правый операнд
        assertNotEquals(op1, op2);
    }

    @Test
    void testEqualsWithDifferentOperatorTypes() {
        // Объекты разных классов (Add vs Sub) не должны быть равны, даже с одинаковыми операндами
        Expression add = new Add(num1, num2);
        Expression sub = new Sub(num1, num2);
        assertNotEquals(add, sub);
    }

    @Test
    void testEqualsWithNull() {
        // Объект не должен быть равен null
        Expression op = new Add(num1, num2);
        assertNotEquals(null, op);
    }

    @Test
    void testEqualsWithDifferentClassObject() {
        // Объект не должен быть равен объекту совершенно другого класса
        Expression op = new Add(num1, num2);
        String otherObject = "(1 + 2)";
        assertNotEquals(op, otherObject);
    }

    @Test
    void testHashCodeConsistency() {
        // Хеш-код должен быть одинаковым для равных объектов
        Expression op1 = new Mul(varX, num1);
        Expression op2 = new Mul(varX, num1);
        assertEquals(op1.hashCode(), op2.hashCode());
    }

    @Test
    void testHashCodeDifference() {
        // Хеш-коды для разных объектов (в идеале) должны отличаться
        Expression add = new Add(num1, num2);
        Expression sub = new Sub(num1, num2); // Другой класс оператора
        Expression addDifferent = new Add(num2, num1); // Другой порядок операндов

        assertNotEquals(add.hashCode(), sub.hashCode());
        assertNotEquals(add.hashCode(), addDifferent.hashCode());
    }
}