package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.GameController;

/**
 * Главный.
 */
public class SnakeApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            GameController controller = loader.getController();
            controller.initKeyHandling(scene);

            primaryStage.setTitle("Змейка");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            javafx.scene.control.Alert alert
                = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Ошибка инициализации");
            alert.setHeaderText("ОШИБКА: Не удалось загрузить интерфейс игры");
            alert.setContentText("Убедитесь, что файл game.fxml существует.\nПодробности: "
                + e.getMessage());
            alert.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
