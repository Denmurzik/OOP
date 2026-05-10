package org.example.runner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;

class GradleRunnerTest {

    @Test
    void buildFailsOnEmptyDir() throws Exception {
        File dir = Files.createTempDirectory("gr").toFile();
        GradleRunner gr = new GradleRunner(10);
        assertFalse(gr.build(dir).successful);
        assertFalse(gr.javadoc(dir).successful);
    }

    @Test
    void testParsesXmlReportEvenWhenGradleExitsWithFailure() throws Exception {
        File dir = Files.createTempDirectory("gr").toFile();
        createFakeGradleWrapper(dir,
                "build", 0, null,
                "javadoc", 0, null,
                "test", 1,
                "<testsuite tests=\"5\" failures=\"1\" errors=\"1\" skipped=\"1\"/>");

        GradleRunner.TestRunResult s = new GradleRunner(10).test(dir);

        assertNotNull(s);
        assertFalse(s.successful);
        assertEquals(2, s.failed);
        assertEquals(1, s.skipped);
        assertEquals(2, s.passed);
    }

    @Test
    void testIgnoresStaleResultsAndAggregatesModules() throws Exception {
        File dir = Files.createTempDirectory("gr").toFile();
        File stale = new File(dir, "build/test-results/test");
        stale.mkdirs();
        try (FileWriter fw = new FileWriter(new File(stale, "TEST-stale.xml"))) {
            fw.write("<testsuite tests=\"9\" failures=\"9\" errors=\"0\" skipped=\"0\"/>");
        }

        createFakeGradleWrapper(dir,
                "build", 0, null,
                "javadoc", 0, null,
                "test", 0,
                """
                <testsuite tests="3" failures="1" errors="0" skipped="0"/>
                ---
                <testsuite tests="4" failures="0" errors="0" skipped="1"/>
                """);

        GradleRunner.TestRunResult s = new GradleRunner(10).test(dir);

        assertTrue(s.successful);
        assertEquals(1, s.failed);
        assertEquals(1, s.skipped);
        assertEquals(5, s.passed);
    }

    private void createFakeGradleWrapper(
            File projectDir,
            String buildCommand, int buildExit, String buildXml,
            String javadocCommand, int javadocExit, String javadocXml,
            String testCommand, int testExit, String testXml) throws Exception {
        boolean windows = System.getProperty("os.name", "").toLowerCase().contains("win");
        if (windows) {
            File wrapper = new File(projectDir, "gradlew.bat");
            try (FileWriter fw = new FileWriter(wrapper)) {
                fw.write("@echo off\r\n");
                fw.write("set args=%*\r\n");
                writeWindowsCommand(fw, buildCommand, buildExit, buildXml);
                writeWindowsCommand(fw, javadocCommand, javadocExit, javadocXml);
                writeWindowsCommand(fw, testCommand, testExit, testXml);
                fw.write("exit /b 0\r\n");
            }
        } else {
            File wrapper = new File(projectDir, "gradlew");
            try (FileWriter fw = new FileWriter(wrapper)) {
                fw.write("#!/usr/bin/env sh\n");
                fw.write("args=\"$*\"\n");
                writeUnixCommand(fw, buildCommand, buildExit, buildXml);
                writeUnixCommand(fw, javadocCommand, javadocExit, javadocXml);
                writeUnixCommand(fw, testCommand, testExit, testXml);
                fw.write("exit 0\n");
            }
            wrapper.setExecutable(true);
        }
    }

    private void writeWindowsCommand(FileWriter fw, String command, int exitCode, String xml)
            throws Exception {
        fw.write("echo %args% | findstr /C:\"" + command + "\" >nul\r\n");
        fw.write("if not errorlevel 1 (\r\n");
        if (xml != null) {
            fw.write("  mkdir build\\test-results\\test 2>nul\r\n");
            String[] parts = xml.split("---\\R?");
            for (int i = 0; i < parts.length; i++) {
                String value = escapeBatch(parts[i].trim());
                fw.write("  if " + i + "==0 (\r\n");
                fw.write(
                        "    > build\\test-results\\test\\TEST-"
                                + i + ".xml echo " + value + "\r\n");
                fw.write("  ) else (\r\n");
                fw.write("    mkdir module" + i + "\\build\\test-results\\test 2>nul\r\n");
                fw.write(
                        "    > module" + i + "\\build\\test-results\\test\\TEST-"
                                + i + ".xml echo " + value + "\r\n");
                fw.write("  )\r\n");
            }
        }
        fw.write("  exit /b " + exitCode + "\r\n");
        fw.write(")\r\n");
    }

    private void writeUnixCommand(FileWriter fw, String command, int exitCode, String xml)
            throws Exception {
        fw.write("case \"$args\" in *\"" + command + "\"*)\n");
        if (xml != null) {
            fw.write("mkdir -p build/test-results/test\n");
            String[] parts = xml.split("---\\R?");
            for (int i = 0; i < parts.length; i++) {
                String value = parts[i].trim().replace("\"", "\\\"");
                if (i == 0) {
                    fw.write(
                            "printf '%s\\n' \"" + value
                                    + "\" > build/test-results/test/TEST-" + i + ".xml\n");
                } else {
                    fw.write("mkdir -p module" + i + "/build/test-results/test\n");
                    fw.write(
                            "printf '%s\\n' \"" + value + "\" > module"
                                    + i + "/build/test-results/test/TEST-" + i + ".xml\n");
                }
            }
        }
        fw.write("exit " + exitCode + "\n");
        fw.write(";; esac\n");
    }

    private String escapeBatch(String value) {
        return value
                .replace("^", "^^")
                .replace("<", "^<")
                .replace(">", "^>")
                .replace("&", "^&");
    }
}
