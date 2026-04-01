package org.example.model;

import java.util.Objects;

/**
 * Точка на игровом поле.
 */
public class Point {
    private final int xpos;
    private final int ypos;

    public Point(int xpos, int ypos) {
        this.xpos = xpos;
        this.ypos = ypos;
    }

    public int getX() {
        return xpos;
    }

    public int getY() {
        return ypos;
    }

    /**
     * Возвращает новую точку, смещённую в заданном направлении.
     */
    public Point move(Direction direction) {
        return new Point(xpos + direction.getDx(), ypos + direction.getDy());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return xpos == point.xpos && ypos == point.ypos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xpos, ypos);
    }

    @Override
    public String toString() {
        return "Point{x=" + xpos + ", y=" + ypos + "}";
    }
}
