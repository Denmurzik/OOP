package ui;

import java.util.Scanner;
import model.enums.PlayerAction;

/**
 * Отвечает за взаимодействие с пользователем:
 * выводит запросы и считывает введенные данные.
 */
public class GamePrompter {
    private final Scanner scanner;
    private final ConsoleView view;

    /**
     * Создает новый prompter с указанным сканером.
     *
     * @param scanner Сканер для чтения ввода.
     */
    public GamePrompter(Scanner scanner, ConsoleView view) {
        this.scanner = scanner;
        this.view = view;
    }

    /**
     * Спрашивает у пользователя количество колод
     * и возвращает корректное значение.
     * Метод будет повторять запрос до тех пор,
     * пока не будет введено положительное число.
     *
     * @return Количество колод, выбранное пользователем.
     */
    public int askForNumberOfDecks() {
        view.promptNumberOfDecks();

        while (true) {
            try {
                int decks = Integer.parseInt(scanner.nextLine());
                if (decks > 0) {
                    return decks;
                }
            } catch (NumberFormatException e) {
                // игнор
            }
            view.printInvalidNumberOfDecks();
        }
    }

    /**
     * Спрашивает у пользователя, хочет ли он сыграть еще один раунд.
     *
     * @return true, если пользователь ввел "1", иначе false.
     */
    public boolean askToPlayAgain() {
        view.promptPlayAgain();
        String choice = scanner.nextLine();
        return "1".equals(choice);
    }

    /**
     * Спрашивает у игрока его ход.
     *
     * @return model.enums.PlayerAction.HIT.
     */
    public PlayerAction askPlayerAction() {
        view.promptPlayerAction();
        String choice = scanner.nextLine();
        if ("1".equals(choice)) {
            return PlayerAction.HIT;
        }
        return PlayerAction.STAND;
    }
}