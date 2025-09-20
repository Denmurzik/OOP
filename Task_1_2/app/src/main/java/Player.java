import java.util.Scanner;

/**
 * Представляет игрока-пользователя в игре Блэкджек.
 * Логика ходов этого участника определяется вводом с консоли.
 */
public class Player extends Participant {

    /**
     * Создает нового игрока.
     */
    public Player() {
        super();
    }

    /**
     * Запрашивает у пользователя, хочет ли он взять еще одну карту ("hit").
     *
     * @param input ввод пользователя с консоли
     * @return {@code true}, если игрок хочет взять карту (ввел '1'), иначе {@code false}
     */
    public int wantsToHit(String input) {


        if ("1".equals(input)) {
            return 1;
        }
        if ("0".equals(input)) {
            return 0;
        }
        ConsoleUtils.printInvalidInput();
        return -1;
    }
}