import java.util.Map;

/**
 * Операцию деления двух выражений.
 */
public final class Div extends BinaryOperator {

    public Div(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double eval(Map<String, Double> variables) {
        double divisor = right.eval(variables);
        if (divisor == 0) {
            throw new ArithmeticException("Деление на ноль.");
        }
        return left.eval(variables) / divisor;
    }

    @Override
    public Expression derivative(String var) {
        // Правило частного
        Expression numerator = new Sub(
                new Mul(left.derivative(var), right),
                new Mul(left, right.derivative(var))
        );
        Expression denominator = new Mul(right, right);
        return new Div(numerator, denominator);
    }

    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();


        // x / x = 1
        if (simplifiedLeft.equals(simplifiedRight)) {
            if (simplifiedLeft instanceof Number && ((Number)simplifiedLeft).getValue() == 0) {
                return new Div(simplifiedLeft, simplifiedRight);
            }
            return new Number(1);
        }

        // 0 / x = 0
        if (simplifiedLeft instanceof Number && ((Number) simplifiedLeft).getValue() == 0) {
            return new Number(0);
        }

        // x / 1 = x
        if (simplifiedRight instanceof Number && ((Number) simplifiedRight).getValue() == 1) {
            return simplifiedLeft;
        }

        // Свертка констант (6 / 3 = 2)
        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            double val1 = ((Number) simplifiedLeft).getValue();
            double val2 = ((Number) simplifiedRight).getValue();
            if (val2 == 0) {
                return new Div(simplifiedLeft, simplifiedRight);
            }
            return new Number(val1 / val2);
        }

        return new Div(simplifiedLeft, simplifiedRight);
    }

    @Override
    public String toString() {
        return String.format("(%s / %s)", left, right);
    }
}