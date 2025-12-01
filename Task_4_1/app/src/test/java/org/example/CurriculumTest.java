package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.core.Grade;
import org.example.core.GradeType;
import org.example.core.Mark;
import org.example.model.Curriculum;
import org.example.model.GradeBook;
import org.example.model.SubjectRequirement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CurriculumTest {

    @Test
    @DisplayName("SubjectRequirement: корректно хранит данные")
    void testSubjectRequirement() {
        SubjectRequirement req = new SubjectRequirement("Math", GradeType.EXAM);
        assertEquals("Math", req.getSubjectName());
        assertEquals(GradeType.EXAM, req.getType());
    }

    @Test
    @DisplayName("Curriculum: План выполнен, если все предметы сданы")
    void testCheckAllSubjectsPassedSuccess() {
        Curriculum curriculum = new Curriculum();
        curriculum.addRequirement(1, "Math", GradeType.EXAM);
        curriculum.addRequirement(1, "Physics", GradeType.DIFF_TEST);

        GradeBook book = new GradeBook();
        book.addGrade(1, new Grade("Math", GradeType.EXAM, Mark.GOOD));
        book.addGrade(1, new Grade("Physics", GradeType.DIFF_TEST, Mark.SATISFACTORY));

        assertTrue(curriculum.checkAllSubjectsPassed(book));
    }

    @Test
    @DisplayName("Curriculum: План НЕ выполнен, если предмет не сдан (нет оценки)")
    void testCheckAllSubjectsPassedFailMissing() {
        Curriculum curriculum = new Curriculum();
        curriculum.addRequirement(1, "Math", GradeType.EXAM);

        GradeBook book = new GradeBook();

        assertFalse(curriculum.checkAllSubjectsPassed(book));
    }

    @Test
    @DisplayName("Curriculum: План НЕ выполнен, если оценка 'неуд'")
    void testCheckAllSubjectsPassedFailBadMark() {
        Curriculum curriculum = new Curriculum();
        curriculum.addRequirement(1, "Math", GradeType.EXAM);

        GradeBook book = new GradeBook();
        book.addGrade(1, new Grade("Math", GradeType.EXAM, Mark.FAIL)); // Двойка

        assertFalse(curriculum.checkAllSubjectsPassed(book));
    }

    @Test
    @DisplayName("Curriculum: План НЕ выполнен, если семестра нет в зачетке")
    void testCheckAllSubjectsPassedFailMissingSemester() {
        Curriculum curriculum = new Curriculum();
        curriculum.addRequirement(1, "Math", GradeType.EXAM);

        GradeBook book = new GradeBook();

        assertFalse(curriculum.checkAllSubjectsPassed(book));
    }
}
