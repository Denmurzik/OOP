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

    @Test
    void testRendererClass() {
        assertEquals("org.example.view.GameRenderer", GameRenderer.class.getName());
    }
}
