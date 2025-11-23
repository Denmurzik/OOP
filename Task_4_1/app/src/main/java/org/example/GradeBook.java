package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс зачетная книжка.
 * Хранит список оценок.
 */
public class GradeBook {

    private final List<Grade> grades = new ArrayList<>();

    public GradeBook() {
    }

    /**
     * Добавляет новую оценку в зачетку.
     */
    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    /**
     * Возвращает список оценок.
     *
     * @return неизменяемый список оценок
     */
    public List<Grade> getGrades() {
        return Collections.unmodifiableList(grades);
    }

    /**
     * Расчет текущего среднего балла за все время обучения.
     * Учитываются экзамены и дифф зачеты.
     */
    public double calculateCurrentGpa() {
        List<Grade> gradedItems = grades.stream()
                .filter(g -> g.isDiplomGrade() && g.getValue() >= Mark.SATISFACTORY.getValue())
                .toList();

        if (gradedItems.isEmpty()) {
            return 0.0;
        }

        double sum = gradedItems.stream()
                .mapToInt(Grade::getValue)
                .sum();

        return sum / gradedItems.size();
    }
}