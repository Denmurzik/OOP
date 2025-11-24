package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * Расчет текущего среднего балла за все время обучения.
     * Учитываются экзамены и дифф зачеты.
     */
    public double calculateCurrentGpa() {
        List<Grade> gradedItems = getGrades().stream()
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