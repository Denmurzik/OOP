import java.util.Map;
import java.util.Objects;

/**
 * Представляет переменную в выражении.
 */
public final class Variable extends Expression {

    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public double eval(Map<String, Double> variables) {
        if (!variables.containsKey(name)) {
            throw new IllegalArgumentException("Переменная '" + name
                    + "' не найдена в переданном списке.");
        }
        return variables.get(name);
    }

    @Override
    public Expression derivative(String var) {
        if (this.name.equals(var)) {
            return new Number(1);
        } else {
            return new Number(0);
        }
    }

    @Override
    public Expression simplify() {
        return this;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Variable variable = (Variable) obj;
        return name.equals(variable.name);
    }

}