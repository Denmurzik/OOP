package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class PizzeriaInterruptExceptionTest {

    @Test
    void testMessage() {
        PizzeriaInterruptException ex = new PizzeriaInterruptException("тест", null);
        assertEquals("тест", ex.getMessage());
    }

    @Test
    void testCause() {
        InterruptedException cause = new InterruptedException("причина");
        PizzeriaInterruptException ex = new PizzeriaInterruptException("тест", cause);
        assertSame(cause, ex.getCause());
    }
}
