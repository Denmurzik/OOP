package org.example.model;

/**
 * Змейка-робот: обёртка над Snake + стратегия поведения.
 */
public class AiSnake {
    private final Snake snake;
    private final SnakeStrategy strategy;
    private boolean alive;

    public AiSnake(Snake snake, SnakeStrategy strategy) {
        this.snake = snake;
        this.strategy = strategy;
        this.alive = true;
    }

    public Snake getSnake() {
        return snake;
    }

    public SnakeStrategy getStrategy() {
        return strategy;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        this.alive = false;
    }

    /**
     * Спрашивает у стратегии следующее направление.
     */
    public Direction decide(GameSnapshot world) {
        return strategy.nextMove(snake, world);
    }
}
