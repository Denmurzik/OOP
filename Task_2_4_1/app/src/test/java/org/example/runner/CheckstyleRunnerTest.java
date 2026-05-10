package org.example.runner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;

class CheckstyleRunnerTest {

    @Test
    void returnsZeroWhenNoSrc() throws Exception {
        File dir = Files.createTempDirectory("cs").toFile();
        dir.deleteOnExit();
        int v = new CheckstyleRunner().countViolations(dir);
        assertEquals(0, v);
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
        write(new File(src, "Bad.java"), "public class Bad{\npublic void x(){int    y=1;}\n}\n");
        int v = new CheckstyleRunner().countViolations(dir);
        assertTrue(v > 0);
    }

    @Test
    void aggregatesViolationsAcrossModules() throws Exception {
        File dir = Files.createTempDirectory("cs").toFile();
        File srcA = new File(dir, "moduleA/src/main/java");
        File srcB = new File(dir, "moduleB/src/main/java");
        srcA.mkdirs();
        srcB.mkdirs();
        write(new File(srcA, "BadA.java"), "public class BadA{int x;}\n");
        write(new File(srcB, "BadB.java"), "public class BadB{int y;}\n");

        int v = new CheckstyleRunner().countViolations(dir);

        assertTrue(v > 1);
    }

    private void write(File file, String content) throws Exception {
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(content);
        }
    }
}
