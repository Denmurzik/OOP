package org.example.controller;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import org.example.model.Direction;
import org.example.model.GameConfig;
import org.example.model.GameField;
import org.example.model.GameModel;
import org.example.model.GameSnapshot;
import org.example.model.GameState;
import org.example.model.Point;
import org.example.model.Snake;
import org.example.model.WinCondition;
import org.example.view.GameRenderer;

/**
 * Контроллер.
 */
public class GameController {
    private static final int CELL_SIZE = 30;

    @FXML
    private Canvas gameCanvas;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label levelLabel;
    @FXML
    private Label stateLabel;

    private GameModel model;
    private GameRenderer renderer;
    private AnimationTimer renderLoop;

    private Thread gameThread;
    private volatile boolean running;
    private final LinkedList<Direction> directionQueue = new LinkedList<>();
    private static final int MAX_QUEUE_SIZE = 3;


    /**
     * Инициализация.
     */
    @FXML
    public void initialize() {
        GameConfig config = GameConfig.defaultConfig();
        Set<Point> obstacles = createObstacles(config);
        GameField field = new GameField(config.getWidth(), config.getHeight(), obstacles);
        model = new GameModel(config, field, new WinCondition() {
            @Override
            public boolean checkWin(Snake snake, int score) {
                return snake.size() >= config.getWinLength();
            }
        });

        renderer = new GameRenderer(gameCanvas, CELL_SIZE);

        startGameThread();
        startRenderLoop();
    }

    /**
     * Привязывает обработку клавиатуры к сцене.
     */
    public void initKeyHandling(Scene scene) {
        scene.setOnKeyPressed(new javafx.event.EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                handleKey(event.getCode());
            }
        });
    }

    private void handleKey(KeyCode code) {
        Direction dir = null;
        switch (code) {
            case UP:
            case W:
                dir = Direction.UP;
                break;
            case DOWN:
            case S:
                dir = Direction.DOWN;
                break;
            case LEFT:
            case A:
                dir = Direction.LEFT;
                break;
            case RIGHT:
            case D:
                dir = Direction.RIGHT;
                break;
            case SPACE:
                model.togglePause();
                break;
            default:
                break;
        }
        if (dir != null) {
            synchronized (directionQueue) {
                if (directionQueue.size() < MAX_QUEUE_SIZE) {
                    directionQueue.add(dir);
                }
            }
        }
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
     * Запускает цикл отрисовки.
     */
    private void startRenderLoop() {
        renderLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                GameSnapshot snapshot = model.getSnapshot();
                renderer.render(snapshot);
                updateLabels(snapshot);
            }
        };
        renderLoop.start();
    }

    private void updateLabels(GameSnapshot snapshot) {
        scoreLabel.setText("Счёт: " + snapshot.getScore());
        levelLabel.setText("Длина: " + snapshot.getSnakeSize());

        switch (snapshot.getState()) {
            case PAUSED:
                stateLabel.setText("ПАУЗА");
                break;
            case GAME_OVER:
                stateLabel.setText("ИГРА ОКОНЧЕНА");
                break;
            case WON:
                stateLabel.setText("ПОБЕДА!");
                break;
            default:
                stateLabel.setText("");
                break;
        }
    }

    @FXML
    private void onRestart() {
        stopGameThread();
        synchronized (model) {
            model.reset();
        }
        synchronized (directionQueue) {
            directionQueue.clear();
        }
        stateLabel.setText("");
        startGameThread();
    }

    @FXML
    private void onPause() {
        model.togglePause();
    }

    /**
     * Создаёт набор препятствий на поле.
     */
    private Set<Point> createObstacles(GameConfig config) {
        Set<Point> obstacles = new HashSet<>();
        final int cx = config.getWidth() / 2;
        final int cy = config.getHeight() / 2;

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
