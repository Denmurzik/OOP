package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Центральная модель игры — управляет логикой змейки, еды и состоянием.
 */
public class GameModel {
    private final GameConfig config;
    private final GameField field;
    private final WinCondition winCondition;
    private final Random random;

    private Snake snake;
    private List<Food> foods;
    private GameState state;
    private int score;

    public GameModel(GameConfig config, GameField field, WinCondition winCondition) {
        this(config, field, winCondition, new Random());
    }

    public GameModel(GameConfig config, GameField field, WinCondition winCondition, Random random) {
        this.config = config;
        this.field = field;
        this.winCondition = winCondition;
        this.random = random;
        reset();
    }

    /**
     * Сбрасывает игру в начальное состояние.
     */
    public void reset() {
        int startX = config.width() / 2;
        int startY = config.height() / 2;
        this.snake = new Snake(new Point(startX, startY), Direction.RIGHT);
        this.foods = new ArrayList<>();
        this.state = GameState.RUNNING;
        this.score = 0;
        spawnFood();
    }

    /**
     * Один тик игры — перемещение змейки, проверка коллизий и еды.
     */
    public void tick() {
        if (state != GameState.RUNNING) {
            return;
        }

        Point newHead = snake.getHead().move(snake.getDirection());

        // Проверка столкновения со стеной
        if (!field.isInBounds(newHead)) {
            state = GameState.GAME_OVER;
            return;
        }

        // Проверка столкновения с препятствием
        if (field.isObstacle(newHead)) {
            state = GameState.GAME_OVER;
            return;
        }

        // Проверка столкновения с телом
        if (snake.contains(newHead)) {
            state = GameState.GAME_OVER;
            return;
        }

        // Проверка еды
        Food eaten = findFoodAt(newHead);
        if (eaten != null) {
            snake.grow(newHead);
            foods.remove(eaten);
            score++;
            spawnFood();
        } else {
            snake.move(newHead);
        }

        // Проверка победы
        if (winCondition.isMet(snake, score)) {
            state = GameState.WON;
        }
    }

    /**
     * Меняет направление змейки.
     */
    public void changeDirection(Direction direction) {
        snake.setDirection(direction);
    }

    /**
     * Переключает паузу.
     */
    public void togglePause() {
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
        } else if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        }
    }

    private Food findFoodAt(Point point) {
        for (Food food : foods) {
            if (food.getPosition().equals(point)) {
                return food;
            }
        }
        return null;
    }

    /**
     * Спавнит еду до тех пор, пока на поле не будет нужное количество.
     */
    private void spawnFood() {
        while (foods.size() < config.foodCount()) {
            Point pos = field.getRandomFreePoint(snake, foods, random);
            if (pos == null) {
                break; // нет свободных клеток
            }
            foods.add(new Food(pos));
        }
    }

    // Геттеры

    public Snake getSnake() {
        return snake;
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

    public GameConfig getConfig() {
        return config;
    }

    public GameField getField() {
        return field;
    }
}
