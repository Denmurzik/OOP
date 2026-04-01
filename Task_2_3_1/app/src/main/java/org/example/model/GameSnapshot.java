package org.example.model;

import java.util.List;

/**
 * Снимок состояния игры для отрисовки.
 */
public class GameSnapshot {
    private final List<Point> snakeSegments;
    private final List<Food> foods;
    private final GameState state;
    private final int score;
    private final int snakeSize;
    private final GameField field;

    public GameSnapshot(List<Point> snakeSegments, List<Food> foods, GameState state, int score, int snakeSize, GameField field) {
        this.snakeSegments = snakeSegments;
        this.foods = foods;
        this.state = state;
        this.score = score;
        this.snakeSize = snakeSize;
        this.field = field;
    }

    public List<Point> getSnakeSegments() {
        return snakeSegments;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public GameState getState() {
        return state;
    }

    public int getScore() {
        return score;
    }

    public int getSnakeSize() {
        return snakeSize;
    }

    public GameField getField() {
        return field;
    }
}
