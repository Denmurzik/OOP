
public class GameRound {

    private final Player player;
    private final Dealer dealer;
    private final Deck deck;
    private final GamePrompter prompter;
    private final ConsoleView view;


    public Player getPlayer() { return this.player; }
    public Dealer getDealer() { return this.dealer; }

    /**
     * Конструктор для инициализации компонентов раунда.
     */
    public GameRound(Player player, Dealer dealer, Deck deck, GamePrompter prompter, ConsoleView view) {
        this.player = player;
        this.dealer = dealer;
        this.deck = deck;
        this.prompter = prompter;
        this.view = view;
    }
    /**
     * Начальная раздача карт игроку и дилеру.
     */
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
     * Основной игровой цикл раунда.
     * @return результат раунда.
     */
    public GameResult play() {
        initialDeal();

        view.printCardsDealt(); 
        showHands(true);

        boolean playerHasBlackjack = (player.getScore() == 21);
        boolean dealerHasBlackjack = (dealer.getScore() == 21);

        if (playerHasBlackjack || dealerHasBlackjack) {
            return handleBlackjackResult(playerHasBlackjack, dealerHasBlackjack);
        }

        playerTurn();

        if (!player.isBusted()) {
            dealerTurn();
        }

        return WinnerEvaluator.determineWinner(player, dealer);
    }

    /**
     * Логика хода игрока.
     */
    private void playerTurn() {
        view.printPlayerTurnHeader();
        while (player.getScore() < 21 && prompter.askPlayerAction() == PlayerAction.HIT) {
            Card newCard = deck.dealCard();
            player.addCard(newCard);
            view.printPlayerDrawnCard(newCard); 
            showHands(true);
        }
    }

    /**
     * Логика хода дилера.
     */
    private void dealerTurn() {
        view.printDealerTurn(); 
        Card hiddenCard = dealer.getHand().getCards().get(1);
        view.printDealerRevealsHiddenCard(hiddenCard); 
        showHands(false);

        while (dealer.getScore() < 17) {
            Card newCard = deck.dealCard();
            dealer.addCard(newCard);
            view.printDealerDrawnCard(newCard); 
            showHands(false);
        }
    }

    /**
     * Обрабатывает ситуацию, когда у игрока или дилера блэкджек.
     *
     * @param playerHasBlackjack true, если у игрока блэкджек.
     * @param dealerHasBlackjack true, если у дилера блэкджек.
     * @return результат раунда.
     */
    private GameResult handleBlackjackResult(boolean playerHasBlackjack, boolean dealerHasBlackjack) {
        showHands(false);
        if (playerHasBlackjack && dealerHasBlackjack) {
            view.printBlackjackPush(); 
            return GameResult.PUSH;
        } else if (playerHasBlackjack) {
            view.printPlayerBlackjack(); 
            return GameResult.PLAYER_WINS;
        } else {
            view.printDealerBlackjack(); 
            return GameResult.DEALER_WINS;
        }
    }

    /**
     * Отображает руки игрока и дилера.
     *
     * @param hideDealerCard true, если нужно скрыть одну карту дилера.
     */
    private void showHands(boolean hideDealerCard) {
        view.showPlayerHand(player.getHand().toDetailedString(), player.getScore()); 
        view.showDealerHand(dealer.getHandAsString(hideDealerCard)); 
    }
}