import java.util.Objects;

/**
 * Класс для бинарных операций.
 */
public abstract class BinaryOperator extends Expression {

    protected final Expression left;
    protected final Expression right;

    protected BinaryOperator(Expression left, Expression right) {
        this.left = Objects.requireNonNull(left);
        this.right = Objects.requireNonNull(right);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        // Проверяем, что это один и тот же класс оператора (Add не может быть равен Sub)
        if (obj == null || getClass() != obj.getClass()) return false;
        BinaryOperator that = (BinaryOperator) obj;
        // Два оператора равны, если их операнды равны
        return left.equals(that.left) && right.equals(that.right);
    }

    @Override
    public int hashCode() {
        // Включаем имя класса в хэш, чтобы различать операторы
        return Objects.hash(left, right, getClass().getName());
    }
}