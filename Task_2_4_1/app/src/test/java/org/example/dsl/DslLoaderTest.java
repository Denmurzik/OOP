package org.example.dsl;

import org.example.model.Config;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

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
}
