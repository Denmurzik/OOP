package org.example.model;

/** Результат проверки одной лабораторной у одного студента. */
public class LabResult {
    private Student student;
    private Lab lab;
    private boolean buildOk;
    private boolean docOk;
    private boolean styleOk;
    private int testsPassed;
    private int testsFailed;
    private int testsSkipped;
    private int bonus;
    private int totalScore;

    /** Создаёт пустой результат для пары "студент и лаба". */
    public LabResult(Student student, Lab lab) {
        this.student = student;
        this.lab = lab;
    }

    public Student getStudent() {
        return student;
    }

    public Lab getLab() {
        return lab;
    }

    public boolean isBuildOk() {
        return buildOk;
    }

    public void setBuildOk(boolean buildOk) {
        this.buildOk = buildOk;
    }

    public boolean isDocOk() {
        return docOk;
    }

    public void setDocOk(boolean docOk) {
        this.docOk = docOk;
    }

    public boolean isStyleOk() {
        return styleOk;
    }

    public void setStyleOk(boolean styleOk) {
        this.styleOk = styleOk;
    }

    public int getTestsPassed() {
        return testsPassed;
    }

    public void setTestsPassed(int testsPassed) {
        this.testsPassed = testsPassed;
    }

    public int getTestsFailed() {
        return testsFailed;
    }

    public void setTestsFailed(int testsFailed) {
        this.testsFailed = testsFailed;
    }

    public int getTestsSkipped() {
        return testsSkipped;
    }

    public void setTestsSkipped(int testsSkipped) {
        this.testsSkipped = testsSkipped;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
