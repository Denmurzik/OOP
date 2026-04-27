package org.example.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Змейка.
 */
public class Snake {
    private final LinkedList<Point> segments;
    private Direction direction;

    /**
     * Создаёт змейку из одного сегмента в заданной позиции.
     */
    public Snake(Point start, Direction direction) {
        this.segments = new LinkedList<>();
        this.segments.add(start);
        this.direction = direction;
    }

    /**
     * Возвращает голову змейки.
     */
    public Point getHead() {
        return segments.getFirst();
    }

    /**
     * Возвращает хвост змейки.
     */
    public Point getTail() {
        return segments.getLast();
    }

    /**
     * Возвращает список сегментов.
     */
    public List<Point> getSegments() {
        return segments;
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * Устанавливает новое направление если оно не противоположно текущему.
     */
    public void setDirection(Direction newDirection) {
        if (!direction.isOpposite(newDirection)) {
            this.direction = newDirection;
        }
    }

    /**
     * Добавляет голову удаляет хвост.
     */
    public void move(Point newHead) {
        segments.addFirst(newHead);
        segments.removeLast();
    }

    /**
     * Добавляет голову без удаления хвоста.
     */
    public void grow(Point newHead) {
        segments.addFirst(newHead);
    }

    /**
     * Занимает ли змейка данную клетку.
     */
    public boolean contains(Point point) {
        return segments.contains(point);
    }

    /**
     * Возвращает длину.
     */
    public int size() {
        return segments.size();
    }
}
