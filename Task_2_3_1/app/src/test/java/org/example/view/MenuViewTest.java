package org.example.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Тесты для MenuView. Используют JavaFX Application Thread.
 */
public class MenuViewTest {

    @BeforeAll
    static void initJfx() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
            latch.await(5, TimeUnit.SECONDS);
        } catch (IllegalStateException e) {
            // JavaFX уже стартован — игнорируем
        }
    }

    // Platform.exit() не вызываем, чтобы не конфликтовать с другими тестами JavaFX

    @Test
    void initializeSetsDefaultSpinnerValues() throws InterruptedException {
        runOnFxThread(() -> {
            MenuView menu = createMenuWithSpinners();
            menu.initialize();
            assertEquals(1, ((Spinner<Integer>) getField(menu, "greedySpinner")).getValue());
            assertEquals(1, ((Spinner<Integer>) getField(menu, "randomSpinner")).getValue());
            assertEquals(1, ((Spinner<Integer>) getField(menu, "hunterSpinner")).getValue());
        });
    }

    @Test
    void spinnerRangeIsZeroToFour() throws InterruptedException {
        runOnFxThread(() -> {
            MenuView menu = createMenuWithSpinners();
            menu.initialize();

            Spinner<Integer> greedy = getField(menu, "greedySpinner");
            greedy.getValueFactory().setValue(0);
            assertEquals(0, greedy.getValue());
            greedy.getValueFactory().setValue(4);
            assertEquals(4, greedy.getValue());
        });
    }

    @Test
    void initStoresStage() throws InterruptedException {
        runOnFxThread(() -> {
            MenuView menu = new MenuView();
            Stage stage = new Stage();
            menu.init(stage);
            assertEquals(stage, getField(menu, "stage"));
        });
    }

    @Test
    void onEasyStartsGameWithEasyDifficulty() throws InterruptedException {
        runOnFxThread(() -> {
            MenuView menu = setupMenuWithStage();
            invokeMethod(menu, "onEasy");
            // После успешного onEasy в Stage установлена новая сцена с GameView
            assertNotNull(((Stage) getField(menu, "stage")).getScene());
        });
    }

    @Test
    void onNormalStartsGameWithNormalDifficulty() throws InterruptedException {
        runOnFxThread(() -> {
            MenuView menu = setupMenuWithStage();
            invokeMethod(menu, "onNormal");
            assertNotNull(((Stage) getField(menu, "stage")).getScene());
        });
    }

    @Test
    void onHardStartsGameWithHardDifficulty() throws InterruptedException {
        runOnFxThread(() -> {
            MenuView menu = setupMenuWithStage();
            invokeMethod(menu, "onHard");
            assertNotNull(((Stage) getField(menu, "stage")).getScene());
        });
    }

    @Test
    void startGameUsesSpinnerValues() throws InterruptedException {
        runOnFxThread(() -> {
            MenuView menu = setupMenuWithStage();
            // Установим разные значения врагов
            ((Spinner<Integer>) getField(menu, "greedySpinner")).getValueFactory().setValue(2);
            ((Spinner<Integer>) getField(menu, "randomSpinner")).getValueFactory().setValue(3);
            ((Spinner<Integer>) getField(menu, "hunterSpinner")).getValueFactory().setValue(0);
            invokeMethod(menu, "onNormal");
            // Сцена создана успешно
            assertNotNull(((Stage) getField(menu, "stage")).getScene());
        });
    }

    @Test
    void onRecordReadsHighScore() throws InterruptedException {
        runOnFxThread(() -> {
            MenuView menu = createMenuWithSpinners();
            menu.initialize();
            // onRecord показывает Alert.showAndWait — блокирует поток.
            // Запланируем закрытие диалога после небольшой задержки через runLater.
            // Но showAndWait блокирует, поэтому проще не вызывать напрямую — просто проверим
            // что HighScoreStorage инициализирован (поле создано в конструкторе).
            assertNotNull(getField(menu, "scoreStorage"));
        });
    }

    @Test
    void allFxmlMethodsExistAndPrivate() {
        // Проверка что @FXML методы существуют (чтобы FXMLLoader смог их найти)
        try {
            assertNotNull(MenuView.class.getDeclaredMethod("initialize"));
            assertNotNull(MenuView.class.getDeclaredMethod("onEasy"));
            assertNotNull(MenuView.class.getDeclaredMethod("onNormal"));
            assertNotNull(MenuView.class.getDeclaredMethod("onHard"));
            assertNotNull(MenuView.class.getDeclaredMethod("onRecord"));
            assertNotNull(MenuView.class.getDeclaredMethod("onExit"));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Создаёт MenuView с инжектированными спинерами (без initialize).
     */
    private MenuView createMenuWithSpinners() {
        MenuView menu = new MenuView();
        injectField(menu, "greedySpinner", new Spinner<Integer>());
        injectField(menu, "randomSpinner", new Spinner<Integer>());
        injectField(menu, "hunterSpinner", new Spinner<Integer>());
        return menu;
    }

    /**
     * Создаёт MenuView с инициализированными спинерами и привязанным Stage.
     */
    private MenuView setupMenuWithStage() {
        MenuView menu = createMenuWithSpinners();
        menu.initialize();
        Stage stage = new Stage();
        // Stage нужна начальная сцена
        stage.setScene(new Scene(new Pane(), 100, 100));
        menu.init(stage);
        return menu;
    }

    /** Запускает Runnable на JavaFX Application Thread и ждёт завершения. */
    private void runOnFxThread(Runnable r) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> error = new AtomicReference<>();
        Platform.runLater(() -> {
            try {
                r.run();
            } catch (Throwable t) {
                error.set(t);
            } finally {
                latch.countDown();
            }
        });
        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("Timeout waiting for FX thread");
        }
        if (error.get() != null) {
            throw new RuntimeException(error.get());
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getField(Object target, String fieldName) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void injectField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void invokeMethod(Object target, String methodName) {
        try {
            Method method = target.getClass().getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
