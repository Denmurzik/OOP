package org.example.model;

/**
 * Направление движения змейки.
 */
public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    /**
     * Проверяет является ли данное направление противоположным текущему.
     */
    public boolean isOpposite(Direction other) {
        return this.dx + other.dx == 0 && this.dy + other.dy == 0;
    }
}
