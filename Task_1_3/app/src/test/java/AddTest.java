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
        // Инициализируем общие для тестов выражения перед каждым тестом
        number2 = new Number(2);
        number3 = new Number(3);
        variableX = new Variable("x");
        zero = new Number(0);
    }

    @Test
    void testEval() {
        // Тест: 2 + 3 = 5
        Expression addNumbers = new Add(number2, number3);
        assertEquals(5.0, addNumbers.eval(Map.of()), 1e-9);

        // Тест: x + 3 при x=10 -> 13
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
        // d/dx (x + 2) -> 1 + 0
        Expression add = new Add(variableX, number2);
        Expression derivative = add.derivative("x");

        // Проверяем, что структура производной верна
        assertTrue(derivative instanceof Add);
        assertEquals(new Number(1), ((Add) derivative).left);
        assertEquals(new Number(0), ((Add) derivative).right);

        // Проверяем результат после упрощения
        assertEquals(new Number(1), derivative.simplify());
    }

    @Test
    void testSimplifyAddZero() {
        // Правило: x + 0 -> x
        Expression addToZero = new Add(variableX, zero);
        assertEquals(variableX, addToZero.simplify());

        // Правило: 0 + x -> x
        Expression addFromZero = new Add(zero, variableX);
        assertEquals(variableX, addFromZero.simplify());
    }

    @Test
    void testSimplifyConstantFolding() {
        // Правило: 2 + 3 -> 5
        Expression add = new Add(number2, number3);
        Expression simplified = add.simplify();

        assertTrue(simplified instanceof Number);
        assertEquals(5.0, ((Number) simplified).getValue(), 1e-9);
    }

    @Test
    void testSimplifyNoChange() {
        // Сложение двух переменных не упрощается
        Expression addVars = new Add(new Variable("x"), new Variable("y"));
        Expression simplified = addVars.simplify();

        // Проверяем, что результат все еще является объектом Add
        assertTrue(simplified instanceof Add);
        // Проверяем, что дочерние элементы остались теми же (т.к. они не упрощаются)
        assertEquals(new Variable("x"), ((Add) simplified).left);
        assertEquals(new Variable("y"), ((Add) simplified).right);
    }
}