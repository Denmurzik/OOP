package org.example.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Игровое поле.
 */
public class GameField {
    private final int width;
    private final int height;
    private final Set<Point> obstacles;

    /**
     * Основной конструктор.
     */
    public GameField(int width, int height) {
        this.width = width;
        this.height = height;
        this.obstacles = new HashSet<>();
    }

    /**
     * Конструктор с препятствиями.
     */
    public GameField(int width, int height, Set<Point> obstacles) {
        this.width = width;
        this.height = height;
        this.obstacles = new HashSet<>(obstacles);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Set<Point> getObstacles() {
        return obstacles;
    }

    /**
     * Проверяет, находится ли точка в пределах поля.
     */
    public boolean isInBounds(Point point) {
        return point.getX() >= 0 && point.getX() < width
                && point.getY() >= 0 && point.getY() < height;
    }

    /**
     * Проверяет, является ли клетка препятствием.
     */
    public boolean isObstacle(Point point) {
        return obstacles.contains(point);
    }

    /**
     * Находит случайную свободную клетку на поле.
     * Свободная — не занята ни одной змейкой, не препятствие, не еда.
     */
    public Point getRandomFreePoint(List<Snake> snakes, List<Food> foods, Random random) {
        List<Point> freePoints = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Point p = new Point(x, y);
                if (!isObstacle(p) && !isFoodAt(p, foods) && !isAnySnakeAt(p, snakes)) {
                    freePoints.add(p);
                }
            }
        }
        if (freePoints.isEmpty()) {
            return null;
        }
        return freePoints.get(random.nextInt(freePoints.size()));
    }

    private boolean isAnySnakeAt(Point point, List<Snake> snakes) {
        for (Snake snake : snakes) {
            if (snake.contains(point)) {
                return true;
            }
        }
        return false;
    }

    private boolean isFoodAt(Point point, List<Food> foods) {
        for (Food food : foods) {
            if (food.getPosition().equals(point)) {
                return true;
            }
        }
        return false;
    }
}
