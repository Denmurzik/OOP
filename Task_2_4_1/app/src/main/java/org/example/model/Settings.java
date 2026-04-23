package org.example.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Settings {
    // Диапазоны баллов в процентах: оценка -> минимальный процент
    private Map<Integer, Integer> gradeMinPercent = new LinkedHashMap<>();
    private int testTimeoutSeconds = 60;
    // Ключ "nick::labId" -> доп.балл
    private Map<String, Integer> bonuses = new HashMap<>();
    // Минимальная доля активных недель, чтобы не снижать оценку
    private double activityThreshold = 0.5;

    public Map<Integer, Integer> getGradeMinPercent() { return gradeMinPercent; }
    public void setGradeMinPercent(Map<Integer, Integer> gradeMinPercent) { this.gradeMinPercent = gradeMinPercent; }

    public int getTestTimeoutSeconds() { return testTimeoutSeconds; }
    public void setTestTimeoutSeconds(int testTimeoutSeconds) { this.testTimeoutSeconds = testTimeoutSeconds; }

    public Map<String, Integer> getBonuses() { return bonuses; }
    public void setBonuses(Map<String, Integer> bonuses) { this.bonuses = bonuses; }

    public double getActivityThreshold() { return activityThreshold; }
    public void setActivityThreshold(double activityThreshold) { this.activityThreshold = activityThreshold; }

    public int getBonusFor(String nick, String labId) {
        return bonuses.getOrDefault(nick + "::" + labId, 0);
    }
}
