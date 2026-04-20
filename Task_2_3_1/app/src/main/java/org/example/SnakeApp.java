package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.view.ErrorDialog;
import org.example.view.MenuView;

/**
 * JavaFX.
 */
public class SnakeApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/menu.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);


            MenuView menu = loader.getController();
            menu.init(primaryStage);

            primaryStage.setTitle("Змейка");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.setMinWidth(400);
            primaryStage.setMinHeight(400);
            primaryStage.show();
        } catch (Exception e) {
            ErrorDialog.show(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
