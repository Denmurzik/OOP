package org.example.view;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Утилита для показа ошибок в диалоговом окне.
 */
public class ErrorDialog {

    private ErrorDialog() { }

    /**
     * Показывает ошибку.
     */
    public static void show(Throwable error) {
        Runnable show = () -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Произошла ошибка");
            String message = error.getMessage();
            if (message == null) {
                message = error.getClass().getSimpleName();
            }
            alert.setContentText(message);
            alert.showAndWait();
        };
        if (Platform.isFxApplicationThread()) {
            show.run();
        } else {
            Platform.runLater(show);
        }
    }
}
