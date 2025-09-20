import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameRoundTest {

    private Player player;
    private Dealer dealer;
    private ConsoleView silentView;

    @BeforeEach
    void setUp() {
        player = new Player();
        dealer = new Dealer();
        silentView = new SilentView();
    }

    @Test
    void initialDealShouldGiveTwoCardsToPlayerAndDealer() {
        Deck deck = new Deck(1);
        GamePrompter prompter = new TestPrompter(false); // Не имеет значения для этого теста
        GameRound gameRound = new GameRound(player, dealer, deck, prompter, silentView);

        gameRound.initialDeal();

        assertEquals(2, player.getHand().getCards().size(),
                "У игрока должно быть 2 карты.");
        assertEquals(2, dealer.getHand().getCards().size(),
                "У дилера должно быть 2 карты.");
    }

    @Test
    void playWhenPlayerGetsBlackjack_returnsPlayerWins() {
        Deck predictableDeck = new PredictableDeck(Arrays.asList(
                new Card(Rank.ACE, Suit.SPADES),   // Карта игрока 1
                new Card(Rank.FIVE, Suit.HEARTS),  // Карта дилера 1
                new Card(Rank.KING, Suit.SPADES),  // Карта игрока 2 -> Блэкджек
                new Card(Rank.SIX, Suit.HEARTS)    // Карта дилера 2
        ));
        GamePrompter prompter = new TestPrompter(false);
        GameRound gameRound = new GameRound(player, dealer, predictableDeck, prompter, silentView);

        GameResult result = gameRound.play();

        assertEquals(GameResult.PLAYER_WINS, result);
        assertEquals(21, player.getScore());
    }

    @Test
    void playWhenPlayerStandsAndWins_returnsPlayerWins() {
        Deck predictableDeck = new PredictableDeck(Arrays.asList(
                new Card(Rank.TEN, Suit.SPADES),   // Игрок: 10
                new Card(Rank.EIGHT, Suit.HEARTS), // Дилер: 8
                new Card(Rank.KING, Suit.SPADES),  // Игрок: 10 + 10 = 20
                new Card(Rank.TEN, Suit.HEARTS)    // Дилер: 8 + 10 = 18
        ));
        GamePrompter prompter = new TestPrompter(false, PlayerAction.STAND);
        GameRound gameRound = new GameRound(player, dealer, predictableDeck, prompter, silentView);

        GameResult result = gameRound.play();

        assertEquals(GameResult.PLAYER_WINS, result);
        assertFalse(player.isBusted());
        assertFalse(dealer.isBusted());
    }

    @Test
    void playWhenPlayerHitsAndBusts_returnsDealerWins() {
        Deck predictableDeck = new PredictableDeck(Arrays.asList(
                new Card(Rank.TEN, Suit.SPADES),   // Игрок: 10
                new Card(Rank.TEN, Suit.HEARTS),   // Дилер: 10
                new Card(Rank.FIVE, Suit.SPADES),  // Игрок: 10 + 5 = 15
                new Card(Rank.SEVEN, Suit.HEARTS), // Дилер: 10 + 7 = 17
                new Card(Rank.QUEEN, Suit.CLUBS)   // Карта для добора игроком -> 15 + 10 = 25 (перебор)
        ));
        GamePrompter prompter = new TestPrompter(false, PlayerAction.HIT);
        GameRound gameRound = new GameRound(player, dealer, predictableDeck, prompter, silentView);

        GameResult result = gameRound.play();

        assertEquals(GameResult.DEALER_WINS, result);
        assertTrue(player.isBusted(), "У игрока должен быть перебор.");
    }

    @Test
    void playWhenDealerMustHitAndWins_returnsDealerWins() {
        Deck predictableDeck = new PredictableDeck(Arrays.asList(
                new Card(Rank.TEN, Suit.SPADES),    // Игрок: 10
                new Card(Rank.TEN, Suit.HEARTS),    // Дилер: 10
                new Card(Rank.EIGHT, Suit.SPADES),  // Игрок: 10 + 8 = 18
                new Card(Rank.SIX, Suit.HEARTS),    // Дилер: 10 + 6 = 16 (обязан брать)
                new Card(Rank.FIVE, Suit.CLUBS)     // Карта для добора дилером -> 16 + 5 = 21
        ));
        GamePrompter prompter = new TestPrompter(false, PlayerAction.STAND);
        GameRound gameRound = new GameRound(player, dealer, predictableDeck, prompter, silentView);

        GameResult result = gameRound.play();

        assertEquals(GameResult.DEALER_WINS, result);
        assertEquals(21, dealer.getScore());
    }
}