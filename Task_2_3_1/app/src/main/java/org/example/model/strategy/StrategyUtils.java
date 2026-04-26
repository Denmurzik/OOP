package org.example.model.strategy;

import java.util.ArrayList;
import java.util.List;
import org.example.model.Direction;
import org.example.model.EnemySnapshot;
import org.example.model.GameSnapshot;
import org.example.model.Point;
import org.example.model.Snake;

/**
 * Вспомогательные методы для стратегий.
 */
public class StrategyUtils {

    private StrategyUtils() { }

    /**
     * Manhattan расстояние между двумя точками.
     */
    public static int manhattan(Point a, Point b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    /**
     * Проверяет, безопасно ли пойти в клетку.
     */
    public static boolean isCellSafe(Point p, GameSnapshot world, Snake self) {
        if (!world.field().isInBounds(p)) {
            return false;
        }
        if (world.field().isObstacle(p)) {
            return false;
        }
        for (Point segment : world.snakeSegments()) {
            if (segment.equals(p)) {
                return false;
            }
        }
        // Тела других врагов
        for (EnemySnapshot enemy : world.enemies()) {
            for (Point segment : enemy.segments()) {
                if (segment.equals(p)) {
                    return false;
                }
            }
        }
        List<Point> selfSegs = self.getSegments();
        for (int i = 0; i < selfSegs.size() - 1; i++) {
            if (selfSegs.get(i).equals(p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Возвращает все направления кроме противоположного текущему.
     */
    public static List<Direction> nonReverseDirections(Direction current) {
        List<Direction> result = new ArrayList<>();
        for (Direction d : Direction.values()) {
            if (!d.isOpposite(current)) {
                result.add(d);
            }
        }
        return result;
    }
}
