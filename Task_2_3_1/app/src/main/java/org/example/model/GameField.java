package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Игровое поле размером width x height с препятствиями.
 */
public class GameField {
    private final int width;
    private final int height;
    private final Set<Point> obstacles;

    public GameField(int width, int height) {
        this.width = width;
        this.height = height;
        this.obstacles = new HashSet<>();
    }

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
        return Collections.unmodifiableSet(obstacles);
    }

    /**
     * Проверяет, находится ли точка в пределах поля.
     */
    public boolean isInBounds(Point point) {
        return point.x() >= 0 && point.x() < width
                && point.y() >= 0 && point.y() < height;
    }

    /**
     * Проверяет, является ли клетка препятствием.
     */
    public boolean isObstacle(Point point) {
        return obstacles.contains(point);
    }

    /**
     * Находит случайную свободную клетку на поле.
     */
    public Point getRandomFreePoint(Snake snake, List<Food> foods, Random random) {
        List<Point> freePoints = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Point p = new Point(x, y);
                if (!snake.contains(p) && !isObstacle(p) && !isFoodAt(p, foods)) {
                    freePoints.add(p);
                }
            }
        }
        if (freePoints.isEmpty()) {
            return null;
        }
        return freePoints.get(random.nextInt(freePoints.size()));
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
