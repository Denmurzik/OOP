import java.util.Scanner;

public class BlackjackGame {
    private Player player;
    private Dealer dealer;
    private Deck deck;
    private Scanner scanner;

    private int playerWins;
    private int dealerWins;

    public BlackjackGame() {
        this.player = new Player();
        this.dealer = new Dealer();
        this.scanner = new Scanner(System.in);
        this.playerWins = 0;
        this.dealerWins = 0;
    }

    /**
     * Конструктор для тестирования. Позволяет подменить источник ввода.
     *
     * @param scanner Сканер для считывания ввода.
     */
    public BlackjackGame(Scanner scanner) {
        this.player = new Player();
        this.dealer = new Dealer();
        this.scanner = scanner;
        this.playerWins = 0;
        this.dealerWins = 0;
    }

    /**
     * Запускает игру и управляет циклом раундов до тех пор, пока игрок не решит выйти.
     */
    public void startGame() {
        ConsoleUtils.printWelcomeMessage();

        int numberOfDecks = 0;
        while (numberOfDecks <= 0) {
            try {
                ConsoleUtils.promptNumberOfDecks();
                String input = scanner.nextLine();
                numberOfDecks = Integer.parseInt(input);
                if (numberOfDecks <= 0) {
                    ConsoleUtils.printInvalidNumberOfDecks();
                }
            } catch (NumberFormatException e) {
                ConsoleUtils.printInvalidNumberOfDecks();
            }
        }

        int round = 1;

        while (true) {
            ConsoleUtils.printRoundHeader(round);

            this.deck = new Deck(numberOfDecks);

            GameRound gameRound = new GameRound(player, dealer, deck, scanner);
            GameResult result = gameRound.play();

            updateScoreAndDisplayResult(result);

            ConsoleUtils.printSeparator();
            ConsoleUtils.promptPlayAgain();
            String choice = scanner.nextLine();
            if (!"1".equals(choice)) {
                break;
            }
            round++;
        }

        ConsoleUtils.printGoodbyeMessage();
        scanner.close();
    }

    /**
     * Обновляет общий счет и отображает результат раунда.
     * @param result Итог раунда (PLAYER_WINS, DEALER_WINS, или PUSH).
     */
    private void updateScoreAndDisplayResult(GameResult result) {
        switch (result) {
            case PLAYER_WINS:
                // Сообщение о блэкджеке уже выведено в GameRound,
                // поэтому здесь обрабатываем только обычные победы.
                if (player.getScore() != 21) {
                    if (dealer.isBusted()) {
                        ConsoleUtils.printDealerBust();
                    } else {
                        ConsoleUtils.printPlayerWinsRound();
                    }
                }
                playerWins++;
                break;
            case DEALER_WINS:
                // Аналогично для дилера
                if (dealer.getScore() != 21) {
                    if (player.isBusted()) {
                        ConsoleUtils.printPlayerBust();
                    } else {
                        ConsoleUtils.printDealerWinsRound();
                    }
                }
                dealerWins++;
                break;
            case PUSH:
                if (player.getScore() != 21) {
                    ConsoleUtils.printPush();
                }
                break;
        }
        ConsoleUtils.printOverallScore(playerWins, dealerWins);
    }


    //методы для теста
    public int getPlayerWins() {
        return playerWins;
    }

    public int getDealerWins() {
        return dealerWins;
    }
}