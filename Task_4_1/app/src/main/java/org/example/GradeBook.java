package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс зачетная книжка.
 * Хранит список оценок и предоставляет методы для их анализа.
 */
public class GradeBook {

    private final List<Grade> grades = new ArrayList<>();
    private final String studentName;
    private int currentSemester;

    public GradeBook(String studentName, int currentSemester) {
        this.studentName = studentName;
        this.currentSemester = currentSemester;
    }

    /**
     * Добавляет новую оценку в зачетку.
     */
    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    /**
     * Расчет текущего среднего балла за все время обучения.
     * Учитываются экзамены и дифф зачеты.
     */
    public double calculateCurrentGpa() {
        List<Grade> gradedItems = grades.stream()
                .filter(g -> g.isDiplomGrade() && g.getValue() >= Grade.SATISFACTORY)
                .toList();

        if (gradedItems.isEmpty()) {
            return 0.0;
        }

        double sum = gradedItems.stream()
                .mapToInt(Grade::getValue)
                .sum();

        return sum / gradedItems.size();
    }

    /**
     * Возможность перевода с платной на бюджетную форму.
     * Нет "3" за последние две сессии.
     * По дифф зачетам "3" допустимы.
     */
    public boolean canTransferToBudget() {

        int lastSession = currentSemester - 1;
        int prevSession = currentSemester - 2;

        if (lastSession < 1) {
            return false;
        }

        List<Integer> sessionsToCheck = new ArrayList<>();
        sessionsToCheck.add(lastSession);
        if (prevSession > 0) {
            sessionsToCheck.add(prevSession);
        }

        boolean hasBadExamGrade = grades.stream()
                .filter(g -> sessionsToCheck.contains(g.getSemester()))
                .filter(g -> g.getType() == GradeType.EXAM)
                .anyMatch(g -> g.getValue() == Grade.SATISFACTORY);

        return !hasBadExamGrade;
    }

    /**
     * Возможность получения красного диплома.
     * Это прогноз основанный на всех имеющихся оценках.
     */
    public boolean canGetRedDiplom() {
        List<Grade> finalGrades = grades.stream()
                .filter(Grade::isDiplomGrade)
                .toList();

        if (finalGrades.isEmpty()) {
            return false;
        }


        boolean hasSatisfactory = finalGrades.stream()
                .anyMatch(g -> g.getValue() == Grade.SATISFACTORY);
        if (hasSatisfactory) {
            return false;
        }


        long excellentCount = finalGrades.stream()
                .filter(g -> g.getValue() == Grade.EXCELLENT)
                .count();

        double excellentPercentage = (double) excellentCount / finalGrades.size();
        if (excellentPercentage < 0.75) {
            return false;
        }


        boolean thesisIsExcellent = grades.stream()
                .filter(g -> g.getType() == GradeType.THESIS)
                .findFirst()
                .map(g -> g.getValue() == Grade.EXCELLENT)
                .orElse(false);

        return thesisIsExcellent;
    }

    /**
     * Возможность получения повышенной стипендии в текущем семестре.
     * Закрыта последняя сессия без '3'
     * и без 'незачетов'.
     */
    public boolean canGetIncreasedScholarship() {
        int lastSession = currentSemester - 1;
        if (lastSession < 1) {
            return false;
        }

        boolean hasSatisfactory = grades.stream()
                .filter(g -> g.getSemester() == lastSession && g.isDiplomGrade())
                .anyMatch(g -> g.getValue() == Grade.SATISFACTORY);


        boolean hasFailedPassFail = grades.stream()
                .filter(g -> g.getSemester() == lastSession
                        && g.getType() == GradeType.PASS_FAIL_TEST)
                .anyMatch(g -> g.getValue() != Grade.PASSED);

        return !hasSatisfactory && !hasFailedPassFail;
    }
}