package org.example.model;

/**
 * Интерфейс условия победы.
 * Позволяет расширять игру разными условиями выигрыша.
 */
public interface WinCondition {

    /**
     * Проверяет, выполнено ли условие победы.
     */
    boolean isMet(Snake snake, int score);
}
