package org.example.runner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class ProcessHelperTest {

    @Test
    void runsSimpleCommand() throws Exception {
        boolean windows = System.getProperty("os.name", "").toLowerCase().contains("win");
        ProcessHelper.Result r;
        if (windows) {
            r = ProcessHelper.run(null, 10, "cmd", "/c", "echo", "hello");
        } else {
            r = ProcessHelper.run(null, 10, "echo", "hello");
        }
        assertEquals(0, r.exitCode);
        assertTrue(r.output.contains("hello"));
        assertEquals(false, r.timedOut);
    }

    @Test
    void resultFields() {
        ProcessHelper.Result r = new ProcessHelper.Result(7, "out", true);
        assertEquals(7, r.exitCode);
        assertEquals("out", r.output);
        assertTrue(r.timedOut);
        assertNotNull(new ProcessHelper());
    }

    @Test
    void requestFields() {
        ProcessHelper.Request request = new ProcessHelper.Request(null, 15, List.of("echo", "x"));
        assertEquals(15, request.timeoutSeconds);
        assertEquals(List.of("echo", "x"), request.command);
    }
}
