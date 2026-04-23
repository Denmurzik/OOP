package org.example.runner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;

class CheckstyleRunnerTest {

    @Test
    void returnsMinusOneWhenNoSrc() throws Exception {
        File dir = Files.createTempDirectory("cs").toFile();
        dir.deleteOnExit();
        int v = new CheckstyleRunner().countViolations(dir);
        assertEquals(-1, v);
    }

    @Test
    void returnsZeroWhenNoJavaFiles() throws Exception {
        File dir = Files.createTempDirectory("cs").toFile();
        File src = new File(dir, "src/main/java");
        src.mkdirs();
        int v = new CheckstyleRunner().countViolations(dir);
        assertEquals(0, v);
    }

    @Test
    void countsViolationsOnBadJava() throws Exception {
        File dir = Files.createTempDirectory("cs").toFile();
        File src = new File(dir, "src/main/java");
        src.mkdirs();
        File bad = new File(src, "Bad.java");
        FileWriter fw = new FileWriter(bad);
        fw.write("public class Bad{\npublic void x(){int    y=1;}\n}\n");
        fw.close();
        int v = new CheckstyleRunner().countViolations(dir);
        // Google Java Style точно ругнётся на такой файл
        assertTrue(v > 0);
    }
}
