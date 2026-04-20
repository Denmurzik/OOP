package org.example.view;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.controller.GameController;
import org.example.model.Difficulty;
import org.example.model.Direction;
import org.example.model.GameSnapshot;
import org.example.model.GameState;
import org.example.model.ModelListener;
import org.example.storage.HighScoreStorage;

/**
 * Представление. Отвечает за отрисовку и передачу событий контроллеру.
 */
public class GameView implements ModelListener {

    @FXML
    private Canvas gameCanvas;
    @FXML
    private Pane canvasContainer;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label stateLabel;

    private GameController controller;
    private GameRenderer renderer;
    private Stage stage;
    private GameSnapshot lastSnapshot;
    private boolean scoreSaved;
    private final HighScoreStorage scoreStorage = new HighScoreStorage();

    /**
     * Инициализация. Создаёт рендерер и настраивает биндинг размера.
     * Контроллер создаётся позже в startGame(Difficulty).
     */
    @FXML
    public void initialize() {
        renderer = new GameRenderer(gameCanvas);

        // Биндим размер Canvas к размеру контейнера
        gameCanvas.widthProperty().bind(canvasContainer.widthProperty());
        gameCanvas.heightProperty().bind(canvasContainer.heightProperty());

        // При изменении размера — перерисовываем
        ChangeListener<Number> resizeListener = (obs, oldVal, newVal) -> {
            if (lastSnapshot != null) {
                renderer.render(lastSnapshot);
            }
        };
        gameCanvas.widthProperty().addListener(resizeListener);
        gameCanvas.heightProperty().addListener(resizeListener);
    }

    /**
     * Запускает игру с заданной сложностью.
     */
    public void startGame(Difficulty difficulty) {
        controller = new GameController(this, difficulty);
    }

    /**
     * Передаёт ссылку на Stage для возврата в меню.
     */
    public void init(Stage stage) {
        this.stage = stage;
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
        scoreSaved = false;
        controller.handleRestart();
    }

    @FXML
    private void onPause() {
        controller.handlePause();
    }

    @FXML
    private void onBackToMenu() {
        try {
            controller.stopGame();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/menu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            MenuView menu = loader.getController();
            menu.init(stage);

            stage.setScene(scene);
        } catch (Exception e) {
            ErrorDialog.show(e);
        }
    }

    /**
     * Вызывается моделью после изменения состояния.
     * Использует Platform.runLater, так как вызов идёт из игрового потока.
     */
    @Override
    public void onModelUpdated(GameSnapshot snapshot) {
        lastSnapshot = snapshot;
        Platform.runLater(() -> {
            renderer.render(snapshot);
            updateLabels(snapshot);
            saveScoreIfNeeded(snapshot);
        });
    }

    private void updateLabels(GameSnapshot snapshot) {
        scoreLabel.setText("Счёт: " + snapshot.score());

        switch (snapshot.state()) {
            case PAUSED:
                stateLabel.setText("ПАУЗА");
                break;
            case GAME_OVER:
                stateLabel.setText("ИГРА ОКОНЧЕНА");
                break;
            default:
                stateLabel.setText("");
                break;
        }
    }

    private void saveScoreIfNeeded(GameSnapshot snapshot) {
        if (scoreSaved) {
            return;
        }
        if (snapshot.state() == GameState.GAME_OVER || snapshot.state() == GameState.WON) {
            scoreStorage.saveIfBetter(snapshot.score());
            scoreSaved = true;
        }
    }
}
