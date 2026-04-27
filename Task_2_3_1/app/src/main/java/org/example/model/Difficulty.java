package org.example.model;

/**
 * Уровень сложности игры. Определяет скорость змейки.
 */
public enum Difficulty {
    EASY(300),
    NORMAL(180),
    HARD(90);

    private final long tickMs;

    Difficulty(long tickMs) {
        this.tickMs = tickMs;
    }

    public long getTickMs() {
        return tickMs;
    }
}
