import model.Card;
import model.Deck;

import java.util.List;

/**
 * Тестовый класс-заглушка для колоды карт.
 * Он не тасуется и раздает карты в заранее определенном порядке.
 * для предсказуемых сценариев
 */
public class PredictableDeck extends Deck {

    /**
     * Создает "предсказуемую" колоду с конкретным набором карт.
     *
     * @param specificCards Список карт в том порядке, в котором они должны раздаваться.
     */
    public PredictableDeck(List<Card> specificCards) {
        super(0);

        this.cards.addAll(specificCards);
    }

    /**
     * метод не тасует карты, чтобы сохранить порядок.
     */
    @Override
    public void shuffle() {
    }
}