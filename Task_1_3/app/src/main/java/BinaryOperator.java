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
        if (obj == null || getClass() != obj.getClass()) return false;
        BinaryOperator that = (BinaryOperator) obj;
        return left.equals(that.left) && right.equals(that.right);
    }

}