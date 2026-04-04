package org.example.controller;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.example.model.Direction;
import org.example.model.GameConfig;
import org.example.model.GameField;
import org.example.model.GameModel;
import org.example.model.GameState;
import org.example.model.ModelListener;
import org.example.model.Point;
import org.example.model.Snake;
import org.example.model.WinCondition;

/**
 * Контроллер.
 */
public class GameController {
    private static final int MAX_QUEUE_SIZE = 3;

    private final GameModel model;
    private final LinkedList<Direction> directionQueue = new LinkedList<>();

    private Thread gameThread;
    private volatile boolean running;

    /**
     * Инициализирует.
     */
    public GameController(ModelListener listener) {
        GameConfig config = GameConfig.defaultConfig();
        Set<Point> obstacles = createObstacles(config);
        GameField field = new GameField(config.getWidth(), config.getHeight(), obstacles);
        model = new GameModel(config, field, new WinCondition() {
            @Override
            public boolean checkWin(Snake snake, int score) {
                return snake.size() >= config.getWinLength();
            }
        });
        model.setListener(listener);
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
     * Перезапускает игру.
     */
    public void handleRestart() {
        stopGameThread();
        synchronized (model) {
            model.reset();
        }
        synchronized (directionQueue) {
            directionQueue.clear();
        }
        startGameThread();
    }

    /**
     * Запускает поток игровой логики.
     */
    private void startGameThread() {
        running = true;
        gameThread = new Thread(new Runnable() {
            @Override
            public void run() {
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
     * Создаёт набор препятствий на поле.
     */
    private Set<Point> createObstacles(GameConfig config) {
        Set<Point> obstacles = new HashSet<>();
        int cx = config.getWidth() / 2;
        int cy = config.getHeight() / 2;

        // Горизонтальная стенка сверху
        for (int x = 5; x <= 8; x++) {
            obstacles.add(new Point(x, 3));
        }
        // Горизонтальная стенка снизу
        for (int x = 11; x <= 14; x++) {
            obstacles.add(new Point(x, config.getHeight() - 4));
        }
        // Вертикальная стенка слева
        for (int y = 5; y <= 8; y++) {
            obstacles.add(new Point(3, y));
        }
        // Вертикальная стенка справа
        for (int y = 6; y <= 9; y++) {
            obstacles.add(new Point(config.getWidth() - 4, y));
        }

        // Убираем препятствия из зоны старта змейки
        Set<Point> safe = new HashSet<>();
        for (Point p : obstacles) {
            if (Math.abs(p.getX() - cx) <= 2 && Math.abs(p.getY() - cy) <= 2) {
                safe.add(p);
            }
        }
        obstacles.removeAll(safe);

        return obstacles;
    }
}
