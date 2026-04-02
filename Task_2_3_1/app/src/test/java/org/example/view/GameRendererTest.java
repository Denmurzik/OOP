package org.example.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GameRendererTest {

    @Test
    void testRendererClass() {
        assertEquals("org.example.view.GameRenderer", GameRenderer.class.getName());
    }
}
