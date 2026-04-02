package org.example;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void testInstantiation() {
        Main main = new Main();
        assertNotNull(main);
    }
}
