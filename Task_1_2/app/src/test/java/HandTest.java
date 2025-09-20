import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {

    private Hand hand;
    private final Card ACE_SPADES = new Card(Rank.ACE, Suit.SPADES);
    private final Card KING_HEARTS = new Card(Rank.KING, Suit.HEARTS);
    private final Card QUEEN_CLUBS = new Card(Rank.QUEEN, Suit.CLUBS);
    private final Card TEN_DIAMONDS = new Card(Rank.TEN, Suit.DIAMONDS);
    private final Card NINE_SPADES = new Card(Rank.NINE, Suit.SPADES);
    private final Card FIVE_HEARTS = new Card(Rank.FIVE, Suit.HEARTS);

    @BeforeEach
    void setUp() {
        hand = new Hand();
    }

    @Test
    void newHand_shouldBeEmptyAndHaveScoreOfZero() {
        assertEquals(0, hand.getCards().size(), "Новая рука должна быть пустой.");
        assertEquals(0, hand.getScore(), "Счет новой руки должен быть равен 0.");
    }

    @Test
    void addCard_shouldIncreaseCardCount() {
        hand.addCard(FIVE_HEARTS);
        assertEquals(1, hand.getCards().size());
        hand.addCard(NINE_SPADES);
        assertEquals(2, hand.getCards().size());
    }

    @Test
    void clear_shouldRemoveAllCardsAndResetScore() {
        hand.addCard(KING_HEARTS);
        hand.addCard(QUEEN_CLUBS);
        assertNotEquals(0, hand.getCards().size());

        hand.clear();

        assertEquals(0, hand.getCards().size(), "Рука должна быть пустой после очистки.");
        assertEquals(0, hand.getScore(), "Счет должен быть 0 после очистки.");
    }


    @Test
    void getScore_withNumberAndFaceCards_shouldBeCorrectSum() {
        hand.addCard(NINE_SPADES); // 9
        hand.addCard(KING_HEARTS); // 10
        assertEquals(19, hand.getScore());
    }

    @Test
    void getScore_withBlackjack_shouldBe21() {
        hand.addCard(ACE_SPADES);  // 11
        hand.addCard(TEN_DIAMONDS); // 10
        assertEquals(21, hand.getScore(), "Туз и десятка должны давать 21 (Блэкджек).");
    }

    @Test
    void getScore_withSingleAceAs11_shouldNotBust() {
        hand.addCard(ACE_SPADES); // 11
        hand.addCard(NINE_SPADES); // 9
        assertEquals(20, hand.getScore(), "Туз должен считаться как 11, если нет перебора.");
    }

    @Test
    void getScore_withSingleAceAs1_shouldPreventBust() {
        hand.addCard(KING_HEARTS); // 10
        hand.addCard(FIVE_HEARTS); // 5
        hand.addCard(ACE_SPADES);  //  1
        assertEquals(16, hand.getScore(), "Туз должен считаться как 1, чтобы избежать перебора.");
    }

    @Test
    void getScore_withTwoAces_shouldBe12() {
        hand.addCard(ACE_SPADES);  // Считается как 11
        hand.addCard(new Card(Rank.ACE, Suit.CLUBS)); // Считается как 1
        assertEquals(12, hand.getScore(), "Два туза должны давать 12 (11 + 1).");
    }

    @Test
    void getScore_withMultipleAcesAndCards_shouldBeCalculatedCorrectly() {
        hand.addCard(ACE_SPADES); // 11
        hand.addCard(new Card(Rank.ACE, Suit.CLUBS)); // 1
        hand.addCard(NINE_SPADES); // 9
        // 11 + 1 + 9 = 21
        assertEquals(21, hand.getScore(), "Два туза и девятка должны давать 21.");
    }

    @Test
    void getScore_withBustedHandWithAces_shouldCountAllAcesAs1() {
        hand.addCard(NINE_SPADES); // 9
        hand.addCard(FIVE_HEARTS); // 5
        hand.addCard(ACE_SPADES);  // 1
        hand.addCard(new Card(Rank.ACE, Suit.HEARTS)); // 1
        // 9 + 5 + 1 + 1 = 16
        assertEquals(16, hand.getScore());
    }
}