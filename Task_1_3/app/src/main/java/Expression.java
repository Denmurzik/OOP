import java.util.Map;

/**
 * Класс представляющий математическое выражение.
 */
public abstract class Expression {

    /**
     * Вычисляет значение выражения, используя заданные значения переменных.
     *
     * @param variables (Map), где ключ - имя переменной, а значение - её числовое значение.
     * @return результат вычисления в виде double.
     */
    public abstract double eval(Map<String, Double> variables);

    /**
     * Выполняет символьное дифференцирование этого выражения по заданной переменной.
     *
     * @param var Имя переменной, по которой производится дифференцирование.
     * @return Новое дерево Expression, представляющее производную.
     */
    public abstract Expression derivative(String var);

    /**
     * Упрощает выражение согласно предопределенным правилам.
     *
     * @return Новое, упрощенное дерево Expression.
     */
    public abstract Expression simplify();


    @Override
    public abstract String toString();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();
}