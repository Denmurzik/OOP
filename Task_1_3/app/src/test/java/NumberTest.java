import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты специально для класса Number.
 */
class NumberTest {

    @Test
    void testGetValue() {
        Number num = new Number(42.5);
        assertEquals(42.5, num.getValue(), 1e-9);
    }

    @Test
    void testEval() {
        // Метод eval должен просто вернуть значение числа, игнорируя переменные
        Number num = new Number(123.0);
        // Проверяем с пустым набором переменных и с непустым
        assertEquals(123.0, num.eval(Map.of()), 1e-9);
        assertEquals(123.0, num.eval(Map.of("x", 10.0)), 1e-9);
    }

    @Test
    void testDerivative() {
        // Производная от любого числа (константы) всегда равна 0
        Number num = new Number(50);
        Expression derivative = num.derivative("x");

        assertTrue(derivative instanceof Number);
        assertEquals(0.0, ((Number) derivative).getValue(), 1e-9);
    }

    @Test
    void testSimplify() {
        // Число является простейшей формой, поэтому simplify должен вернуть сам объект
        Number num = new Number(7);
        assertSame(num, num.simplify(), "Simplify не должен изменять объект Number");
    }

    @Test
    void testToStringFormatting() {
        // Целые числа должны выводиться без ".0"
        Number intNum = new Number(15.0);
        assertEquals("15", intNum.toString());

        // Дробные числа должны выводиться как есть
        Number floatNum = new Number(15.5);
        assertEquals("15.5", floatNum.toString());

        // Ноль
        Number zero = new Number(0);
        assertEquals("0", zero.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        Number numA = new Number(10.0);
        Number numB = new Number(10.0);
        Number numC = new Number(20.0);
        Number numD = new Number(10.000000001); // Близкое, но не равное значение

        // Рефлексивность
        assertEquals(numA, numA);

        // Симметричность
        assertTrue(numA.equals(numB) && numB.equals(numA));
        assertEquals(numA.hashCode(), numB.hashCode());

        // Неравенство
        assertNotEquals(numA, numC);
        assertNotEquals(numA.hashCode(), numC.hashCode());

        // Точность
        assertNotEquals(numA, numD);

        // Сравнение с null и другими типами
        assertNotEquals(null, numA);
        assertNotEquals("10.0", numA);
    }
}