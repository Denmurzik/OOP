package org.example.model;

import java.time.LocalDate;

public class Lab {
    private String id;
    private String name;
    private int maxScore;
    private LocalDate softDeadline;
    private LocalDate hardDeadline;

    public Lab() {}

    public Lab(String id, String name, int maxScore, LocalDate softDeadline, LocalDate hardDeadline) {
        this.id = id;
        this.name = name;
        this.maxScore = maxScore;
        this.softDeadline = softDeadline;
        this.hardDeadline = hardDeadline;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getMaxScore() { return maxScore; }
    public void setMaxScore(int maxScore) { this.maxScore = maxScore; }

    public LocalDate getSoftDeadline() { return softDeadline; }
    public void setSoftDeadline(LocalDate softDeadline) { this.softDeadline = softDeadline; }

    public LocalDate getHardDeadline() { return hardDeadline; }
    public void setHardDeadline(LocalDate hardDeadline) { this.hardDeadline = hardDeadline; }
}
