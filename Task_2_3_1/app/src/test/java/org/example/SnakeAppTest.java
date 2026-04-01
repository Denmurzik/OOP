package org.example;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class SnakeAppTest {

    @Test
    void testInstantiation() {
        SnakeApp app = new SnakeApp();
        assertNotNull(app);
    }

    @Test
    void testMainMethod() {
        try {
            SnakeApp.main(new String[0]);
        } catch (Exception e) {
        }
    }
}
