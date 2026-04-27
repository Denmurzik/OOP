package org.example.model;

import java.util.List;

/**
 * Снимок состояния игры для отрисовки.
 */
public record GameSnapshot(
        List<Point> snakeSegments,
        List<Food> foods,
        GameState state,
        int score,
        int snakeSize,
        GameField field,
        List<EnemySnapshot> enemies
) {
}
