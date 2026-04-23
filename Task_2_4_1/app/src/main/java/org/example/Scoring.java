package org.example;

import org.example.model.Config;
import org.example.model.Lab;
import org.example.model.LabResult;
import org.example.model.Settings;
import org.example.model.StudentReport;

import java.time.LocalDate;
import java.util.Map;

public class Scoring {

    /** Балл за задачу с учётом дедлайнов. */
    public static int scoreFor(boolean allGreen, Lab lab, LocalDate submissionDate) {
        if (!allGreen) return 0;
        int score = lab.getMaxScore();
        LocalDate soft = lab.getSoftDeadline();
        LocalDate hard = lab.getHardDeadline();
        if (hard != null && submissionDate != null && submissionDate.isAfter(hard)) return 0;
        if (soft != null && submissionDate != null && submissionDate.isAfter(soft)) score = Math.max(0, score / 2);
        return score;
    }

    /** Оценка на контрольную точку: учитываются только лабы со soft-дедлайном до даты КТ. */
    public static String gradeOnCheckpoint(StudentReport report, LocalDate ctDate, Config config) {
        int got = 0;
        int max = 0;
        for (Lab lab : config.getLabs()) {
            if (lab.getSoftDeadline() == null) continue;
            if (lab.getSoftDeadline().isAfter(ctDate)) continue;
            max += lab.getMaxScore();
            LabResult r = report.findResult(lab.getId());
            if (r != null) got += r.getTotalScore();
        }
        return gradeByPercent(got, max, config);
    }

    /** Оценка по сумме баллов и таблице порогов из настроек. */
    public static String grade(int totalScore, Config config) {
        int max = 0;
        for (Lab l : config.getLabs()) max += l.getMaxScore();
        return gradeByPercent(totalScore, max, config);
    }

    private static String gradeByPercent(int got, int max, Config config) {
        Settings s = config.getSettings();
        if (max <= 0 || s.getGradeMinPercent().isEmpty()) return "-";
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
