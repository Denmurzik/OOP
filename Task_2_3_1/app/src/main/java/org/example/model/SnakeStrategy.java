package org.example.model;

/**
 * Стратегия поведения змейки-робота.
 */
public interface SnakeStrategy {

    /**
     * Возвращает следующее направление движения для данной змейки.
     */
    Direction nextMove(Snake self, GameSnapshot world);

    /**
     * Имя стратегии.
     */
    String name();
}
