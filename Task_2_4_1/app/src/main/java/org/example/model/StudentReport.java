package org.example.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StudentReport {
    private Student student;
    private List<LabResult> results = new ArrayList<>();
    private double activity; // 0.0..1.0
    private String grade = "-";
    // Оценка на каждую контрольную точку: имя КТ -> оценка
    private Map<String, String> checkpointGrades = new LinkedHashMap<>();

    public StudentReport(Student student) {
        this.student = student;
    }

    public Student getStudent() { return student; }

    public List<LabResult> getResults() { return results; }

    public double getActivity() { return activity; }
    public void setActivity(double activity) { this.activity = activity; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public Map<String, String> getCheckpointGrades() { return checkpointGrades; }

    public int totalScore() {
        int sum = 0;
        for (LabResult r : results) sum += r.getTotalScore();
        return sum;
    }

    public LabResult findResult(String labId) {
        for (LabResult r : results) {
            if (r.getLab().getId().equals(labId)) return r;
        }
        return null;
    }
}
