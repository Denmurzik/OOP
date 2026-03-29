package org.example.model;

/**
 * Точка на игровом поле.
 */
public record Point(int x, int y) {

    /**
     * Возвращает новую точку, смещённую в заданном направлении.
     */
    public Point move(Direction direction) {
        return new Point(x + direction.getDx(), y + direction.getDy());
    }
}
