package org.example;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class SnakeAppTest {

    @Test
    void testInstantiation() {
        SnakeApp app = new SnakeApp();
        assertNotNull(app);
    }
}
