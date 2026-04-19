package org.example.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.model.Difficulty;
import org.example.storage.HighScoreStorage;

/**
 * Контроллер главного меню.
 */
public class MenuView {
    private Stage stage;
    private final HighScoreStorage scoreStorage = new HighScoreStorage();

    /**
     * Передаёт ссылку на Stage для переключения сцен.
     */
    public void init(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void onEasy() {
        startGame(Difficulty.EASY);
    }

    @FXML
    private void onNormal() {
        startGame(Difficulty.NORMAL);
    }

    @FXML
    private void onHard() {
        startGame(Difficulty.HARD);
    }

    @FXML
    private void onRecord() {
        int record = scoreStorage.load();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Рекорд");
        alert.setHeaderText("Лучший результат");
        alert.setContentText("Ваш рекорд: " + record);
        alert.showAndWait();
    }

    @FXML
    private void onExit() {
        Platform.exit();
    }

    private void startGame(Difficulty difficulty) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/game.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            GameView view = loader.getController();
            view.init(stage);
            view.initKeyHandling(scene);
            view.startGame(difficulty);

            stage.setScene(scene);
        } catch (Exception e) {
            ErrorDialog.show(e);
        }
    }
}
