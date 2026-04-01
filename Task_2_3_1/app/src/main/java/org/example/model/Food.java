package org.example.model;

/**
 * Класс еды.
 * Разные типы еды могут давать разный рост змейки.
 */
public abstract class Food {
    private final Point position;

    public Food(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }

    /**
     * Количество сегментов, на которое вырастет змейка при поедании.
     */
    public abstract int getGrowthValue();
}
