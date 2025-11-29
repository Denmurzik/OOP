package org.example.core;

/**
 * Хранит информацию об одной оценке.
 */
public class Grade {

    private final String subjectName;
    private final int semester;
    private final GradeType type;
    private final Mark mark;

    /**
     * Конструктор для создания объекта оценки.
     *
     * @param subjectName Название предмета
     * @param semester    Номер семестра
     * @param type        Тип контроля (экзамен, дифф. зачет, зачет и т.д.)
     * @param mark        Оценка
     */
    public Grade(String subjectName, int semester, GradeType type, Mark mark) {
        this.subjectName = subjectName;
        this.semester = semester;
        this.type = type;
        this.mark = mark;
    }

    /**
     * Проверяет, идет ли оценка в диплом.
     *
     * @return true, если это оценка, идущая в диплом (экзамен или дифф. зачет)
     */
    public boolean isDiplomGrade() {
        return type == GradeType.EXAM || type == GradeType.DIFF_TEST;
    }

    /**
     * Геттер.
     *
     * @return название предмета
     */
    public String getSubjectName() {
        return subjectName;
    }

    /**
     * Геттер.
     *
     * @return номер семестра
     */
    public int getSemester() {
        return semester;
    }

    /**
     * Геттер.
     *
     * @return тип оценки
     */
    public GradeType getType() {
        return type;
    }

    /**
     * Геттер.
     *
     * @return значение оценки
     */
    public int getValue() {
        return mark.getValue();
    }

    /**
     * Геттер.
     *
     * @return оценка
     */
    public Mark getMark() {
        return mark;
    }
}
