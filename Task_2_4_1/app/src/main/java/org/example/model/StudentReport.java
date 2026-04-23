package org.example.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Итоговый отчёт по одному студенту: его лабы, активность и оценка. */
public class StudentReport {
    private Student student;
    private List<LabResult> results = new ArrayList<>();
    private double activity;
    private String grade = "-";
    private Map<String, String> checkpointGrades = new LinkedHashMap<>();

    /** Создаёт пустой отчёт для студента. */
    public StudentReport(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public List<LabResult> getResults() {
        return results;
    }

    public double getActivity() {
        return activity;
    }

    public void setActivity(double activity) {
        this.activity = activity;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Map<String, String> getCheckpointGrades() {
        return checkpointGrades;
    }

    /** Сумма баллов по всем лабам студента. */
    public int totalScore() {
        int sum = 0;
        for (LabResult r : results) {
            sum += r.getTotalScore();
        }
        return sum;
    }

    /** Ищет результат по id лабы, возвращает null если не найден. */
    public LabResult findResult(String labId) {
        for (LabResult r : results) {
            if (r.getLab().getId().equals(labId)) {
                return r;
            }
        }
        return null;
    }
}
