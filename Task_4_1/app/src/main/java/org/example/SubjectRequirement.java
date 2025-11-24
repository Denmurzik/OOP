package org.example;

/**
 * Требование к предмету в учебном плане.
 */
public class SubjectRequirement {
    private final String subjectName;
    private final GradeType type;

    /**
     * Конструктор.
     *
     * @param subjectName Название предмета
     * @param type Тип контроля
     */
    public SubjectRequirement(String subjectName, GradeType type) {
        this.subjectName = subjectName;
        this.type = type;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public GradeType getType() {
        return type;
    }
}
