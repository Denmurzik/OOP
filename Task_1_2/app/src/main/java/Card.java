/**
 * Представляет одну игральную карту, имеющую ранг и масть.
 */
public class Card {
    private Rank rank;
    private Suit suit;

    /**
     * Создает карту с заданным рангом и мастью.
     *
     * @param rank ранг карты (например, {@code Rank.ACE})
     * @param suit масть карты (например, {@code Suit.SPADES})
     */
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Возвращает стоимость карты в очках.
     *
     * @return количество очков
     */
    public int getValue() {
        return rank.getValue();
    }

    /**
     * Возвращает ранг карты.
     *
     * @return объект {@link Rank}
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Возвращает масть карты.
     *
     * @return объект {@link Suit}
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Возвращает полное строковое представление карты.
     *
     * @return строка вида "Туз Пики (11)"
     */
    public String toString() {
        return rank.toString() + " " + suit.toString() + " (" + getValue() + ")";
    }
}