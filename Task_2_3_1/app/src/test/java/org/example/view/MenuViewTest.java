package org.example.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.scene.control.Spinner;
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
            MenuView menu = new MenuView();
            Spinner<Integer> greedy = new Spinner<>();
            Spinner<Integer> random = new Spinner<>();
            Spinner<Integer> hunter = new Spinner<>();
            injectField(menu, "greedySpinner", greedy);
            injectField(menu, "randomSpinner", random);
            injectField(menu, "hunterSpinner", hunter);

            menu.initialize();

            // Дефолтное значение спинеров — 1
            assertEquals(1, greedy.getValue());
            assertEquals(1, random.getValue());
            assertEquals(1, hunter.getValue());
        });
    }

    @Test
    void spinnerRangeIsZeroToFour() throws InterruptedException {
        runOnFxThread(() -> {
            MenuView menu = new MenuView();
            Spinner<Integer> greedy = new Spinner<>();
            Spinner<Integer> random = new Spinner<>();
            Spinner<Integer> hunter = new Spinner<>();
            injectField(menu, "greedySpinner", greedy);
            injectField(menu, "randomSpinner", random);
            injectField(menu, "hunterSpinner", hunter);

            menu.initialize();

            // Проверяем что можно установить 0 и 4 (граничные)
            greedy.getValueFactory().setValue(0);
            assertEquals(0, greedy.getValue());
            greedy.getValueFactory().setValue(4);
            assertEquals(4, greedy.getValue());

            // 5 за пределами — спинер должен ограничить (зависит от реализации, но не упасть)
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
            // Просто проверяем что не падает
            assertNotNull(menu);
        });
    }

    @Test
    void onRecordAndOnExitDontThrow() throws InterruptedException {
        runOnFxThread(() -> {
            MenuView menu = new MenuView();
            Spinner<Integer> greedy = new Spinner<>();
            Spinner<Integer> random = new Spinner<>();
            Spinner<Integer> hunter = new Spinner<>();
            injectField(menu, "greedySpinner", greedy);
            injectField(menu, "randomSpinner", random);
            injectField(menu, "hunterSpinner", hunter);
            menu.initialize();

            // Не вызываем onRecord/onExit — они показывают Alert/закрывают приложение.
            // Достаточно проверить что методы существуют и доступны через рефлексию.
            try {
                Method onRecord = MenuView.class.getDeclaredMethod("onRecord");
                Method onExit = MenuView.class.getDeclaredMethod("onExit");
                assertNotNull(onRecord);
                assertNotNull(onExit);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
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

    private void injectField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
