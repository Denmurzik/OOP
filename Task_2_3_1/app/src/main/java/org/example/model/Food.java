package org.example.model;

/**
 * Еда на игровом поле.
 */
public class Food {
    private final Point position;

    public Food(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }
}
