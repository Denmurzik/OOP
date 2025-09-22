package model;

import model.enums.Suit;
import model.enums.Rank;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Представляет игральную колоду, которая может состоять
 * из одного или нескольких наборов по 52 карты.
 */
public class Deck {
    protected List<Card> cards;

    /**
     * Создает колоду, состоящую из указанного количества 52-карточных наборов.
     *
     * @param numberOfDecks количество 52-карточных наборов для включения в колоду
     */
    public Deck(int numberOfDecks) {

        this.cards = new ArrayList<Card>();

        for (int i = 0; i < numberOfDecks; i++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    this.cards.add(new Card(rank, suit));
                }
            }
        }
    }

    /**
     * Перемешивает все карты в колоде в случайном порядке.
     */
    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    /**
     * Выдает (удаляет и возвращает) одну карту с верха колоды.
     *
     * @return верхняя карта в колоде.
     * @throws IllegalStateException если в колоде не осталось карт.
     */
    public Card dealCard() {
        return cards.remove(0);
    }

    /**
     * Возвращает количество оставшихся карт в колоде.
     *
     * @return количество карт в колоде.
     */
    public int getCardCount() {
        return this.cards.size();
    }
}