package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
}
