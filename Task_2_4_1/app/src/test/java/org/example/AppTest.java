package org.example;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class AppTest {
    @Test
    void appClassLoads() {
        assertNotNull(App.class.getName());
    }
}
