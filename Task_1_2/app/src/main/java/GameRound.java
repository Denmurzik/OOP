
public class GameRound {

    private final Player player;
    private final Dealer dealer;
    private final Deck deck;
    private final GamePrompter prompter; // <-- Вместо Scanner
    private final ConsoleView view;     // <-- Наш объект для вывода


    public Player getPlayer() { return this.player; }
    public Dealer getDealer() { return this.dealer; }


    public GameRound(Player player, Dealer dealer, Deck deck, GamePrompter prompter, ConsoleView view) {
        this.player = player;
        this.dealer = dealer;
        this.deck = deck;
        this.prompter = prompter; // <-- Сохраняем prompter
        this.view = view;         // <-- Сохраняем view
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

    private void playerTurn() {
        view.printPlayerTurnHeader();
        while (player.getScore() < 21 && prompter.askPlayerAction() == PlayerAction.HIT) {
            Card newCard = deck.dealCard();
            player.addCard(newCard);
            view.printPlayerDrawnCard(newCard); 
            showHands(true);
        }
    }

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

    private void showHands(boolean hideDealerCard) {
        view.showPlayerHand(player.getHand().toDetailedString(), player.getScore()); 
        view.showDealerHand(dealer.getHandAsString(hideDealerCard)); 
    }
}