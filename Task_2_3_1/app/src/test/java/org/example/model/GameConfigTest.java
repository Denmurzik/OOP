package org.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GameConfigTest {

    @Test
    void testDefaultConfig() {
        GameConfig config = GameConfig.defaultConfig();
        
        assertEquals(20, config.getWidth());
        assertEquals(15, config.getHeight());
        assertEquals(3, config.getFoodCount());
        assertEquals(10, config.getWinLength());
        assertEquals(200L, config.getTickMs());
    }
}
