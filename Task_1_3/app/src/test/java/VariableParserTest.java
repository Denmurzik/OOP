import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.Test;


/**
 * Юнит-тесты VariableParser.
 */
class VariableParserTest {

    @Test
    void testStandardParsing() {
        String assignments = "x=10; y=13.5";
        Map<String, Double> expected = Map.of("x", 10.0, "y", 13.5);
        assertEquals(expected, VariableParser.parseVariables(assignments));
    }

    @Test
    void testEmptyAndNullInput() {
        // Пустая строка должна возвращать пустую карту
        assertTrue(VariableParser.parseVariables("").isEmpty());
        // Строка с пробелами должна возвращать пустую карту
        assertTrue(VariableParser.parseVariables("   ").isEmpty());
        // null должен возвращать пустую карту
        assertTrue(VariableParser.parseVariables(null).isEmpty());
    }

    @Test
    void testRobustnessWithSpacesAndSemicolons() {
        // Лишние пробелы и разделители должны обрабатываться корректно
        String assignments = " x = 3 ; y = 4 ; ; ";
        Map<String, Double> expected = Map.of("x", 3.0, "y", 4.0);
        assertEquals(expected, VariableParser.parseVariables(assignments));
    }

    @Test
    void testThrowsExceptionForEmptyValues() {
        // Пустые значения
        assertThrows(IllegalArgumentException.class,
                () -> VariableParser.parseVariables("x=; y=5"));
        // Пустое имя переменной
        assertThrows(IllegalArgumentException.class,
                () -> VariableParser.parseVariables("x=5; =5"));
    }

    @Test
    void testThrowsExceptionForNonNumericValue() {
        // Не число в качестве значения
        assertThrows(IllegalArgumentException.class,
                () -> VariableParser.parseVariables("x=abc"));
    }

    @Test
    void testThrowsExceptionForInvalidFormat() {
        // Нет разделителя '='
        assertThrows(IllegalArgumentException.class,
                () -> VariableParser.parseVariables("x 5"));
        // Нет разделителя ';'
        assertThrows(IllegalArgumentException.class,
                () -> VariableParser.parseVariables("x=5 y=3"));
        // Нестандартные разделители
        assertThrows(IllegalArgumentException.class,
                () -> VariableParser.parseVariables("x=3, y=4"));
    }
}