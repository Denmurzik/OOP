package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.example.model.Assignment;
import org.example.model.Checkpoint;
import org.example.model.Config;
import org.example.model.Group;
import org.example.model.Lab;
import org.example.model.LabResult;
import org.example.model.Student;
import org.example.model.StudentReport;
import org.example.runner.ProcessHelper;
import org.junit.jupiter.api.Test;

class CheckerTest {

    @Test
    void runProducesReportsPerGroupWhenRepoUnavailable() throws Exception {
        Config cfg = new Config();
        Lab lab = new Lab("2-1-1", "l", 1,
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 12, 30));
        cfg.getLabs().add(lab);
        Group g = new Group("G");
        g.getStudents().add(new Student("n", "ФИО", "file:///no-repo"));
        cfg.getGroups().add(g);
        cfg.getAssignments().add(new Assignment("n", List.of("2-1-1")));
        cfg.getCheckpoints().add(new Checkpoint("КТ", LocalDate.of(2026, 5, 1)));
        cfg.getSettings().getGradeMinPercent().put(5, 90);
        cfg.getSettings().getGradeMinPercent().put(2, 0);
        cfg.getSettings().setTestTimeoutSeconds(5);

        File work = Files.createTempDirectory("checker").toFile();
        Map<Group, List<StudentReport>> data = new Checker(cfg, work).run();

        assertNotNull(data);
        assertEquals(1, data.size());
        List<StudentReport> reports = data.values().iterator().next();
        assertEquals(1, reports.size());
        StudentReport report = reports.get(0);
        LabResult result = report.getResults().get(0);
        assertEquals(0, report.totalScore());
        assertEquals("REPO_UNAVAILABLE", result.getStatus());
        assertNotNull(report.getCheckpointGrades().get("КТ"));
    }

    @Test
    void runComputesSubmissionDateAndCheckpointFromRelevantCommits() throws Exception {
        LocalDate submissionDate = LocalDate.now().minusDays(5);
        LocalDate unrelatedDate = LocalDate.now().minusDays(1);
        File remote = createRemoteRepo(submissionDate, unrelatedDate);

        Config cfg = baseConfig(
                remote.getAbsolutePath(),
                10,
                submissionDate.plusDays(1),
                submissionDate.plusDays(20));
        cfg.getCheckpoints().add(new Checkpoint("КТ", submissionDate.plusDays(2)));

        File work = Files.createTempDirectory("checker").toFile();
        Map<Group, List<StudentReport>> data = new Checker(cfg, work).run();

        StudentReport report = data.values().iterator().next().get(0);
        LabResult result = report.getResults().get(0);
        assertEquals("OK", result.getStatus());
        assertEquals(submissionDate, result.getSubmissionDate());
        assertEquals(10, result.getTotalScore());
        assertEquals("5", report.getCheckpointGrades().get("КТ"));
    }

    @Test
    void runAppliesActivityPenaltyByOne() throws Exception {
        LocalDate submissionDate = LocalDate.now().minusWeeks(9);
        File remote = createRemoteRepo(submissionDate, null);

        Config cfg = baseConfig(
                remote.getAbsolutePath(),
                10,
                submissionDate.plusWeeks(1),
                submissionDate.plusWeeks(3));
        cfg.getSettings().getGradeMinPercent().put(4, 70);
        cfg.getSettings().getGradeMinPercent().put(3, 50);
        cfg.getSettings().setActivityThreshold(0.95);

        File work = Files.createTempDirectory("checker").toFile();
        Map<Group, List<StudentReport>> data = new Checker(cfg, work).run();

        StudentReport report = data.values().iterator().next().get(0);
        assertTrue(report.getActivity() < 0.95);
        assertEquals("4", report.getGrade());
    }

    @Test
    void runSkipsUnknownStudent() throws Exception {
        Config cfg = new Config();
        cfg.getLabs().add(new Lab("1", "l", 1, null, null));
        cfg.getAssignments().add(new Assignment("ghost", List.of("1")));
        File work = Files.createTempDirectory("checker").toFile();
        Map<Group, List<StudentReport>> data = new Checker(cfg, work).run();
        assertNotNull(data);
    }

    private Config baseConfig(
            String repoUrl,
            int maxScore,
            LocalDate softDeadline,
            LocalDate hardDeadline) {
        Config cfg = new Config();
        cfg.getLabs().add(new Lab("2-1-1", "Лаба", maxScore, softDeadline, hardDeadline));
        Group g = new Group("G");
        g.getStudents().add(new Student("n", "ФИО", repoUrl));
        cfg.getGroups().add(g);
        cfg.getAssignments().add(new Assignment("n", List.of("2-1-1")));
        cfg.getSettings().getGradeMinPercent().put(5, 90);
        cfg.getSettings().getGradeMinPercent().put(2, 0);
        cfg.getSettings().setTestTimeoutSeconds(5);
        return cfg;
    }

    private File createRemoteRepo(
            LocalDate labCommitDate,
            LocalDate unrelatedCommitDate) throws Exception {
        File remote = Files.createTempDirectory("checker-remote").toFile();
        ProcessHelper.run(remote, 30, "git", "init", "--bare", "--initial-branch=main");

        File source = Files.createTempDirectory("checker-source").toFile();
        ProcessHelper.run(source, 30, "git", "init", "-b", "main");
        ProcessHelper.run(source, 30, "git", "config", "user.email", "t@t.t");
        ProcessHelper.run(source, 30, "git", "config", "user.name", "T");

        File labDir = new File(source, "task_2_1_1");
        labDir.mkdirs();
        write(new File(labDir, "build.gradle"), "plugins {}\n");
        write(new File(labDir, "src/main/java/Good.java"),
                "class Good {\n    int sum(int a, int b) {\n        return a + b;\n    }\n}\n");
        createFakeWrapper(labDir);
        ProcessHelper.run(source, 30, "git", "add", ".");
        commitWithDate(source, labCommitDate, "lab");

        if (unrelatedCommitDate != null) {
            write(new File(source, "README.md"), "later\n");
            ProcessHelper.run(source, 30, "git", "add", "README.md");
            commitWithDate(source, unrelatedCommitDate, "readme");
        }

        ProcessHelper.run(source, 30, "git", "remote", "add", "origin", remote.getAbsolutePath());
        ProcessHelper.run(source, 30, "git", "push", "-u", "origin", "main");
        return remote;
    }

    private void createFakeWrapper(File projectDir) throws Exception {
        boolean windows = System.getProperty("os.name", "").toLowerCase().contains("win");
        if (windows) {
            File wrapper = new File(projectDir, "gradlew.bat");
            try (FileWriter fw = new FileWriter(wrapper)) {
                fw.write("@echo off\r\n");
                fw.write("set args=%*\r\n");
                fw.write("echo %args% | findstr /C:\"test\" >nul\r\n");
                fw.write("if not errorlevel 1 (\r\n");
                fw.write("  mkdir build\\test-results\\test 2>nul\r\n");
                fw.write(
                        "  > build\\test-results\\test\\TEST-ok.xml echo "
                                + "^<testsuite tests=\"2\" failures=\"0\" "
                                + "errors=\"0\" skipped=\"0\"/^>\r\n");
                fw.write(")\r\n");
                fw.write("exit /b 0\r\n");
            }
        } else {
            File wrapper = new File(projectDir, "gradlew");
            try (FileWriter fw = new FileWriter(wrapper)) {
                fw.write("#!/usr/bin/env sh\n");
                fw.write("args=\"$*\"\n");
                fw.write("case \"$args\" in *\"test\"*)\n");
                fw.write("mkdir -p build/test-results/test\n");
                fw.write(
                        "printf '%s\\n' '<testsuite tests=\"2\" failures=\"0\" "
                                + "errors=\"0\" skipped=\"0\"/>' > "
                                + "build/test-results/test/TEST-ok.xml\n");
                fw.write(";; esac\n");
                fw.write("exit 0\n");
            }
            wrapper.setExecutable(true);
        }
    }

    private void commitWithDate(File dir, LocalDate date, String message) throws Exception {
        String isoDate = date + "T12:00:00+07:00";
        boolean windows = System.getProperty("os.name", "").toLowerCase().contains("win");
        if (windows) {
            ProcessHelper.run(dir, 30,
                    "cmd", "/c",
                    "set GIT_AUTHOR_DATE=" + isoDate
                            + "&& set GIT_COMMITTER_DATE=" + isoDate
                            + "&& git commit -m " + message);
        } else {
            ProcessHelper.run(dir, 30,
                    "sh", "-c",
                    "GIT_AUTHOR_DATE='" + isoDate + "' "
                            + "GIT_COMMITTER_DATE='" + isoDate + "' "
                            + "git commit -m '" + message + "'");
        }
    }

    private void write(File file, String content) throws Exception {
        file.getParentFile().mkdirs();
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(content);
        }
    }
}
