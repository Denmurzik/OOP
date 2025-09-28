import java.util.Map;

/**
 * Представляет операцию умножения двух выражений.
 */
public final class Mul extends BinaryOperator {

    public Mul(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) * right.eval(variables);
    }

    @Override
    public Expression derivative(String var) {
        // Правило произведения
        return new Add(
                new Mul(left.derivative(var), right),
                new Mul(left, right.derivative(var))
        );
    }

    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        // x * 0 = 0 или 0 * x = 0
        if ((simplifiedLeft instanceof Number && ((Number) simplifiedLeft).getValue() == 0)
                || (simplifiedRight instanceof Number
                && ((Number) simplifiedRight).getValue() == 0)) {
            return new Number(0);
        }
        // x * 1 = x
        if (simplifiedLeft instanceof Number && ((Number) simplifiedLeft).getValue() == 1) {
            return simplifiedRight;
        }
        // 1 * x = x
        if (simplifiedRight instanceof Number && ((Number) simplifiedRight).getValue() == 1) {
            return simplifiedLeft;
        }
        //  Свертка констант
        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            double val1 = ((Number) simplifiedLeft).getValue();
            double val2 = ((Number) simplifiedRight).getValue();
            return new Number(val1 * val2);
        }

        return new Mul(simplifiedLeft, simplifiedRight);
    }

    @Override
    public String toString() {
        return String.format("(%s * %s)", left, right);
    }
}