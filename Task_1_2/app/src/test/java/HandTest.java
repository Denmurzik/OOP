import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import model.Card;
import model.Hand;
import model.enums.Rank;
import model.enums.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class HandTest {

    private Hand hand;
    private final Card aceSpades = new Card(Rank.ACE, Suit.SPADES);
    private final Card kingHearts = new Card(Rank.KING, Suit.HEARTS);
    private final Card queenClubs = new Card(Rank.QUEEN, Suit.CLUBS);
    private final Card tenDiamonds = new Card(Rank.TEN, Suit.DIAMONDS);
    private final Card nineSpades = new Card(Rank.NINE, Suit.SPADES);
    private final Card fiveHearts = new Card(Rank.FIVE, Suit.HEARTS);

    @BeforeEach
    void setUp() {
        hand = new Hand();
    }

    @Test
    void newHandShouldBeEmptyAndHaveScoreOfZero() {
        assertEquals(0, hand.getCards().size(), "Новая рука должна быть пустой.");
        assertEquals(0, hand.getScore(), "Счет новой руки должен быть равен 0.");
    }

    @Test
    void addCardShouldIncreaseCardCount() {
        hand.addCard(fiveHearts);
        assertEquals(1, hand.getCards().size());
        hand.addCard(nineSpades);
        assertEquals(2, hand.getCards().size());
    }

    @Test
    void clearShouldRemoveAllCardsAndResetScore() {
        hand.addCard(kingHearts);
        hand.addCard(queenClubs);
        assertNotEquals(0, hand.getCards().size());

        hand.clear();

        assertEquals(0, hand.getCards().size(), "Рука должна быть пустой после очистки.");
        assertEquals(0, hand.getScore(), "Счет должен быть 0 после очистки.");
    }


    @Test
    void getScoreWithNumberAndFaceCards_shouldBeCorrectSum() {
        hand.addCard(nineSpades); // 9
        hand.addCard(kingHearts); // 10
        assertEquals(19, hand.getScore());
    }

    @Test
    void getScoreWithBlackjack_shouldBe21() {
        hand.addCard(aceSpades);  // 11
        hand.addCard(tenDiamonds); // 10
        assertEquals(21, hand.getScore(), "Туз и десятка должны давать 21 (Блэкджек).");
    }

    @Test
    void getScoreWithSingleAceAs11_shouldNotBust() {
        hand.addCard(aceSpades); // 11
        hand.addCard(nineSpades); // 9
        assertEquals(20, hand.getScore(), "Туз должен считаться как 11, если нет перебора.");
    }

    @Test
    void getScoreWithSingleAceAs1_shouldPreventBust() {
        hand.addCard(kingHearts); // 10
        hand.addCard(fiveHearts); // 5
        hand.addCard(aceSpades);  //  1
        assertEquals(16, hand.getScore(), "Туз должен считаться как 1, чтобы избежать перебора.");
    }

    @Test
    void getScoreWithTwoAces_shouldBe12() {
        hand.addCard(aceSpades);  // Считается как 11
        hand.addCard(new Card(Rank.ACE, Suit.CLUBS)); // Считается как 1
        assertEquals(12, hand.getScore(), "Два туза должны давать 12 (11 + 1).");
    }

    @Test
    void getScoreWithMultipleAcesAndCards_shouldBeCalculatedCorrectly() {
        hand.addCard(aceSpades); // 11
        hand.addCard(new Card(Rank.ACE, Suit.CLUBS)); // 1
        hand.addCard(nineSpades); // 9
        // 11 + 1 + 9 = 21
        assertEquals(21, hand.getScore(), "Два туза и девятка должны давать 21.");
    }

    @Test
    void getScoreWithBustedHandWithAces_shouldCountAllAcesAs1() {
        hand.addCard(nineSpades); // 9
        hand.addCard(fiveHearts); // 5
        hand.addCard(aceSpades);  // 1
        hand.addCard(new Card(Rank.ACE, Suit.HEARTS)); // 1
        // 9 + 5 + 1 + 1 = 16
        assertEquals(16, hand.getScore());
    }
}