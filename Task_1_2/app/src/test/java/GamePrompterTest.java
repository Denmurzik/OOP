import org.junit.jupiter.api.Test;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class GamePrompterTest {

    @Test
    void askToPlayAgain_whenInputIs1_returnsTrue() {
        // Arrange (Подготовка)
        String simulatedInput = "1"; // Имитируем ввод пользователя
        Scanner testScanner = new Scanner(simulatedInput);
        // Используем SilentView, чтобы в консоли не было лишнего текста
        GamePrompter prompter = new GamePrompter(testScanner, new SilentView());

        // Act (Действие)
        boolean result = prompter.askToPlayAgain();

        // Assert (Проверка)
        assertTrue(result, "Должно вернуться true, если введено '1'");
    }

    @Test
    void askToPlayAgain_whenInputIsNot1_returnsFalse() {
        // Arrange
        String simulatedInput = "любой другой текст";
        Scanner testScanner = new Scanner(simulatedInput);
        GamePrompter prompter = new GamePrompter(testScanner, new SilentView());

        // Act
        boolean result = prompter.askToPlayAgain();

        // Assert
        assertFalse(result, "Должно вернуться false, если введено не '1'");
    }

    @Test
    void askPlayerAction_whenInputIs1_returnsHit() {
        // Arrange
        String simulatedInput = "1";
        Scanner testScanner = new Scanner(simulatedInput);
        GamePrompter prompter = new GamePrompter(testScanner, new SilentView());

        // Act
        PlayerAction result = prompter.askPlayerAction();

        // Assert
        assertEquals(PlayerAction.HIT, result);
    }

    @Test
    void askForNumberOfDecks_whenInputIsValid_returnsNumber() {
        // Arrange
        String simulatedInput = "4"; // Корректный ввод
        Scanner testScanner = new Scanner(simulatedInput);
        GamePrompter prompter = new GamePrompter(testScanner, new SilentView());

        // Act
        int result = prompter.askForNumberOfDecks();

        // Assert
        assertEquals(4, result);
    }

    @Test
    void askForNumberOfDecks_whenInputIsInvalidThenValid_returnsValidNumber() {
        // Arrange
        // Имитируем несколько неверных вводов, а затем один верный
        String simulatedInput = "abc\n-5\n0\n2\n"; // \n - это перенос строки
        Scanner testScanner = new Scanner(simulatedInput);
        GamePrompter prompter = new GamePrompter(testScanner, new SilentView());

        // Act
        // Метод должен проигнорировать 'abc', -5, 0 и вернуть 2
        int result = prompter.askForNumberOfDecks();

        // Assert
        assertEquals(2, result, "Метод должен продолжать спрашивать до получения корректного ввода.");
    }
}