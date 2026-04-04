package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Управляет логикой змейки, еды и состоянием.
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
    private ModelListener listener;

    /**
     * Конструктор без рандома.
     */
    public GameModel(GameConfig config, GameField field, WinCondition winCondition) {
        this(config, field, winCondition, new Random());
    }

    /**
     * Конструктор.
     */
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
    public synchronized void reset() {
        int startX = config.getWidth() / 2;
        int startY = config.getHeight() / 2;
        this.snake = new Snake(new Point(startX, startY), Direction.RIGHT);
        this.foods = new ArrayList<>();
        this.state = GameState.RUNNING;
        this.score = 0;
        spawnFood();
        notifyListener();
    }

    /**
     * Один тик игры перемещение змейки, проверка коллизий и еды.
     */
    public synchronized void tick() {
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
            score += eaten.getGrowthValue();
            // рост
            for (int i = 1; i < eaten.getGrowthValue(); i++) {
                snake.grow(snake.getHead());
            }
            spawnFood();
        } else {
            snake.move(newHead);
        }

        // Проверка победы
        if (winCondition.checkWin(snake, score)) {
            state = GameState.WON;
        }
        notifyListener();
    }

    /**
     * Меняет направление змейки.
     */
    public synchronized void changeDirection(Direction direction) {
        snake.setDirection(direction);
    }

    /**
     * Переключает паузу.
     */
    public synchronized void togglePause() {
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
        } else if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        }
        notifyListener();
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
     * Устанавливает слушателя изменений модели.
     */
    public void setListener(ModelListener listener) {
        this.listener = listener;
    }

    /**
     * Уведомляет слушателя о текущем состоянии.
     */
    private void notifyListener() {
        if (listener != null) {
            listener.onModelUpdated(getSnapshot());
        }
    }

    /**
     * Спавнит еду до тех пор, пока на поле не будет нужное количество.
     */
    private void spawnFood() {
        while (foods.size() < config.getFoodCount()) {
            Point pos = field.getRandomFreePoint(snake, foods, random);
            if (pos == null) {
                break; 
            }
            foods.add(new BasicFood(pos));
        }
    }


    public Snake getSnake() {
        return snake;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public synchronized GameState getState() {
        return state;
    }

    /**
     * Возвращает снимок текущего состояния для отрисовки.
     */
    public synchronized GameSnapshot getSnapshot() {
        return new GameSnapshot(
                new ArrayList<>(snake.getSegments()),
                new ArrayList<>(foods),
                state,
                score,
                snake.size(),
                field
        );
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
