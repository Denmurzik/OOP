package org.example.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class GameControllerTest {

    @BeforeAll
    static void initJfx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
        }
    }

    @Test
    void testControllerFull() throws Exception {
        GameController controller = new GameController();
        assertNotNull(controller);


        injectField(controller, "gameCanvas", new Canvas(800, 600));
        injectField(controller, "scoreLabel", new Label());
        injectField(controller, "stateLabel", new Label());


        controller.initialize();
        
        Scene scene = new Scene(new Pane());
        controller.initKeyHandling(scene);
        
        invokeMethod(controller, "onPause");
        invokeMethod(controller, "onRestart");
    }

    private void invokeMethod(Object target, String methodName) throws Exception {
        java.lang.reflect.Method method = target.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        method.invoke(target);
    }

    private void injectField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
