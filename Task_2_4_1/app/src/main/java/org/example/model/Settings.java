package org.example.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/** Настройки системы: пороги оценок, таймауты, бонусы, порог активности. */
public class Settings {
    private Map<Integer, Integer> gradeMinPercent = new LinkedHashMap<>();
    private int testTimeoutSeconds = 60;
    private Map<String, Integer> bonuses = new HashMap<>();
    private double activityThreshold = 0.5;

    public Map<Integer, Integer> getGradeMinPercent() {
        return gradeMinPercent;
    }

    public void setGradeMinPercent(Map<Integer, Integer> gradeMinPercent) {
        this.gradeMinPercent = gradeMinPercent;
    }

    public int getTestTimeoutSeconds() {
        return testTimeoutSeconds;
    }

    public void setTestTimeoutSeconds(int testTimeoutSeconds) {
        this.testTimeoutSeconds = testTimeoutSeconds;
    }

    public Map<String, Integer> getBonuses() {
        return bonuses;
    }

    public void setBonuses(Map<String, Integer> bonuses) {
        this.bonuses = bonuses;
    }

    public double getActivityThreshold() {
        return activityThreshold;
    }

    public void setActivityThreshold(double activityThreshold) {
        this.activityThreshold = activityThreshold;
    }

    /** Доп. балл студенту за задачу, или 0 если не назначен. */
    public int getBonusFor(String nick, String labId) {
        return bonuses.getOrDefault(nick + "::" + labId, 0);
    }
}
