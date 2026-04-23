package org.example.runner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;

class GradleRunnerTest {

    @Test
    void testStatsDefaults() {
        GradleRunner.TestStats s = new GradleRunner.TestStats();
        assertEquals(0, s.passed);
        assertEquals(0, s.failed);
        assertEquals(0, s.skipped);
    }

    @Test
    void buildFailsOnEmptyDir() throws Exception {
        File dir = Files.createTempDirectory("gr").toFile();
        GradleRunner gr = new GradleRunner(10);
        assertEquals(false, gr.build(dir));
        assertEquals(false, gr.javadoc(dir));
    }

    @Test
    void testParsesXmlReport() throws Exception {
        File dir = Files.createTempDirectory("gr").toFile();
        File results = new File(dir, "build/test-results/test");
        results.mkdirs();
        File xml = new File(results, "TEST-a.xml");
        FileWriter fw = new FileWriter(xml);
        fw.write("<testsuite tests=\"5\" failures=\"1\" errors=\"1\" skipped=\"1\"/>");
        fw.close();

        GradleRunner.TestStats s = new GradleRunner(10).test(dir);
        assertNotNull(s);
        assertEquals(2, s.failed);
        assertEquals(1, s.skipped);
        assertEquals(2, s.passed);
    }
}
