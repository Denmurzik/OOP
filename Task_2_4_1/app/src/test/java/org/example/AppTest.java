package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.Test;

class AppTest {

    @Test
    void noArgsPrintsUsage() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        int exit = App.run(new String[0], stream(out), stream(err));

        assertEquals(1, exit);
        assertTrue(text(err).contains("Использование: oop-checker <команда>"));
    }

    @Test
    void unknownCommandPrintsError() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        int exit = App.run(new String[]{"unknown"}, stream(out), stream(err));

        assertEquals(1, exit);
        assertTrue(text(err).contains("Неизвестная команда: unknown"));
    }

    @Test
    void commandRegistryContainsAliasesToSameCommand() {
        Map<String, AppCommand> commands = App.commandRegistry();

        assertTrue(commands.containsKey("test"));
        assertTrue(commands.containsKey("report"));
        assertSame(commands.get("test"), commands.get("report"));
    }

    @Test
    void reportCommandFailsClearlyWhenScriptMissing() {
        ReportCommand command = new ReportCommand(
                new org.example.dsl.DslLoader(),
                new org.example.report.HtmlReporter());
        try {
            command.run(
                    new java.io.File(System.getProperty("java.io.tmpdir"), "no-script-dir"),
                    System.out);
        } catch (Exception e) {
            assertEquals("В рабочей директории не найден oop-checker.groovy", e.getMessage());
            return;
        }
        throw new AssertionError("Expected missing script error");
    }

    private PrintStream stream(ByteArrayOutputStream out) {
        return new PrintStream(out, true, StandardCharsets.UTF_8);
    }

    private String text(ByteArrayOutputStream out) {
        return out.toString(StandardCharsets.UTF_8);
    }
}
