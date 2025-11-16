package org.example;

/**
 * Хранит информацию об одной оценке.
 */
public class Grade {

    public static final int EXCELLENT = 5;
    public static final int GOOD = 4;
    public static final int SATISFACTORY = 3;
    public static final int PASSED = 1;

    private final String subjectName;
    private final int semester;
    private final GradeType type;
    private final int value;

    /**
     * Конструктор для создания объекта оценки.
     *
     * @param subjectName Название предмета
     * @param semester Номер семестра
     * @param type Тип контроля (экзамен, дифф. зачет, зачет и т.д.)
     * @param value Значение оценки (5, 4, 3 или 1/0 для зачета)
     */
    public Grade(String subjectName, int semester, GradeType type, int value) {
        this.subjectName = subjectName;
        this.semester = semester;
        this.type = type;
        this.value = value;
    }

    /**
     * @return true, если это оценка, идущая в диплом (экзамен или дифф. зачет)
     */
    public boolean isDiplomGrade() {
        return type == GradeType.EXAM || type == GradeType.DIFF_TEST;
    }

    /**
     * Геттеры для полей класса.
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
        return value;
    }
}