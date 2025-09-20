import java.util.Scanner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class GamePrompterTest {

    @Test
    void askToPlayAgain_whenInputIs1_returnsTrue() {

        String simulatedInput = "1";
        Scanner testScanner = new Scanner(simulatedInput);

        GamePrompter prompter = new GamePrompter(testScanner, new SilentView());

        boolean result = prompter.askToPlayAgain();

        assertTrue(result, "Должно вернуться true, если введено '1'");
    }

    @Test
    void askToPlayAgain_whenInputIsNot1_returnsFalse() {

        String simulatedInput = "любой другой текст";
        Scanner testScanner = new Scanner(simulatedInput);
        GamePrompter prompter = new GamePrompter(testScanner, new SilentView());

        boolean result = prompter.askToPlayAgain();

        assertFalse(result, "Должно вернуться false, если введено не '1'");
    }

    @Test
    void askPlayerAction_whenInputIs1_returnsHit() {

        String simulatedInput = "1";
        Scanner testScanner = new Scanner(simulatedInput);
        GamePrompter prompter = new GamePrompter(testScanner, new SilentView());

        PlayerAction result = prompter.askPlayerAction();

        assertEquals(PlayerAction.HIT, result);
    }

    @Test
    void askForNumberOfDecks_whenInputIsValid_returnsNumber() {
        String simulatedInput = "4";
        Scanner testScanner = new Scanner(simulatedInput);
        GamePrompter prompter = new GamePrompter(testScanner, new SilentView());

        int result = prompter.askForNumberOfDecks();

        assertEquals(4, result);
    }

    @Test
    void askForNumberOfDecks_whenInputIsInvalidThenValid_returnsValidNumber() {
        String simulatedInput = "abc\n-5\n0\n2\n"; //
        Scanner testScanner = new Scanner(simulatedInput);
        GamePrompter prompter = new GamePrompter(testScanner, new SilentView());

        int result = prompter.askForNumberOfDecks();

        assertEquals(2, result,
                "Метод должен продолжать спрашивать до получения корректного ввода.");
    }
}