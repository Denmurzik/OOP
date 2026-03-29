package org.example.model;

/**
 * Конфигурация игры.
 *
 * @param width     ширина поля в клетках (N)
 * @param height    высота поля в клетках (M)
 * @param foodCount количество еды на поле одновременно (T)
 * @param winLength длина змейки для победы (L)
 * @param tickMs    интервал между тиками в миллисекундах (скорость)
 */
public record GameConfig(int width, int height, int foodCount, int winLength, long tickMs) {

    /**
     * Конфигурация по умолчанию.
     */
    public static GameConfig defaultConfig() {
        return new GameConfig(20, 15, 3, 10, 200);
    }
}
