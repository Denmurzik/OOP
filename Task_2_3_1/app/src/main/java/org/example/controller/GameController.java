package org.example.controller;

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
import org.example.model.GameState;
import org.example.view.GameRenderer;

/**
 * Контроллер игры — связывает модель и представление, управляет игровым циклом.
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
    private AnimationTimer gameLoop;
    private long lastTick;
    private Direction pendingDirection;

    @FXML
    public void initialize() {
        GameConfig config = GameConfig.defaultConfig();
        GameField field = new GameField(config.width(), config.height());
        model = new GameModel(config, field,
                (snake, score) -> snake.size() >= config.winLength());

        renderer = new GameRenderer(gameCanvas, CELL_SIZE);

        startGameLoop();
    }

    /**
     * Привязывает обработку клавиатуры к сцене.
     */
    public void initKeyHandling(Scene scene) {
        scene.setOnKeyPressed(event -> handleKey(event.getCode()));
    }

    private void handleKey(KeyCode code) {
        switch (code) {
            case UP, W -> pendingDirection = Direction.UP;
            case DOWN, S -> pendingDirection = Direction.DOWN;
            case LEFT, A -> pendingDirection = Direction.LEFT;
            case RIGHT, D -> pendingDirection = Direction.RIGHT;
            case SPACE -> model.togglePause();
            default -> { }
        }
    }

    private void startGameLoop() {
        lastTick = 0;
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTick == 0) {
                    lastTick = now;
                }

                long elapsed = (now - lastTick) / 1_000_000; // в миллисекунды
                if (elapsed >= model.getConfig().tickMs()) {
                    lastTick = now;

                    if (model.getState() == GameState.RUNNING) {
                        if (pendingDirection != null) {
                            model.changeDirection(pendingDirection);
                            pendingDirection = null;
                        }
                        model.tick();
                    }
                }

                renderer.render(model);
                updateLabels();
            }
        };
        gameLoop.start();
    }

    private void updateLabels() {
        scoreLabel.setText("Счёт: " + model.getScore());
        levelLabel.setText("Длина: " + model.getSnake().size());

        switch (model.getState()) {
            case PAUSED -> stateLabel.setText("ПАУЗА");
            case GAME_OVER -> stateLabel.setText("ИГРА ОКОНЧЕНА");
            case WON -> stateLabel.setText("ПОБЕДА!");
            default -> stateLabel.setText("");
        }
    }

    @FXML
    private void onRestart() {
        model.reset();
        pendingDirection = null;
        stateLabel.setText("");
    }

    @FXML
    private void onPause() {
        model.togglePause();
    }
}
