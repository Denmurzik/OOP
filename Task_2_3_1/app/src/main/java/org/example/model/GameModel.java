package org.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private List<AiSnake> enemies;
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
        this.enemies = new ArrayList<>();
        this.state = GameState.RUNNING;
        this.score = 0;
        spawnFood();
        notifyListener();
    }

    /**
     * Добавляет змейку-робота в игру.
     */
    public synchronized void addEnemy(AiSnake enemy) {
        enemies.add(enemy);
    }

    /**
     * Один тик игры — движение игрока, движение врагов, проверка коллизий.
     */
    public synchronized void tick() {
        if (state != GameState.RUNNING) {
            return;
        }

 
        GameSnapshot worldBefore = getSnapshot();

 
        Map<AiSnake, Point> enemyHeads = new HashMap<>();
        for (AiSnake enemy : enemies) {
            if (!enemy.isAlive()) {
                continue;
            }
            Direction dir = enemy.decide(worldBefore);
            enemy.getSnake().setDirection(dir);
            Point newHead = enemy.getSnake().getHead().move(enemy.getSnake().getDirection());
            enemyHeads.put(enemy, newHead);
        }

      
        Point playerNewHead = snake.getHead().move(snake.getDirection());

        if (!field.isInBounds(playerNewHead) || field.isObstacle(playerNewHead)
                || snake.contains(playerNewHead)) {
            state = GameState.GAME_OVER;
            notifyListener();
            return;
        }

        boolean headOn = false;
        for (Map.Entry<AiSnake, Point> entry : enemyHeads.entrySet()) {
            if (entry.getValue().equals(playerNewHead)) {
                entry.getKey().kill();
                headOn = true;
            }
        }
        if (headOn) {
            state = GameState.GAME_OVER;
            enemies.removeIf(e -> !e.isAlive());
            notifyListener();
            return;
        }


        if (isPlayerHittingEnemyBody(playerNewHead)) {
            state = GameState.GAME_OVER;
            notifyListener();
            return;
        }


        Food eaten = findFoodAt(playerNewHead);
        if (eaten != null) {
            snake.grow(playerNewHead);
            foods.remove(eaten);
            score += eaten.getGrowthValue();
            for (int i = 1; i < eaten.getGrowthValue(); i++) {
                snake.grow(snake.getHead());
            }
        } else {
            snake.move(playerNewHead);
        }


        moveEnemies(enemyHeads);

        enemies.removeIf(e -> !e.isAlive());

        spawnFood();

        if (winCondition.checkWin(snake, score)) {
            state = GameState.WON;
        }
        notifyListener();
    }

    /**
     * Применяет движение всех живых ИИ с проверкой коллизий.
     */
    private void moveEnemies(Map<AiSnake, Point> enemyHeads) {
        for (Map.Entry<AiSnake, Point> e1 : enemyHeads.entrySet()) {
            if (!e1.getKey().isAlive()) {
                continue;
            }
            for (Map.Entry<AiSnake, Point> e2 : enemyHeads.entrySet()) {
                if (e1.getKey() == e2.getKey() || !e2.getKey().isAlive()) {
                    continue;
                }
                if (e1.getValue().equals(e2.getValue())) {
                    e1.getKey().kill();
                    e2.getKey().kill();
                }
            }
        }

        // Применяем движение каждого живого
        for (Map.Entry<AiSnake, Point> entry : enemyHeads.entrySet()) {
            AiSnake enemy = entry.getKey();
            if (!enemy.isAlive()) {
                continue;
            }
            Point newHead = entry.getValue();
            Snake enemySnake = enemy.getSnake();

            // Проверки
            if (!field.isInBounds(newHead) || field.isObstacle(newHead)) {
                enemy.kill();
                continue;
            }
            if (enemySnake.contains(newHead)) {
                enemy.kill();
                continue;
            }
            // Тело игрока
            if (snake.contains(newHead)) {
                enemy.kill();
                continue;
            }
            // Тело других врагов
            if (isOtherEnemyBodyAt(newHead, enemy)) {
                enemy.kill();
                continue;
            }

            // Поедание еды
            Food eaten = findFoodAt(newHead);
            if (eaten != null) {
                enemySnake.grow(newHead);
                foods.remove(eaten);
            } else {
                enemySnake.move(newHead);
            }
        }
    }

    private boolean isPlayerHittingEnemyBody(Point point) {
        for (AiSnake enemy : enemies) {
            if (!enemy.isAlive()) {
                continue;
            }
            List<Point> segs = enemy.getSnake().getSegments();
            // Проверяем все сегменты кроме хвоста
            int last = segs.size() - 1;
            for (int i = 0; i < last; i++) {
                if (segs.get(i).equals(point)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOtherEnemyBodyAt(Point point, AiSnake exclude) {
        for (AiSnake enemy : enemies) {
            if (enemy == exclude || !enemy.isAlive()) {
                continue;
            }
            if (enemy.getSnake().contains(point)) {
                return true;
            }
        }
        return false;
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
        List<Snake> allSnakes = collectAllSnakes();
        while (foods.size() < config.getFoodCount()) {
            Point pos = field.getRandomFreePoint(allSnakes, foods, random);
            if (pos == null) {
                break;
            }
            foods.add(new BasicFood(pos));
        }
    }

    private List<Snake> collectAllSnakes() {
        List<Snake> all = new ArrayList<>();
        all.add(snake);
        for (AiSnake e : enemies) {
            if (e.isAlive()) {
                all.add(e.getSnake());
            }
        }
        return all;
    }

    public Snake getSnake() {
        return snake;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public List<AiSnake> getEnemies() {
        return enemies;
    }

    public synchronized GameState getState() {
        return state;
    }

    /**
     * Возвращает снимок текущего состояния для отрисовки.
     */
    public synchronized GameSnapshot getSnapshot() {
        List<EnemySnapshot> enemySnaps = new ArrayList<>();
        for (AiSnake e : enemies) {
            if (e.isAlive()) {
                enemySnaps.add(new EnemySnapshot(
                        new ArrayList<>(e.getSnake().getSegments()),
                        e.getStrategy().type()
                ));
            }
        }
        return new GameSnapshot(
                new ArrayList<>(snake.getSegments()),
                new ArrayList<>(foods),
                state,
                score,
                snake.size(),
                field,
                enemySnaps
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
