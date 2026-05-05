package org.example;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.example.model.Assignment;
import org.example.model.Checkpoint;
import org.example.model.Config;
import org.example.model.Group;
import org.example.model.Lab;
import org.example.model.LabResult;
import org.example.model.Student;
import org.example.model.StudentReport;
import org.example.runner.CheckstyleRunner;
import org.example.runner.GitClient;
import org.example.runner.GradleRunner;

/** Основная оркестрация: прогоняет проверки по всем студентам из конфига. */
public class Checker {

    private final Config config;
    private final File workDir;
    private final GitClient git;
    private final GradleRunner gradle;
    private final CheckstyleRunner checkstyle;

    /** Создаёт проверку на основе конфига. */
    public Checker(Config config, File workDir) {
        this.config = config;
        this.workDir = workDir;
        long timeout = config.getSettings().getTestTimeoutSeconds() * 4L;
        if (timeout < 30) {
            timeout = 30;
        }
        this.git = new GitClient(timeout);
        long gradleTimeout = config.getSettings().getTestTimeoutSeconds() * 2L;
        if (gradleTimeout < 60) {
            gradleTimeout = 60;
        }
        this.gradle = new GradleRunner(gradleTimeout);
        this.checkstyle = new CheckstyleRunner();
    }

    /** Запускает проверку и возвращает отчёты, сгруппированные по группам. */
    public Map<Group, List<StudentReport>> run() {
        Map<Group, List<StudentReport>> byGroup = new LinkedHashMap<>();
        for (Group g : config.getGroups()) {
            byGroup.put(g, new ArrayList<StudentReport>());
        }

        File clonesDir = new File(workDir, ".oop-checker-clones");
        clonesDir.mkdirs();

        for (Assignment a : config.getAssignments()) {
            Student student = config.findStudent(a.getNick());
            if (student == null) {
                continue;
            }
            StudentReport report = new StudentReport(student);
            File repoDir = new File(clonesDir, student.getNick());
            boolean cloned = git.cloneOrUpdate(student.getRepo(), repoDir);

            for (String labId : a.getLabIds()) {
                Lab lab = config.findLab(labId);
                if (lab == null) {
                    continue;
                }
                LabResult r = new LabResult(student, lab);
                report.getResults().add(r);

                if (!cloned) {
                    r.setTotalScore(0);
                    continue;
                }

                File labDir = findLabDir(repoDir, labId);
                if (labDir == null) {
                    int bonus = config.getSettings().getBonusFor(student.getNick(), labId);
                    r.setBonus(bonus);
                    r.setTotalScore(bonus);
                    continue;
                }

                r.setBuildOk(gradle.build(labDir));
                if (r.isBuildOk()) {
                    r.setDocOk(gradle.javadoc(labDir));
                    int v = checkstyle.countViolations(labDir);
                    r.setStyleOk(v == 0);
                }
                if (r.isBuildOk() && r.isDocOk() && r.isStyleOk()) {
                    GradleRunner.TestStats s = gradle.test(labDir);
                    r.setTestsPassed(s.passed);
                    r.setTestsFailed(s.failed);
                    r.setTestsSkipped(s.skipped);
                }

                boolean allGreen = r.isBuildOk() && r.isDocOk() && r.isStyleOk()
                        && r.getTestsFailed() == 0 && r.getTestsSkipped() == 0
                        && r.getTestsPassed() > 0;
                int score = Scoring.scoreFor(allGreen, lab, LocalDate.now());
                int bonus = config.getSettings().getBonusFor(student.getNick(), labId);
                r.setBonus(bonus);
                r.setTotalScore(score + bonus);
            }

            if (cloned) {
                report.setActivity(computeActivity(repoDir));
            }

            int total = report.totalScore();
            String grade = Scoring.grade(total, config);
            if (!"-".equals(grade)
                    && report.getActivity() < config.getSettings().getActivityThreshold()) {
                try {
                    int g = Integer.parseInt(grade);
                    int lower = g - 1;
                    if (lower < 2) {
                        lower = 2;
                    }
                    grade = String.valueOf(lower);
                } catch (NumberFormatException ignore) {
                    // оставляем как есть
                }
            }
            report.setGrade(grade);

            for (Checkpoint cp : config.getCheckpoints()) {
                String cpGrade = Scoring.gradeOnCheckpoint(report, cp.getDate(), config);
                report.getCheckpointGrades().put(cp.getName(), cpGrade);
            }

            Group group = config.findGroupOf(a.getNick());
            if (group != null) {
                byGroup.get(group).add(report);
            }
        }
        return byGroup;
    }

    private File findLabDir(File repoDir, String labId) {
        String folder = "task_" + labId.replace('-', '_');
        File[] files = repoDir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory() && f.getName().equalsIgnoreCase(folder)) {
                    return f;
                }
            }
        }
        if (new File(repoDir, "build.gradle").exists()
                || new File(repoDir, "build.gradle.kts").exists()) {
            return repoDir;
        }
        return null;
    }

    private double computeActivity(File repoDir) {
        LocalDate from = firstLabStart();
        LocalDate to = LocalDate.now();
        if (from == null || !from.isBefore(to)) {
            return 0.0;
        }
        List<LocalDate> dates = git.commitDates(repoDir, from, to);
        if (dates.isEmpty()) {
            return 0.0;
        }
        Set<Long> activeWeeks = new HashSet<>();
        for (LocalDate date : dates) {
            long days = date.toEpochDay() - from.toEpochDay();
            activeWeeks.add(days / 7);
        }
        long totalWeeks = (to.toEpochDay() - from.toEpochDay()) / 7 + 1;
        if (totalWeeks < 1) {
            totalWeeks = 1;
        }
        double ratio = (double) activeWeeks.size() / totalWeeks;
        if (ratio > 1.0) {
            ratio = 1.0;
        }
        return ratio;
    }

    private LocalDate firstLabStart() {
        LocalDate min = null;
        for (Lab l : config.getLabs()) {
            LocalDate d = l.getSoftDeadline();
            if (d != null && (min == null || d.isBefore(min))) {
                min = d;
            }
        }
        if (min != null) {
            return min.minusWeeks(2);
        }
        return LocalDate.now().minusWeeks(10);
    }
}
