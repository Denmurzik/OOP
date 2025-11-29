package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.example.core.Grade;
import org.example.core.GradeType;
import org.example.core.Mark;

/**
 * Класс Семестр.
 * Хранит оценки за конкретный семестр.
 */
public class Semester {

    private final int number;
    private final List<Grade> grades;

    /**
     * Конструктор.
     *
     * @param number Номер семестра
     */
    public Semester(int number) {
        this.number = number;
        this.grades = new ArrayList<>();
    }

    /**
     * Добавляет оценку в семестр.
     *
     * @param grade Оценка
     */
    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    /**
     * Геттер.
     *
     * @return номер семестра
     */
    public int getNumber() {
        return number;
    }

    /**
     * Возвращает список оценок семестра.
     *
     * @return неизменяемый список оценок
     */
    public List<Grade> getGrades() {
        return Collections.unmodifiableList(grades);
    }

    /**
     * Проверяет, есть ли тройки за экзамены в этом семестре.
     *
     * @return true, если есть тройка
     */
    public boolean hasSatisfactoryInExams() {
        return grades.stream()
                .filter(g -> g.getType() == GradeType.EXAM)
                .anyMatch(g -> g.getMark() == Mark.SATISFACTORY);
    }

    /**
     * Проверяет, есть ли тройки за экзамены или дифф. зачеты.
     *
     * @return true, если есть тройка
     */
    public boolean hasSatisfactoryInDiplomGrades() {
        return grades.stream()
                .filter(Grade::isDiplomGrade)
                .anyMatch(g -> g.getMark() == Mark.SATISFACTORY);
    }

    /**
     * Проверяет, есть ли незачеты.
     *
     * @return true, если есть незачет
     */
    public boolean hasFailedPassFail() {
        return grades.stream()
                .filter(g -> g.getType() == GradeType.PASS_FAIL_TEST)
                .anyMatch(g -> g.getMark() != Mark.PASS);
    }

    /**
     * Сумма значений оценок, идущих в диплом (для расчета среднего балла).
     * Учитываются только экзамены и дифф. зачеты с оценкой не ниже
     * удовлетворительно.
     *
     * @return сумма баллов
     */
    public int getGradeSum() {
        return grades.stream()
                .filter(g -> g.isDiplomGrade() && g.getValue() >= Mark.SATISFACTORY.getValue())
                .mapToInt(Grade::getValue)
                .sum();
    }

    /**
     * Количество оценок, идущих в диплом (для расчета среднего балла).
     * Учитываются только экзамены и дифф. зачеты с оценкой не ниже
     * удовлетворительно.
     *
     * @return количество оценок
     */
    public int getGradeCount() {
        return (int) grades.stream()
                .filter(g -> g.isDiplomGrade() && g.getValue() >= Mark.SATISFACTORY.getValue())
                .count();
    }

    /**
     * Проверяет, закрыт ли семестр (все предметы сданы и нет двоек).
     *
     * @param plan список требований для этого семестра
     * @return true, если семестр закрыт
     */
    public boolean isClosed(List<SubjectRequirement> plan) {
        if (plan == null || plan.isEmpty()) {
            return true; // Если требований нет, семестр считается закрытым
        }

        for (SubjectRequirement req : plan) {
            boolean passed = grades.stream()
                    .anyMatch(g -> g.getSubjectName().equals(req.getSubjectName())
                            && g.getType() == req.getType()
                            && g.getValue() > Mark.FAIL.getValue());
            if (!passed) {
                return false;
            }
        }
        return true;
    }
}
