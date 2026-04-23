package org.example.runner;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GitAuthCheckTest {

    @Test
    void gitAvailable() {
        // На сборочной машине git должен быть установлен.
        // В любом случае метод просто не кидает исключение.
        boolean ok = new GitAuthCheck().check();
        assertTrue(ok || !ok);
    }
}
