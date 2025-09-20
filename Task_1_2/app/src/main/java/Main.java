import java.util.Scanner;

/**
 * Главный класс приложения, отвечающий за запуск игры Блэкджек.
 * Собирает все компоненты игры и запускает игровой процесс.
 */
public class Main {
    /**
     * Точка входа в программу.
     */
    public static void main(String[] args) {

        Player player = new Player();
        Dealer dealer = new Dealer();

        Scanner scanner = new Scanner(System.in);

        ConsoleView view = new ConsoleView();
        GamePrompter prompter = new GamePrompter(scanner);

        GameRoundFactory roundFactory = new GameRoundFactory();

        BlackjackGame game = new BlackjackGame(player, dealer, prompter, view, roundFactory);

        game.startGame();
    }
}