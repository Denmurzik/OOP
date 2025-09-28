import java.util.Map;

/**
 * Представляет операцию вычитания двух выражений.
 */
public final class Sub extends BinaryOperator {

    public Sub(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) - right.eval(variables);
    }

    @Override
    public Expression derivative(String var) {
        // Правило: (u - v)' = u' - v' [cite: 9]
        return new Sub(left.derivative(var), right.derivative(var));
    }

    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        // x - 0 = x
        if (simplifiedRight instanceof Number && ((Number) simplifiedRight).getValue() == 0) {
            return simplifiedLeft;
        }
        // x - x = 0
        if (simplifiedLeft.equals(simplifiedRight)) {
            return new Number(0);
        }
        // Свертка констант
        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            double val1 = ((Number) simplifiedLeft).getValue();
            double val2 = ((Number) simplifiedRight).getValue();
            return new Number(val1 - val2);
        }

        return new Sub(simplifiedLeft, simplifiedRight);
    }

    @Override
    public String toString() {
        return String.format("(%s - %s)", left, right);
    }
}