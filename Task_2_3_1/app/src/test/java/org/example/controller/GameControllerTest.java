package org.example.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class GameControllerTest {

    @Test
    void testControllerInstantiation() {
        GameController controller = new GameController();
        assertNotNull(controller);
    }
}
