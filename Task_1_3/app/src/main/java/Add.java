import java.util.Map;

/**
 * Представляет операцию сложения двух выражений.
 */
public final class Add extends BinaryOperator {

    public Add(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) + right.eval(variables);
    }

    @Override
    public Expression derivative(String var) {
        // Правило: (u + v)' = u' + v'
        return new Add(left.derivative(var), right.derivative(var));
    }

    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        // x + 0 = x
        if (simplifiedRight instanceof Number && ((Number) simplifiedRight).getValue() == 0) {
            return simplifiedLeft;
        }
        // 0 + x = x
        if (simplifiedLeft instanceof Number && ((Number) simplifiedLeft).getValue() == 0) {
            return simplifiedRight;
        }
        //  Свертка констант ( 2 + 3 = 5)
        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            double val1 = ((Number) simplifiedLeft).getValue();
            double val2 = ((Number) simplifiedRight).getValue();
            return new Number(val1 + val2);
        }

        return new Add(simplifiedLeft, simplifiedRight);
    }

    @Override
    public String toString() {
        return String.format("(%s + %s)", left, right);
    }
}