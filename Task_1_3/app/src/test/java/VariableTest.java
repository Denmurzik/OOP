import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.util.Map;

/**
 * Variable.
 */
class VariableTest {

    @Test
    void testGetName() {
        Variable var = new Variable("myVar");
        assertEquals("myVar", var.getName());
    }

    @Test
    void testEval() {
        Variable varX = new Variable("x");
        Map<String, Double> variables = Map.of("x", 42.0, "y", 99.0);

        // Переменная 'x' должна успешно найти свое значение в карте
        assertEquals(42.0, varX.eval(variables), 1e-9);
    }

    @Test
    void testEvalThrowsExceptionWhenVariableNotFound() {
        Variable varZ = new Variable("z");
        Map<String, Double> variables = Map.of("x", 42.0, "y", 99.0);

        // Попытка вычислить значение переменной 'z', которой нет в карте, должна выбросить исключение
        assertThrows(IllegalArgumentException.class, () -> varZ.eval(variables));
    }

    @Test
    void testDerivative() {
        Variable varX = new Variable("x");

        // Производная переменной по себе самой равна 1
        Expression derivativeSame = varX.derivative("x");
        assertTrue(derivativeSame instanceof Number);
        assertEquals(1.0, ((Number) derivativeSame).getValue(), 1e-9);

        // Производная переменной по другой переменной равна 0
        Expression derivativeOther = varX.derivative("y");
        assertTrue(derivativeOther instanceof Number);
        assertEquals(0.0, ((Number) derivativeOther).getValue(), 1e-9);
    }

    @Test
    void testSimplify() {
        // Переменная является простейшей формой, поэтому simplify должен вернуть сам объект
        Variable var = new Variable("a");
        assertSame(var, var.simplify(), "Simplify не должен изменять объект Variable");
    }

    @Test
    void testToString() {
        Variable var = new Variable("alpha");
        assertEquals("alpha", var.toString());
    }

    @Test
    void testEquals() {
        Variable varA = new Variable("x");
        Variable varB = new Variable("x");
        Variable varC = new Variable("y");

        assertEquals(varA, varA);

        assertTrue(varA.equals(varB) && varB.equals(varA));

        assertNotEquals(varA, varC);


        assertNotEquals(null, varA);
        assertNotEquals("x", varA);
    }
}