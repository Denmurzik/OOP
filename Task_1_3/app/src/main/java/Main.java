import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите математическое выражение (например, 10 * (x + 3) - y / 2):");
        String exprStr = scanner.nextLine();

        System.out.println("Введите значения переменных (например, x=2; y=4):");
        String assignmentsStr = scanner.nextLine();

        try {
            Expression expression = ExpressionParser.parse(exprStr);
            System.out.println("Разобранное выражение: " + expression.toString());

            Map<String, Double> variables = parseVariables(assignmentsStr);

            double result = expression.eval(variables);
            System.out.println("Результат вычисления: " + result);

            System.out.println("Введите переменную для дифференцирования (например, x):");
            String var = scanner.nextLine();
            Expression derivative = expression.derivative(var);
            System.out.println("Производная по '" + var + "': " + derivative.toString());
            System.out.println("Упрощенная производная: " + derivative.simplify().toString());

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static Map<String, Double> parseVariables(String assignments) {
        Map<String, Double> variables = new HashMap<>();
        if (assignments == null || assignments.trim().isEmpty()) {
            return variables;
        }
        String[] pairs = assignments.trim().split("\\s*;\\s*");

        for (String pair : pairs) {
            if (pair.trim().isEmpty()) {
                continue;
            }

            String[] keyValue = pair.split("\\s*=\\s*");
            if (keyValue.length != 2) {
                throw new IllegalArgumentException("Неверный формат пары ключ-значение: " + pair);
            }

            String varName = keyValue[0].trim();
            String valueStr = keyValue[1].trim();

            if (varName.isEmpty() || valueStr.isEmpty()) {
                throw new IllegalArgumentException("Пустое имя переменной или значение в паре: " + pair);
            }

            try {
                double value = Double.parseDouble(valueStr);
                variables.put(varName, value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Значение переменной не является числом: " + valueStr);
            }
        }
        return variables;
    }
}
