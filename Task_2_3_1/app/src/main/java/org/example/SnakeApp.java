package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.GameController;

/**
 * Главный класс JavaFX приложения "Змейка".
 */
public class SnakeApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        GameController controller = loader.getController();
        controller.initKeyHandling(scene);

        primaryStage.setTitle("Змейка");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
