package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Учебный план.
 */
public class Curriculum {

    private final Map<Integer, List<SubjectRequirement>> requirements = new HashMap<>();

    /**
     * Добавляет требование в учебный план.
     *
     * @param semester    Номер семестра
     * @param subjectName Название предмета
     * @param type        Тип контроля
     */
    public void addRequirement(int semester, String subjectName, GradeType type) {
        requirements.computeIfAbsent(semester, k -> new ArrayList<>())
                .add(new SubjectRequirement(subjectName, type));
    }

    /**
     * Проверяет, сдал ли студент все предметы из плана.
     *
     * @param gradeBook Зачетная книжка студента
     * @return true, если все предметы сданы
     */
    public boolean checkAllSubjectsPassed(GradeBook gradeBook) {
        for (Map.Entry<Integer, List<SubjectRequirement>> entry : requirements.entrySet()) {
            int semesterNum = entry.getKey();
            List<SubjectRequirement> semesterRequirements = entry.getValue();

            Semester semester = gradeBook.getSemester(semesterNum);
            if (semester == null) {
                return false;
            }

            for (SubjectRequirement req : semesterRequirements) {
                boolean passed = semester.getGrades().stream()
                        .anyMatch(g -> g.getSubjectName().equals(req.getSubjectName())
                                && g.getType() == req.getType()
                                && g.getValue() > Mark.FAIL.getValue());
                if (!passed) {
                    return false;
                }
            }
        }
        return true;
    }
}
