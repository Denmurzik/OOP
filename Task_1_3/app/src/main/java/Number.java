import java.util.Map;
import java.util.Objects;

/**
 * Представляет числовую константу в выражении.
 * Это конечный узел (лист) в дереве выражений.
 */
public final class Number extends Expression {

    private final double value;

    public Number(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public double eval(Map<String, Double> variables) {
        return this.value;
    }

    @Override
    public Expression derivative(String var) {
        return new Number(0);
    }

    @Override
    public Expression simplify() {
        return this;
    }

    @Override
    public String toString() {
        if (value == (long) value) {
            return String.format("%d", (long) value);
        }
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Number number = (Number) obj;
        return Double.compare(number.value, value) == 0;
    }

}