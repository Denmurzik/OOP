package org.example.view;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import org.example.model.GameConfig;
import org.example.model.GameField;
import org.example.model.GameModel;
import org.example.model.WinCondition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;

class GameRendererTest {

    @BeforeAll
    static void initJfx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Успешно, Toolkit уже запущен
        }
    }

    @Test
    void testRender() {
        Canvas canvas = new Canvas(800, 600);
        GameRenderer renderer = new GameRenderer(canvas, 20);
        assertNotNull(renderer);

        GameConfig config = GameConfig.defaultConfig();
        GameField field = new GameField(config.getWidth(), config.getHeight());
        WinCondition win = (s, sc) -> false;
        GameModel model = new GameModel(config, field, win, new Random(0));

        // Отрисуем различные состояния
        renderer.render(model.getSnapshot());

        model.tick();
        renderer.render(model.getSnapshot());

        model.togglePause();
        renderer.render(model.getSnapshot());
    }
}
