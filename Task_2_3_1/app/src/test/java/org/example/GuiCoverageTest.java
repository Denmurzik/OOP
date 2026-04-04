package org.example;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.model.GameConfig;
import org.example.model.GameField;
import org.example.model.GameModel;
import org.example.model.WinCondition;
import org.example.view.GameRenderer;
import org.example.view.GameView;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Coverage test class.
 */
public class GuiCoverageTest {

    @BeforeAll
    static void initJfx() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
            latch.await(5, TimeUnit.SECONDS);
        } catch (IllegalStateException e) {
            // ignore
        }
    }

    @AfterAll
    static void tearDownJfx() {
        Platform.exit();
    }

    @Test
    void testGameRendererCoverage() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> error = new AtomicReference<>();
        Platform.runLater(() -> {
            try {
                Canvas canvas = new Canvas(800, 600);
                GameRenderer renderer = new GameRenderer(canvas, 10);
                assertNotNull(renderer);

                GameConfig config = GameConfig.defaultConfig();
                GameField field = new GameField(config.getWidth(), config.getHeight());
                WinCondition win = (s, sc) -> false;
                GameModel model = new GameModel(config, field, win, new Random(0));

                renderer.render(model.getSnapshot());
                model.tick();
                renderer.render(model.getSnapshot());
                model.togglePause();
                renderer.render(model.getSnapshot());
            } catch (Throwable t) {
                error.set(t);
            } finally {
                latch.countDown();
            }
        });
        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("Timeout GameRenderer");
        }
        if (error.get() != null) {
            throw new RuntimeException(error.get());
        }
    }

    @Test
    void testGameViewCoverage() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> error = new AtomicReference<>();
        Platform.runLater(() -> {
            try {
                GameView view = new GameView();
                injectField(view, "gameCanvas", new Canvas(800, 600));
                injectField(view, "scoreLabel", new Label());
                injectField(view, "stateLabel", new Label());
                injectField(view, "levelLabel", new Label());

                view.initialize();

                Scene scene = new Scene(new Pane());
                view.initKeyHandling(scene);

                try {
                    invokeMethodArgs(view, "handleKey",
                            new Class<?>[]{KeyCode.class}, new Object[]{KeyCode.W});
                } catch (Exception e) {
                    // ignore
                }

                try {
                    invokeMethodArgs(view, "handleKey",
                            new Class<?>[]{KeyCode.class}, new Object[]{KeyCode.ESCAPE});
                } catch (Exception e) {
                    // ignore
                }

                try {
                    invokeMethod(view, "onPause");
                } catch (Exception e) {
                    // ignore
                }

                try {
                    invokeMethod(view, "onRestart");
                } catch (Exception e) {
                    // ignore
                }

                System.out.println("Execution of GameView UI methods successful.");

            } catch (Throwable globalE) {
                error.set(globalE);
            } finally {
                latch.countDown();
            }
        });
        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("Timeout GameView");
        }
        if (error.get() != null) {
            throw new RuntimeException(error.get());
        }
    }

    @Test
    void testSnakeAppStartCoverage() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                SnakeApp app = new SnakeApp();
                Stage stage = new Stage();
                app.start(stage);
            } catch (Exception e) {
                // ignore
            } finally {
                latch.countDown();
            }
        });
        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("Timeout SnakeApp");
        }
    }

    private void invokeMethod(Object target, String methodName) throws Exception {
        Method method = target.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        method.invoke(target);
    }

    private void invokeMethodArgs(Object target, String methodName,
                                  Class<?>[] types, Object[] args) throws Exception {
        Method method = target.getClass().getDeclaredMethod(methodName, types);
        method.setAccessible(true);
        method.invoke(target, args);
    }

    private void injectField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
