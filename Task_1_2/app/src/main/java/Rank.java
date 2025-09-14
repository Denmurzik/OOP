/**
 * Перечисление, представляющее ранги (достоинства) игральных карт и их стоимость в очках.
 */
public enum Rank {
    TWO("Двойка", 2),
    THREE("Тройка", 3),
    FOUR("Четверка", 4),
    FIVE("Пятерка", 5),
    SIX("Шестерка", 6),
    SEVEN("Семерка", 7),
    EIGHT("Восьмерка", 8),
    NINE("Девятка", 9),
    TEN("Десятка", 10),
    JACK("Валет", 10),
    QUEEN("Дама", 10),
    KING("Король", 10),
    ACE("Туз", 11);

    private String name;
    private int value;

    /**
     * Создает ранг карты с названием и стоимостью в очках.
     * @param name русскоязычное название ранга
     * @param value стоимость ранга в очках (для Туза по умолчанию 11)
     */
    Rank(String name, int value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Возвращает стоимость ранга в очках.
     * @return количество очков.
     */
    public int getValue() {
        return value;
    }

    /**
     * Возвращает русскоязычное название ранга.
     * @return название ранга.
     */
    public String toString() {
        return name;
    }

}