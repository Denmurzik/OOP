package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.core.Grade;
import org.example.core.GradeType;
import org.example.core.Mark;

/**
 * Класс зачетная книжка.
 * Хранит список семестров и оценок в них.
 */
public class GradeBook {

    private final Map<Integer, Semester> semesters = new HashMap<>();

    /**
     * Добавляет новую оценку в зачетку.
     * Попадает в семестр.
     */
    public void addGrade(Grade grade) {
        int semesterNum = grade.getSemester();
        Semester semester = semesters.computeIfAbsent(semesterNum, Semester::new);
        semester.addGrade(grade);
    }

    /**
     * Возвращает список всех оценок.
     *
     * @return неизменяемый список всех оценок
     */
    public List<Grade> getGrades() {
        List<Grade> allGrades = new ArrayList<>();
        for (Semester semester : semesters.values()) {
            allGrades.addAll(semester.getGrades());
        }
        return Collections.unmodifiableList(allGrades);
    }

    /**
     * Возвращает семестр по номеру.
     *
     * @param number Номер семестра
     * @return Объект Semester или null, если семестра нет
     */
    public Semester getSemester(int number) {
        return semesters.get(number);
    }

    /**
     * Вычисляет текущий средний балл.
     *
     * @return средний балл
     */
    public double calculateCurrentGpa() {
        int totalSum = 0;
        int totalCount = 0;

        for (Semester semester : semesters.values()) {
            totalSum += semester.getGradeSum();
            totalCount += semester.getGradeCount();
        }

        if (totalCount == 0) {
            return 0.0;
        }

        return (double) totalSum / totalCount;
    }

    /**
     * Проверяет наличие троек за все время обучения.
     *
     * @return true, если есть хотя бы одна тройка
     */
    public boolean hasSatisfactoryGrades() {
        return getGrades().stream()
                .anyMatch(g -> g.getMark() == Mark.SATISFACTORY);
    }

    /**
     * Вычисляет процент отличных оценок среди тех, что идут в диплом.
     *
     * @return процент отличных оценок (от 0.0 до 1.0)
     */
    public double getExcellentPercentage() {
        List<Grade> diplomGrades = getGrades().stream()
                .filter(Grade::isDiplomGrade)
                .toList();

        if (diplomGrades.isEmpty()) {
            return 0.0;
        }

        long excellentCount = diplomGrades.stream()
                .filter(g -> g.getMark() == Mark.EXCELLENT)
                .count();

        return (double) excellentCount / diplomGrades.size();
    }

    /**
     * Проверяет, сдана ли ВКР на отлично.
     *
     * @return true, если ВКР на отлично
     */
    public boolean isThesisExcellent() {
        return getGrades().stream()
                .filter(g -> g.getType() == GradeType.THESIS)
                .findFirst()
                .map(g -> g.getMark() == Mark.EXCELLENT)
                .orElse(false);
    }
}
