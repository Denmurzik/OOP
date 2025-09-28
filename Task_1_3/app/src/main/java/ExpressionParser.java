import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Парсер.
 */
public class ExpressionParser {

    // Map для хранения приоритетов операторов
    private static final Map<String, Integer> PRECEDENCE = new HashMap<>();
    static {
        PRECEDENCE.put("-", 1);
        PRECEDENCE.put("+", 1);
        PRECEDENCE.put("/", 2);
        PRECEDENCE.put("*", 2);
        PRECEDENCE.put("~", 3);
    }

    /**
     * Главный метод, который парсит строку.
     *
     * @param expressionString Входное выражение.
     * @return Корень дерева Expression
     */
    public static Expression parse(String expressionString) {
        List<String> tokens = tokenize(expressionString);
        Queue<String> postfixQueue = shuntingYard(tokens);
        return buildExpressionTree(postfixQueue);
    }

    /**
     * Разбиение строки на числа, переменные, операторы, скобки.
     */
    private static List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        // Регулярное выражение для поиска чисел, переменных, операторов и скобок
        Pattern pattern = Pattern.compile("\\d*\\.?\\d+|[a-zA-Z]+|[\\+\\-\\*/\\(\\)]");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            tokens.add(matcher.group());
        }

        // Обработка унарного минуса
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (token.equals("-")) {
                if (i == 0 || "(".equals(tokens.get(i - 1)) || isOperator(tokens.get(i - 1))) {
                    tokens.set(i, "~");
                }
            }
        }
        return tokens;
    }

    /**
     * Преобразования в постфиксную нотацию.
     */
    private static Queue<String> shuntingYard(List<String> tokens) {
        Queue<String> outputQueue = new LinkedList<>();
        Stack<String> operatorStack = new Stack<>();

        for (String token : tokens) {
            if (isNumber(token) || isVariable(token)) {
                outputQueue.add(token);
            } else if (isOperator(token)) {
                while (!operatorStack.isEmpty() && isOperator(operatorStack.peek()) &&
                        PRECEDENCE.get(operatorStack.peek()) >= PRECEDENCE.get(token)) {
                    outputQueue.add(operatorStack.pop());
                }
                operatorStack.push(token);
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    outputQueue.add(operatorStack.pop());
                }
                if (!operatorStack.isEmpty() && operatorStack.peek().equals("(")) {
                    operatorStack.pop();
                } else {
                    throw new IllegalArgumentException("Ошибка: несогласованные скобки.");
                }
            }
        }

        while (!operatorStack.isEmpty()) {
            String op = operatorStack.pop();
            if (op.equals("(") || op.equals(")")) {
                throw new IllegalArgumentException("Ошибка: несогласованные скобки.");
            }
            outputQueue.add(op);
        }

        return outputQueue;
    }

    /**
     * Построение дерева выражений из постфиксной записи.
     */
    private static Expression buildExpressionTree(Queue<String> postfixQueue) {
        Stack<Expression> stack = new Stack<>();

        while (!postfixQueue.isEmpty()) {
            String token = postfixQueue.poll();

            if (isNumber(token)) {
                stack.push(new Number(Double.parseDouble(token)));
            } else if (isVariable(token)) {
                stack.push(new Variable(token));
            } else if (isOperator(token)) {
                // Унарный оператор
                if (token.equals("~")) {
                    if (stack.isEmpty()) throw new IllegalArgumentException("Неверное выражение.");
                    Expression operand = stack.pop();
                    stack.push(new Sub(new Number(0), operand)); // Представляем ~x как (0 - x)
                    continue;
                }

                // Бинарные операторы
                if (stack.size() < 2) throw new IllegalArgumentException("Неверное выражение.");
                Expression right = stack.pop();
                Expression left = stack.pop();

                switch (token) {
                    case "+": stack.push(new Add(left, right)); break;
                    case "-": stack.push(new Sub(left, right)); break;
                    case "*": stack.push(new Mul(left, right)); break;
                    case "/": stack.push(new Div(left, right)); break;
                    default: throw new IllegalArgumentException("Неизвестный оператор: " + token);
                }
            }
        }
        if (stack.size() != 1) throw new IllegalArgumentException("Неверное выражение.");

        return stack.pop();
    }

    private static boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isVariable(String str) {
        return str.matches("[a-zA-Z]+");
    }

    private static boolean isOperator(String str) {
        return PRECEDENCE.containsKey(str);
    }
}