import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Вывод.
 */
class ConsoleViewTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private ConsoleView view;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        view = new ConsoleView();
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void printWelcomeMessage_shouldPrintCorrectWelcomeMessage() {
        String expectedOutput = "Добро пожаловать в Блэкджек!" + System.lineSeparator();

        view.printWelcomeMessage();

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void printRoundHeader_shouldPrintHeaderWithCorrectRoundNumber() {
        String expectedOutput = "\nРаунд 5" + System.lineSeparator();

        view.printRoundHeader(5);

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void promptPlayAgain_shouldPrintPromptWithoutNewline() {
        String expectedOutput = "Хотите сыграть еще раз? (1 - да, 0 - нет): ";

        view.promptPlayAgain();

        assertEquals(expectedOutput, outContent.toString());
    }


    @Test
    void printOverallScore_whenPlayerLeads_printsPlayerWinningMessage() {

        String expected = "Счёт 3 : 1 в вашу пользу. " + System.lineSeparator();

        view.printOverallScore(3, 1);

        assertEquals(expected, outContent.toString());
    }

    @Test
    void printOverallScore_whenDealerLeads_printsDealerWinningMessage() {

        String expected = "Счёт 1 : 3 в пользу дилера. " + System.lineSeparator();

        view.printOverallScore(1, 3);

        assertEquals(expected, outContent.toString());
    }

    @Test
    void printOverallScore_whenTied_printsPushMessage() {

        String expected = "Счёт 2 : 2 Ничья. " + System.lineSeparator();

        view.printOverallScore(2, 2);

        assertEquals(expected, outContent.toString());
    }
}