package org.example.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import org.example.controller.GameController;
import org.example.model.Direction;
import org.example.model.GameSnapshot;
import org.example.model.GameState;
import org.example.model.ModelListener;

/**
 * Представление. Отвечает за отрисовку и передачу событий контроллеру.
 */
public class GameView implements ModelListener {
    private static final int CELL_SIZE = 30;

    @FXML
    private Canvas gameCanvas;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label levelLabel;
    @FXML
    private Label stateLabel;

    private GameController controller;
    private GameRenderer renderer;

    /**
     * Инициализация. Создаёт рендерер и контроллер.
     */
    @FXML
    public void initialize() {
        renderer = new GameRenderer(gameCanvas, CELL_SIZE);
        controller = new GameController(this);
    }

    /**
     * Привязывает обработку клавиатуры к сцене.
     */
    public void initKeyHandling(Scene scene) {
        scene.setOnKeyPressed(event -> handleKey(event.getCode()));
    }

    private void handleKey(KeyCode code) {
        switch (code) {
            case UP:
            case W:
                controller.handleDirection(Direction.UP);
                break;
            case DOWN:
            case S:
                controller.handleDirection(Direction.DOWN);
                break;
            case LEFT:
            case A:
                controller.handleDirection(Direction.LEFT);
                break;
            case RIGHT:
            case D:
                controller.handleDirection(Direction.RIGHT);
                break;
            case SPACE:
                controller.handlePause();
                break;
            default:
                break;
        }
    }

    @FXML
    private void onRestart() {
        controller.handleRestart();
    }

    @FXML
    private void onPause() {
        controller.handlePause();
    }

    /**
     * Вызывается моделью после изменения состояния.
     * Использует Platform.runLater, так как вызов идёт из игрового потока.
     */
    @Override
    public void onModelUpdated(GameSnapshot snapshot) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                renderer.render(snapshot);
                updateLabels(snapshot);
            }
        });
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
}
