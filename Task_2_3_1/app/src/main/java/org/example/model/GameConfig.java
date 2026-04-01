package org.example.model;

/**
 * Конфигурация игры.
 */
public class GameConfig {
    private final int width;
    private final int height;
    private final int foodCount;
    private final int winLength;
    private final long tickMs;

    public GameConfig(int width, int height, int foodCount, int winLength, long tickMs) {
        this.width = width;
        this.height = height;
        this.foodCount = foodCount;
        this.winLength = winLength;
        this.tickMs = tickMs;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public int getWinLength() {
        return winLength;
    }

    public long getTickMs() {
        return tickMs;
    }

    /**
     * Конфигурация по умолчанию.
     */
    public static GameConfig defaultConfig() {
        return new GameConfig(20, 15, 3, 10, 200);
    }
}
