package org.example.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.example.model.AiSnake;
import org.example.model.Difficulty;
import org.example.model.Direction;
import org.example.model.EnemyCounts;
import org.example.model.GameConfig;
import org.example.model.GameField;
import org.example.model.GameModel;
import org.example.model.GameState;
import org.example.model.ModelListener;
import org.example.model.Point;
import org.example.model.Snake;
import org.example.model.SnakeStrategy;
import org.example.model.WinCondition;
import org.example.model.strategy.GreedyStrategy;
import org.example.model.strategy.HunterStrategy;
import org.example.model.strategy.RandomStrategy;
import org.example.view.ErrorDialog;

/**
 * Контроллер.
 */
public class GameController {
    private static final int MAX_QUEUE_SIZE = 3;

    // Заготовленные фигуры препятствий (смещения от якорной точки)
    private static final int[][][] SHAPES = {
        // Горизонтальная линия из 4 клеток
        {{0, 0}, {1, 0}, {2, 0}, {3, 0}},
        // Вертикальная линия из 4 клеток
        {{0, 0}, {0, 1}, {0, 2}, {0, 3}},
        // Квадрат 2x2
        {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
        // L-образная фигура
        {{0, 0}, {0, 1}, {0, 2}, {1, 2}},
        // Плюс
        {{1, 0}, {0, 1}, {1, 1}, {2, 1}, {1, 2}},
        // Т-образная фигура
        {{0, 0}, {1, 0}, {2, 0}, {1, 1}, {1, 2}}
    };

    private final LinkedList<Direction> directionQueue = new LinkedList<>();
    private final ModelListener listener;
    private final Difficulty difficulty;
    private final EnemyCounts enemyCounts;

    private GameModel model;
    private Thread gameThread;
    private volatile boolean running;

    /**
     * Инициализирует контроллер с заданной сложностью и количеством врагов.
     */
    public GameController(ModelListener listener, Difficulty difficulty, EnemyCounts enemyCounts) {
        this.listener = listener;
        this.difficulty = difficulty;
        this.enemyCounts = enemyCounts;
        model = createNewModel();
        startGameThread();
    }

    /**
     * Обрабатывает нажатие клавиши направления.
     */
    public void handleDirection(Direction dir) {
        synchronized (directionQueue) {
            if (directionQueue.size() < MAX_QUEUE_SIZE) {
                directionQueue.add(dir);
            }
        }
    }

    /**
     * Переключает паузу.
     */
    public void handlePause() {
        model.togglePause();
    }

    /**
     * Перезапускает игру с новым полем (новыми препятствиями).
     */
    public void handleRestart() {
        stopGameThread();
        model = createNewModel();
        synchronized (directionQueue) {
            directionQueue.clear();
        }
        startGameThread();
    }

    /**
     * Останавливает игру (вызывается при возврате в меню).
     */
    public void stopGame() {
        stopGameThread();
    }


    /**
     * Создаёт новую модель с новым полем, препятствиями и врагами.
     */
    private GameModel createNewModel() {
        GameConfig config = GameConfig.withDifficulty(difficulty);
        Set<Point> obstacles = createObstacles(config);
        GameField field = new GameField(config.getWidth(), config.getHeight(), obstacles);
        // Условие победы не используется — играем пока не врежемся
        WinCondition winCondition = (snake, score) -> false;
        GameModel newModel = new GameModel(config, field, winCondition);
        newModel.setListener(listener);

        // Спавним врагов в количестве, указанном пользователем
        for (int i = 0; i < enemyCounts.greedy(); i++) {
            spawnEnemy(newModel, config, new GreedyStrategy());
        }
        for (int i = 0; i < enemyCounts.random(); i++) {
            spawnEnemy(newModel, config, new RandomStrategy());
        }
        for (int i = 0; i < enemyCounts.hunter(); i++) {
            spawnEnemy(newModel, config, new HunterStrategy());
        }

        return newModel;
    }

    /**
     * Создаёт врага со случайной позицией подальше от игрока.
     */
    private void spawnEnemy(GameModel model, GameConfig config, SnakeStrategy strategy) {
        Random random = new Random();
        int cx = config.getWidth() / 2;
        int cy = config.getHeight() / 2;

        Point startPos = null;
        for (int attempt = 0; attempt < 100; attempt++) {
            int x = random.nextInt(config.getWidth());
            int y = random.nextInt(config.getHeight());
            // Подальше от игрока (минимум 5 клеток)
            if (Math.abs(x - cx) + Math.abs(y - cy) < 5) {
                continue;
            }
            Point p = new Point(x, y);
            if (model.getField().isObstacle(p)) {
                continue;
            }
            // Не на уже размещённого врага
            if (isCellTakenByEnemy(p, model)) {
                continue;
            }
            startPos = p;
            break;
        }
        if (startPos == null) {
            return;  // не нашли место — пропускаем
        }

        Direction[] dirs = Direction.values();
        Direction dir = dirs[random.nextInt(dirs.length)];
        Snake snake = new Snake(startPos, dir);
        model.addEnemy(new AiSnake(snake, strategy));
    }

    private boolean isCellTakenByEnemy(Point p, GameModel model) {
        for (AiSnake e : model.getEnemies()) {
            if (e.getSnake().contains(p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Запускает поток игровой логики.
     */
    private void startGameThread() {
        running = true;
        gameThread = new Thread(() -> {
            try {
                while (running) {
                    synchronized (model) {
                        if (model.getState() == GameState.RUNNING) {
                            synchronized (directionQueue) {
                                if (!directionQueue.isEmpty()) {
                                    model.changeDirection(directionQueue.poll());
                                }
                            }
                            model.tick();
                        }
                    }
                    try {
                        Thread.sleep(model.getConfig().getTickMs());
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            } catch (Throwable t) {
                ErrorDialog.show(t);
            }
        }, "GameLogicThread");
        gameThread.setDaemon(true);
        gameThread.start();
    }

    /**
     * Останавливает поток игровой логики.
     */
    private void stopGameThread() {
        running = false;
        if (gameThread != null) {
            gameThread.interrupt();
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                // игнорируем
            }
        }
    }

    /**
     * Создаёт случайный набор препятствий из заготовленных фигур.
     */
    private Set<Point> createObstacles(GameConfig config) {
        Random random = new Random();
        Set<Point> obstacles = new HashSet<>();

        int cx = config.getWidth() / 2;
        int cy = config.getHeight() / 2;

        // Случайное количество фигур от 3 до 6
        int shapeCount = 3 + random.nextInt(4);

        int attempts = 0;
        int placed = 0;
        while (placed < shapeCount && attempts < 100) {
            attempts++;

            // Выбираем случайную фигуру
            int[][] shape = SHAPES[random.nextInt(SHAPES.length)];

            // Случайная якорная точка
            int anchorX = random.nextInt(config.getWidth());
            int anchorY = random.nextInt(config.getHeight());

            // Собираем клетки фигуры
            List<Point> cells = new ArrayList<>();
            boolean fits = true;
            for (int[] offset : shape) {
                int x = anchorX + offset[0];
                int y = anchorY + offset[1];

                // Проверка границ
                if (x < 0 || x >= config.getWidth() || y < 0 || y >= config.getHeight()) {
                    fits = false;
                    break;
                }
                // Проверка зоны старта змейки (3x3 вокруг центра)
                if (Math.abs(x - cx) <= 2 && Math.abs(y - cy) <= 2) {
                    fits = false;
                    break;
                }
                // Проверка пересечения с уже размещёнными фигурами
                Point p = new Point(x, y);
                if (obstacles.contains(p)) {
                    fits = false;
                    break;
                }
                cells.add(p);
            }

            if (fits) {
                obstacles.addAll(cells);
                placed++;
            }
        }

        return obstacles;
    }
}
