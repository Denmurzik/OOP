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

    /** Итог тестов: сколько прошло, упало, пропущено. */
    public static class TestStats {
        public int passed;
        public int failed;
        public int skipped;
    }

    /** Запускает сборку без тестов, возвращает true при успехе. */
    public boolean build(File projectDir) {
        return runGradle(projectDir, "build", "-x", "test") == 0;
    }

    /** Запускает генерацию javadoc, возвращает true при успехе. */
    public boolean javadoc(File projectDir) {
        return runGradle(projectDir, "javadoc") == 0;
    }

    /** Запускает тесты и возвращает статистику по XML-отчётам. */
    public TestStats test(File projectDir) {
        TestStats stats = new TestStats();
        runGradle(projectDir, "test");
        File resultsDir = findTestResultsDir(projectDir);
        if (resultsDir != null && resultsDir.isDirectory()) {
            File[] files = resultsDir.listFiles();
            if (files != null) {
                for (File xml : files) {
                    if (xml.getName().startsWith("TEST-") && xml.getName().endsWith(".xml")) {
                        parseJunitXml(xml, stats);
                    }
                }
            }
        }
        return stats;
    }

    private File findTestResultsDir(File projectDir) {
        File a = new File(projectDir, "build/test-results/test");
        if (a.isDirectory()) {
            return a;
        }
        File[] modules = projectDir.listFiles();
        if (modules != null) {
            for (File m : modules) {
                if (!m.isDirectory()) {
                    continue;
                }
                File b = new File(m, "build/test-results/test");
                if (b.isDirectory()) {
                    return b;
                }
            }
        }
        return null;
    }

    private void parseJunitXml(File xml, TestStats stats) {
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

    private int runGradle(File projectDir, String... args) {
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
            if (r.timedOut) {
                return -1;
            }
            return r.exitCode;
        } catch (Exception e) {
            return -1;
        }
    }
}
