package org.example.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class GameControllerTest {


    
    @Test
    void testInstantiation() {
        GameController controller = new GameController();
        assertNotNull(controller);
    }
}
