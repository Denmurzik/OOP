package org.example.dsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.example.model.Config;
import org.junit.jupiter.api.Test;

class DslLoaderTest {

    @Test
    void loadsConfigFromGroovyDsl() throws Exception {
        File tmp = File.createTempFile("simple", ".groovy");
        tmp.deleteOnExit();
        try (InputStream in = getClass().getResourceAsStream("/simple.groovy")) {
            assertNotNull(in, "simple.groovy должен быть в ресурсах");
            Files.copy(in, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        Config cfg = new DslLoader().load(tmp);

        assertEquals(1, cfg.getLabs().size());
        assertEquals("Простые числа", cfg.getLabs().get(0).getName());
        assertEquals(1, cfg.getGroups().size());
        assertEquals(2, cfg.getGroups().get(0).getStudents().size());
        assertEquals(2, cfg.getAssignments().size());
        assertEquals(1, cfg.getCheckpoints().size());
        assertEquals(90, cfg.getSettings().getGradeMinPercent().get(5));
        assertEquals(1, cfg.getSettings().getBonusFor("stud2", "2-1-1"));
    }

    @Test
    void throwsWhenFileMissing() {
        assertThrows(IllegalArgumentException.class,
                () -> new DslLoader().load(new File("no-such-file.groovy")));
    }

    @Test
    void importConfigLoadsOtherFile() throws Exception {
        File dir = Files.createTempDirectory("dsl").toFile();
        File tasks = new File(dir, "tasks.groovy");
        try (FileWriter fw = new FileWriter(tasks)) {
            fw.write("tasks { task id: '1', name: 'a', maxScore: 1,"
                    + " softDeadline: '2026-01-01', hardDeadline: '2026-06-01' }\n");
        }
        File main = new File(dir, "main.groovy");
        try (FileWriter fw = new FileWriter(main)) {
            fw.write("importConfig 'tasks.groovy'\n");
        }
        Config cfg = new DslLoader().load(main);
        assertEquals(1, cfg.getLabs().size());
        assertEquals("a", cfg.getLabs().get(0).getName());
    }
}
