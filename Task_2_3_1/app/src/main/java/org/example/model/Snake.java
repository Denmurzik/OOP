package org.example.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Змейка — упорядоченный набор сегментов с головой и хвостом.
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
     * Возвращает неизменяемый список сегментов.
     */
    public List<Point> getSegments() {
        return Collections.unmodifiableList(segments);
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * Устанавливает новое направление, если оно не противоположно текущему.
     */
    public void setDirection(Direction newDirection) {
        if (!direction.isOpposite(newDirection)) {
            this.direction = newDirection;
        }
    }

    /**
     * Перемещает змейку: добавляет голову, удаляет хвост.
     */
    public void move(Point newHead) {
        segments.addFirst(newHead);
        segments.removeLast();
    }

    /**
     * Растит змейку: добавляет голову без удаления хвоста.
     */
    public void grow(Point newHead) {
        segments.addFirst(newHead);
    }

    /**
     * Проверяет, занимает ли змейка данную клетку.
     */
    public boolean contains(Point point) {
        return segments.contains(point);
    }

    /**
     * Возвращает длину змейки.
     */
    public int size() {
        return segments.size();
    }
}
