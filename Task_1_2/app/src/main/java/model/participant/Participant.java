package model.participant;

import model.Card;
import model.Hand;

/**
 * Представляет общего участника игры в Блэкджек, у которого есть рука карт.
 * Этот класс служит базовым для {@link Player} и {@link Dealer}.
 */
public class Participant {
    /**
     * Рука карт, принадлежащая участнику.
     */
    Hand hand;

    /**
     * Создает нового участника с пустой рукой карт.
     */
    public Participant() {
        this.hand = new Hand();
    }

    /**
     * Добавляет карту в руку участника.
     *
     * @param card карта для добавления
     */
    public void addCard(Card card) {
        this.hand.addCard(card);
    }

    /**
     * Возвращает текущий счет очков в руке.
     *
     * @return сумма очков
     */
    public int getScore() {
        return this.hand.getScore();
    }

    /**
     * Возвращает объект руки участника.
     *
     * @return объект {@link Hand}
     */
    public Hand getHand() {
        return this.hand;
    }

    /**
     * Очищает руку участника, удаляя все карты.
     */
    public void clearHand() {
        this.hand.clear();
    }

    /**
     * Проверяет, есть ли у участника "перебор" (счет больше 21).
     *
     * @return {@code true}, если счет больше 21, иначе {@code false}
     */
    public boolean isBusted() {
        return this.hand.getScore() > 21;
    }
}