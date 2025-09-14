/**
 * Перечисление, представляющее масти игральных карт.
 */
public enum Suit {
    HEARTS("Черви"),
    DIAMONDS("Бубны"),
    CLUBS("Трефы"),
    SPADES("Пики");

    private String name;

    /**
     * Создает масть.
     *
     * @param name название масти на русском языке.
     */
    private Suit(String name) {
        this.name = name;
    }

    /**
     * Возвращает русскоязычное название масти.
     *
     * @return название масти.
     */
    public String toString() {
        return name;
    }
}