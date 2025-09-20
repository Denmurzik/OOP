import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleViewTest {

    // Это специальный поток, который будет "ловить" вывод в консоль
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    // Здесь мы сохраняем оригинальный системный поток вывода
    private final PrintStream originalOut = System.out;

    private ConsoleView view;

    @BeforeEach
    public void setUp() {
        // Перед КАЖДЫМ тестом перенаправляем System.out на наш "перехватчик"
        System.setOut(new PrintStream(outContent));
        view = new ConsoleView();
    }

    @AfterEach
    public void tearDown() {
        // После КАЖДОГО теста возвращаем System.out в исходное состояние.
        // Это КРАЙНЕ ВАЖНО, чтобы не сломать другие тесты или работу IDE.
        System.setOut(originalOut);
    }

    @Test
    void printWelcomeMessage_shouldPrintCorrectWelcomeMessage() {
        // Arrange (Подготовка)
        String expectedOutput = "Добро пожаловать в Блэкджек!" + System.lineSeparator();

        // Act (Действие)
        view.printWelcomeMessage();

        // Assert (Проверка)
        // Сравниваем то, что мы "поймали", с тем, что ожидали
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void printRoundHeader_shouldPrintHeaderWithCorrectRoundNumber() {
        // Arrange
        String expectedOutput = "\nРаунд 5" + System.lineSeparator();

        // Act
        view.printRoundHeader(5);

        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void promptPlayAgain_shouldPrintPromptWithoutNewline() {
        // Arrange
        // Метод System.out.print не добавляет перенос строки, это важно проверить
        String expectedOutput = "Хотите сыграть еще раз? (1 - да, 0 - нет): ";

        // Act
        view.promptPlayAgain();

        // Assert
        assertEquals(expectedOutput, outContent.toString());
    }

    // --- Тестирование метода с логикой ---

    @Test
    void printOverallScore_whenPlayerLeads_printsPlayerWinningMessage() {
        // Arrange
        String expected = "Счёт 3 : 1 в вашу пользу. " + System.lineSeparator();

        // Act
        view.printOverallScore(3, 1);

        // Assert
        assertEquals(expected, outContent.toString());
    }

    @Test
    void printOverallScore_whenDealerLeads_printsDealerWinningMessage() {
        // Arrange
        String expected = "Счёт 1 : 3 в пользу дилера. " + System.lineSeparator();

        // Act
        view.printOverallScore(1, 3);

        // Assert
        assertEquals(expected, outContent.toString());
    }

    @Test
    void printOverallScore_whenTied_printsPushMessage() {
        // Arrange
        String expected = "Счёт 2 : 2 Ничья. " + System.lineSeparator();

        // Act
        view.printOverallScore(2, 2);

        // Assert
        assertEquals(expected, outContent.toString());
    }
}