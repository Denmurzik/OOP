import java.util.HashMap;
import java.util.Map;

/**
 * Класс для парсинга строки с переменными.
 */
public final class VariableParser {

    private VariableParser() {}

    /**
     * Парсит строку с переменными.
     *
     * @param assignments строка вида "x=10; y=13"
     * @return Map со значениями переменных
     */
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
                throw new IllegalArgumentException(
                        "Неверный формат пары ключ-значение: " + pair);
            }

            String varName = keyValue[0].trim();
            String valueStr = keyValue[1].trim();

            if (varName.isEmpty() || valueStr.isEmpty()) {
                throw new IllegalArgumentException(
                        "Пустое имя переменной или значение в паре: " + pair);
            }

            try {
                double value = Double.parseDouble(valueStr);
                variables.put(varName, value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Значение переменной не является числом: " + valueStr);
            }
        }
        return variables;
    }
}