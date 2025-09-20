/**
 * Класс, управляющий логикой игры в блэкджек.
 * Отвечает за инициализацию компонентов игры, управление раундами и подсчетом очков.
 */
public class BlackjackGame {
    private final Player player;
    private final Dealer dealer;
    private final GamePrompter prompter;
    private final ConsoleView view;
    private final GameRoundFactory roundFactory;

    private Deck deck;
    private int playerWins;
    private int dealerWins;

    /**
     * Конструктор для инициализации компонентов игры.
     *
     */
    public BlackjackGame(Player player, Dealer dealer, GamePrompter prompter,
                         ConsoleView view, GameRoundFactory roundFactory) {
        this.player = player;
        this.dealer = dealer;
        this.prompter = prompter;
        this.view = view;
        this.playerWins = 0;
        this.dealerWins = 0;
        this.roundFactory = roundFactory;
    }

    /**
     * Запускает игру и управляет циклом раундов.
     */
    public void startGame() {
        view.printWelcomeMessage();

        int numberOfDecks = prompter.askForNumberOfDecks();
        int round = 1;

        do {
            view.printRoundHeader(round);

            this.deck = new Deck(numberOfDecks);

            GameRound gameRound = roundFactory.create(player, dealer, deck, prompter, view);
            GameResult result = gameRound.play();


            updateScoreAndDisplayResult(result);

            round++;
        } while (prompter.askToPlayAgain());

        view.printGoodbyeMessage();
    }

    /**
     * Обновляет счет и отображает результат раунда через ConsoleView.
     *
     * @param result Итог раунда.
     */
    private void updateScoreAndDisplayResult(GameResult result) {
        switch (result) {
            case PLAYER_WINS:
                if (player.getScore() != 21) {
                    if (dealer.isBusted()) {
                        view.printDealerBust();
                    } else {
                        view.printPlayerWinsRound();
                    }
                }
                playerWins++;
                break;
            case DEALER_WINS:
                if (dealer.getScore() != 21) {
                    if (player.isBusted()) {
                        view.printPlayerBust();
                    } else {
                        view.printDealerWinsRound();
                    }
                }
                dealerWins++;
                break;
            default:
                if (player.getScore() != 21) {
                    view.printPush();
                }
                break;
        }
        view.printOverallScore(playerWins, dealerWins);
    }

    /**
     * Возвращает количество побед игрока.
     *
     * @return количество побед игрока.
     */
    public int getPlayerWins() {
        return playerWins;
    }

    /**
     * Возвращает количество побед дилера.
     *
     * @return количество побед дилера.
     */
    public int getDealerWins() {
        return dealerWins;
    }
}