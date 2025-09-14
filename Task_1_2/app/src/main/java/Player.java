import java.util.Scanner;

/**
 * Представляет игрока-пользователя в игре Блэкджек.
 * Логика ходов этого участника определяется вводом с консоли.
 */
public class Player extends Participant {

    /**
     * Создает нового игрока с именем по умолчанию.
     */
    public Player() {
        super();
    }

    /**
     * Запрашивает у пользователя, хочет ли он взять еще одну карту ("hit").
     *
     * @param scanner объект Scanner для чтения ввода из консоли.
     * @return {@code true}, если игрок хочет взять карту (ввел '1'), иначе {@code false}
     */
    public boolean wantsToHit(Scanner scanner) {
        System.out.println("\nВаш ход");
        System.out.println("--------------------");
        System.out.print("Введите \"1\", чтобы взять карту, и \"0\", чтобы остановиться: ");
        String input = scanner.nextLine();
        return "1".equals(input);
    }
}