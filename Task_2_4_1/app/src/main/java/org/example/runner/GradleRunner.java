package org.example.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** Запускает gradle build/javadoc/test и парсит результаты тестов. */
public class GradleRunner {

    private final long timeoutSeconds;

    /** Создаёт раннер с общим таймаутом на команду. */
    public GradleRunner(long timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    /** Результат запуска gradle-команды. */
    public static class RunResult {
        public final boolean successful;
        public final boolean timedOut;
        public final int exitCode;
        public final String output;

        /** Создаёт результат запуска gradle-команды. */
        public RunResult(boolean successful, boolean timedOut, int exitCode, String output) {
            this.successful = successful;
            this.timedOut = timedOut;
            this.exitCode = exitCode;
            this.output = output;
        }
    }

    /** Итог тестов: статус запуска и количества passed/failed/skipped. */
    public static class TestRunResult extends RunResult {
        public int passed;
        public int failed;
        public int skipped;

        /** Создаёт результат запуска тестов с исходным статусом команды. */
        public TestRunResult(boolean successful, boolean timedOut, int exitCode, String output) {
            super(successful, timedOut, exitCode, output);
        }
    }

    /** Запускает сборку без тестов, возвращает true при успехе. */
    public RunResult build(File projectDir) {
        return runGradle(projectDir, "build", "-x", "test");
    }

    /** Запускает генерацию javadoc, возвращает true при успехе. */
    public RunResult javadoc(File projectDir) {
        return runGradle(projectDir, "javadoc");
    }

    /** Запускает тесты и возвращает статистику по XML-отчётам. */
    public TestRunResult test(File projectDir) {
        deleteTestResults(projectDir);
        TestRunResult result = runGradle(projectDir, "test", "--rerun-tasks");
        TestRunResult stats = new TestRunResult(
                result.successful, result.timedOut, result.exitCode, result.output);
        if (result.timedOut) {
            return stats;
        }
        for (File resultsDir : findTestResultsDirs(projectDir)) {
            File[] files = resultsDir.listFiles();
            if (files == null) {
                continue;
            }
            for (File xml : files) {
                if (xml.getName().startsWith("TEST-") && xml.getName().endsWith(".xml")) {
                    parseJunitXml(xml, stats);
                }
            }
        }
        return stats;
    }

    private List<File> findTestResultsDirs(File projectDir) {
        List<File> dirs = new ArrayList<>();
        collectTestResultDirs(projectDir, dirs);
        return dirs;
    }

    private void parseJunitXml(File xml, TestRunResult stats) {
        try {
            DocumentBuilder b = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = b.parse(xml);
            NodeList suites = doc.getElementsByTagName("testsuite");
            for (int i = 0; i < suites.getLength(); i++) {
                NamedNodeMap attrs = suites.item(i).getAttributes();
                int tests = intAttr(attrs, "tests");
                int failures = intAttr(attrs, "failures");
                int errors = intAttr(attrs, "errors");
                int skipped = intAttr(attrs, "skipped");
                stats.failed += failures + errors;
                stats.skipped += skipped;
                int passed = tests - failures - errors - skipped;
                if (passed < 0) {
                    passed = 0;
                }
                stats.passed += passed;
            }
        } catch (Exception ignore) {
            // битый XML — просто не увеличиваем счётчики
        }
    }

    private int intAttr(NamedNodeMap attrs, String name) {
        Node n = attrs.getNamedItem(name);
        if (n == null) {
            return 0;
        }
        try {
            return Integer.parseInt(n.getNodeValue().trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private TestRunResult runGradle(File projectDir, String... args) {
        try {
            boolean windows = System.getProperty("os.name", "").toLowerCase().contains("win");
            File wrapperBat = new File(projectDir, "gradlew.bat");
            File wrapperSh = new File(projectDir, "gradlew");

            List<String> full = new ArrayList<>();
            if (windows && wrapperBat.exists()) {
                full.add(wrapperBat.getAbsolutePath());
            } else if (!windows && wrapperSh.exists()) {
                full.add(wrapperSh.getAbsolutePath());
            } else {
                full.add(windows ? "gradle.bat" : "gradle");
            }
            full.add("--no-daemon");
            for (String arg : args) {
                full.add(arg);
            }

            String[] cmdArr = full.toArray(new String[0]);
            ProcessHelper.Result r = ProcessHelper.run(projectDir, timeoutSeconds, cmdArr);
            return new TestRunResult(
                    r.exitCode == 0 && !r.timedOut, r.timedOut, r.exitCode, r.output);
        } catch (Exception e) {
            return new TestRunResult(
                    false,
                    false,
                    -1,
                    e.getMessage() == null ? "" : e.getMessage());
        }
    }

    private void collectTestResultDirs(File dir, List<File> out) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (!file.isDirectory()) {
                continue;
            }
            if ("test".equals(file.getName())
                    && file.getParentFile() != null
                    && "test-results".equals(file.getParentFile().getName())
                    && file.isDirectory()) {
                out.add(file);
                continue;
            }
            collectTestResultDirs(file, out);
        }
    }

    private void deleteTestResults(File projectDir) {
        for (File dir : findTestResultsDirs(projectDir)) {
            deleteRecursively(dir);
        }
    }

    private void deleteRecursively(File file) {
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                deleteRecursively(child);
            }
        }
        if (file.exists() && !file.delete()) {
            throw new RunnerException("Не удалось удалить " + file.getAbsolutePath());
        }
    }
}
