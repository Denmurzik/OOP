package model;

import model.enums.Rank;

import java.util.ArrayList;
import java.util.List;

/**
 * Представляет руку карт, принадлежащую участнику игры.
 * Класс отвечает за хранение карт, подсчет очков и форматирование вывода.
 */
public class Hand {
    /**
     * Список карт в руке.
     */
    private List<Card> cards;

    /**
     * Создает новую, пустую руку.
     */
    public Hand() {
        this.cards = new ArrayList<Card>();
    }

    /**
     * Добавляет карту в руку.
     *
     * @param card карта для добавления
     */
    public void addCard(Card card) {
        this.cards.add(card);
    }

    /**
     * Удаляет все карты из руки.
     */
    public void clear() {
        this.cards.clear();
    }

    /**
     * Подсчитывает и возвращает сумму очков в руке.
     * Корректно обрабатывает Тузы, считая их за 1 или 11 очков.
     *
     * @return итоговая сумма очков
     */
    public int getScore() {
        int score = 0;
        int aceCount = 0;

        for (Card card : cards) {
            score += card.getValue();
            if (card.getRank() == Rank.ACE) {
                aceCount++;
            }
        }

        while (aceCount > 0 && score > 21) {
            score -= 10;
            aceCount--;
        }

        return score;
    }

    /**
     * Возвращает список карт в руке.
     *
     * @return список объектов {@link Card}
     */
    public List<Card> getCards() {
        return this.cards;
    }

    /**
     * Создает детализированное строковое представление руки.
     * В отличие от {@code toString()}, этот метод показывает контекстную
     * стоимость Тузов (1 или 11) в зависимости от текущей суммы очков.
     *
     * @return отформатированная строка с картами и их стоимостью
     */
    public String toDetailedString() {
        int score = 0;
        int aceCount = 0;
        for (Card card : cards) {
            score += card.getValue();
            if (card.getRank() == Rank.ACE) {
                aceCount++;
            }
        }

        int acesThatAreWorthOne = 0;
        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
            acesThatAreWorthOne++;
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);

            if (card.getRank() == Rank.ACE && acesThatAreWorthOne > 0) {
                sb.append(card.getSuit()).append(" ").append(card.getRank()).append(" (1)");
                acesThatAreWorthOne--;
            } else {
                sb.append(card.toString());
            }

            if (i < cards.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append("]");
        return sb.toString();
    }

    /**
     * Возвращает простое строковое представление руки.
     *
     * @return строка со списком карт
     */
    public String toString() {
        return cards.toString();
    }
}