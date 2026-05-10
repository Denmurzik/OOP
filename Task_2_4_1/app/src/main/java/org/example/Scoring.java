package org.example;

import java.time.LocalDate;
import java.util.Map;
import org.example.model.Config;
import org.example.model.Lab;
import org.example.model.LabResult;
import org.example.model.Settings;
import org.example.model.StudentReport;

/** Подсчёт баллов и оценок. */
public class Scoring {

    /** Балл за задачу с учётом дедлайнов. */
    public static int scoreFor(boolean allGreen, Lab lab, LocalDate submissionDate) {
        if (!allGreen) {
            return 0;
        }
        int score = lab.getMaxScore();
        LocalDate soft = lab.getSoftDeadline();
        LocalDate hard = lab.getHardDeadline();
        if (hard != null && submissionDate != null && submissionDate.isAfter(hard)) {
            return 0;
        }
        if (soft != null && submissionDate != null && submissionDate.isAfter(soft)) {
            score = score / 2;
            if (score < 0) {
                score = 0;
            }
        }
        return score;
    }

    /** Оценка на КТ: учитываются лабы со soft-дедлайном до её даты. */
    public static String gradeOnCheckpoint(StudentReport report, LocalDate ctDate, Config config) {
        int got = 0;
        int max = 0;
        for (Lab lab : config.getLabs()) {
            if (lab.getSoftDeadline() == null) {
                continue;
            }
            if (lab.getSoftDeadline().isAfter(ctDate)) {
                continue;
            }
            max += lab.getMaxScore();
            LabResult r = report.findResult(lab.getId());
            if (r != null
                    && r.getSubmissionDate() != null
                    && !r.getSubmissionDate().isAfter(ctDate)) {
                got += r.getTotalScore();
            }
        }
        return gradeByPercent(got, max, config);
    }

    /** Оценка по общей сумме баллов. */
    public static String grade(int totalScore, Config config) {
        int max = 0;
        for (Lab l : config.getLabs()) {
            max += l.getMaxScore();
        }
        return gradeByPercent(totalScore, max, config);
    }

    private static String gradeByPercent(int got, int max, Config config) {
        Settings s = config.getSettings();
        if (max <= 0 || s.getGradeMinPercent().isEmpty()) {
            return "-";
        }
        int percent = (int) Math.round(got * 100.0 / max);
        String best = "-";
        int bestMin = -1;
        for (Map.Entry<Integer, Integer> e : s.getGradeMinPercent().entrySet()) {
            if (percent >= e.getValue() && e.getValue() > bestMin) {
                bestMin = e.getValue();
                best = String.valueOf(e.getKey());
            }
        }
        return best;
    }
}
