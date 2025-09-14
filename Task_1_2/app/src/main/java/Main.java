/**
 * Главный класс приложения, отвечающий за запуск игры Блэкджек.
 */
public class Main {
    /**
     * Точка входа в программу.
     * Создает экземпляр {@link BlackjackGame} и запускает игровой процесс.
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        BlackjackGame game = new BlackjackGame();
        game.startGame();
    }
}