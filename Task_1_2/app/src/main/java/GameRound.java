import java.util.Scanner;

public class GameRound {
    private Player player;
    private Dealer dealer;
    private Deck deck;
    private Scanner scanner;

    public Player getPlayer() {
        return this.player;
    }
    public Dealer getDealer() {
        return this.dealer;
    }

    public GameRound(Player player, Dealer dealer, Deck deck, Scanner scanner) {
        this.player = player;
        this.dealer = dealer;
        this.deck = deck;
        this.scanner = scanner;
    }

    public void initialDeal() {
        deck.shuffle();
        player.clearHand();
        dealer.clearHand();

        player.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());
        player.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());
    }

    /**
     * Запускает и управляет логикой одного полного раунда игры.
     * @return GameResult - результат раунда (победа игрока, дилера или ничья).
     */
    public GameResult play() {
        initialDeal();

        ConsoleUtils.printCardsDealt();
        showHands(true);

        // Проверка на блэкджек в начале раунда
        boolean playerHasBlackjack = (player.getScore() == 21);
        boolean dealerHasBlackjack = (dealer.getScore() == 21);

        if (playerHasBlackjack || dealerHasBlackjack) {
            return handleBlackjackResult(playerHasBlackjack, dealerHasBlackjack);
        }

        // Ход игрока
        playerTurn();

        // Если у игрока не перебор, ходит дилер
        if (!player.isBusted()) {
            dealerTurn();
        }

        // Определение победителя в конце раунда
        return WinnerEvaluator.determineWinner(player, dealer);
    }

    /**
     * Управляет ходом игрока.
     */
    private void playerTurn() {
        ConsoleUtils.printPlayerTurnHeader();
        ConsoleUtils.promptPlayerAction();

        while (player.getScore() < 21 && player.wantsToHit(scanner.nextLine()) == 1) {
            Card newCard = deck.dealCard();
            player.addCard(newCard);
            ConsoleUtils.printPlayerDrawnCard(newCard);
            showHands(true);
            ConsoleUtils.printPlayerTurnHeader();
            ConsoleUtils.promptPlayerAction();
        }
    }

    /**
     * Управляет ходом дилера.
     */
    private void dealerTurn() {
        ConsoleUtils.printDealerTurn();
        Card hiddenCard = dealer.getHand().getCards().get(1); // Получаем скрытую карту
        ConsoleUtils.printDealerRevealsHiddenCard(hiddenCard);
        showHands(false); // Показываем обе карты дилера

        while (dealer.getScore() < 17) {
            Card newCard = deck.dealCard();
            dealer.addCard(newCard);
            ConsoleUtils.printDealerDrawnCard(newCard);
            showHands(false);
        }
    }

    /**
     * Обрабатывает результат при наличии блэкджека.
     * @return GameResult, соответствующий исходу.
     */
    private GameResult handleBlackjackResult(boolean playerHasBlackjack, boolean dealerHasBlackjack) {
        showHands(false);
        if (playerHasBlackjack && dealerHasBlackjack) {
            ConsoleUtils.printBlackjackPush();
            return GameResult.PUSH;
        } else if (playerHasBlackjack) {
            ConsoleUtils.printPlayerBlackjack();
            return GameResult.PLAYER_WINS;
        } else {
            ConsoleUtils.printDealerBlackjack();
            return GameResult.DEALER_WINS;
        }
    }

    /**
     * Отображает руки игрока и дилера.
     */
    private void showHands(boolean hideDealerCard) {
        ConsoleUtils.showPlayerHand(player.getHand().toDetailedString(), player.getScore());
        ConsoleUtils.showDealerHand(dealer.getHandAsString(hideDealerCard));
    }
}