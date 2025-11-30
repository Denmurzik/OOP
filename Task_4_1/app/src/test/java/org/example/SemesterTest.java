package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.example.core.Grade;
import org.example.core.GradeType;
import org.example.core.Mark;
import org.example.model.Semester;
import org.example.model.SubjectRequirement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class SemesterTest {

    @Test
    @DisplayName("Инициализация семестра")
    void testInitialization() {
        Semester semester = new Semester(1);
        assertEquals(1, semester.getNumber());
        assertTrue(semester.getGrades().isEmpty());
    }

    @Test
    @DisplayName("Добавление оценки")
    void testAddGrade() {
        Semester semester = new Semester(1);
        Grade grade = new Grade("Math", GradeType.EXAM, Mark.EXCELLENT);
        semester.addGrade(grade);

        assertEquals(1, semester.getGrades().size());
        assertEquals(grade, semester.getGrades().get(0));
    }

    @Test
    @DisplayName("Список оценок неизменяем")
    void testGetGradesImmutable() {
        Semester semester = new Semester(1);
        List<Grade> grades = semester.getGrades();
        assertThrows(UnsupportedOperationException.class, () -> {
            grades.add(new Grade("Math", GradeType.EXAM, Mark.EXCELLENT));
        });
    }

    @Test
    @DisplayName("Проверка наличия троек за экзамены")
    void testHasSatisfactoryInExams() {
        Semester semester = new Semester(1);
        
        // Нет оценок
        assertFalse(semester.hasSatisfactoryInExams());

        // Только хорошие оценки
        semester.addGrade(new Grade("Math", GradeType.EXAM, Mark.GOOD));
        assertFalse(semester.hasSatisfactoryInExams());

        // Тройка за дифзачет
        semester.addGrade(new Grade("Physics", GradeType.DIFF_TEST, Mark.SATISFACTORY));
        assertFalse(semester.hasSatisfactoryInExams());

        // Тройка за экзамен
        semester.addGrade(new Grade("History", GradeType.EXAM, Mark.SATISFACTORY));
        assertTrue(semester.hasSatisfactoryInExams());
    }

    @Test
    @DisplayName("Проверка наличия троек в дипломных оценках")
    void testHasSatisfactoryInDiplomGrades() {
        Semester semester = new Semester(1);

        // Тройка за экзамен
        semester.addGrade(new Grade("Math", GradeType.EXAM, Mark.SATISFACTORY));
        assertTrue(semester.hasSatisfactoryInDiplomGrades());
        
        Semester semester2 = new Semester(2);
        // Тройка за дифзачет
        semester2.addGrade(new Grade("Physics", GradeType.DIFF_TEST, Mark.SATISFACTORY));
        assertTrue(semester2.hasSatisfactoryInDiplomGrades());

        Semester semester3 = new Semester(3);
        semester3.addGrade(new Grade("PE", GradeType.PASS_FAIL_TEST, Mark.PASS));
        assertFalse(semester3.hasSatisfactoryInDiplomGrades());
    }

    @Test
    @DisplayName("Расчет суммы и количества оценок для диплома")
    void testGradeSumAndCount() {
        Semester semester = new Semester(1);
        
        // Экзамен 5
        semester.addGrade(new Grade("Math", GradeType.EXAM, Mark.EXCELLENT)); // 5
        // Дифзачет 4
        semester.addGrade(new Grade("Physics", GradeType.DIFF_TEST, Mark.GOOD)); // 4
        // Зачет (не идёт)
        semester.addGrade(new Grade("PE", GradeType.PASS_FAIL_TEST, Mark.PASS));
        // Экзамен 2 (не идёт)
        semester.addGrade(new Grade("History", GradeType.EXAM, Mark.FAIL));

        assertEquals(9, semester.getGradeSum());
        assertEquals(2, semester.getGradeCount());
    }

    @Test
    @DisplayName("Проверка закрытия семестра")
    void testIsClosed() {
        Semester semester = new Semester(1);
        List<SubjectRequirement> plan = new ArrayList<>();
        
        // Пустой план
        assertTrue(semester.isClosed(null));
        assertTrue(semester.isClosed(plan));

        plan.add(new SubjectRequirement("Math", GradeType.EXAM));
        plan.add(new SubjectRequirement("Physics", GradeType.DIFF_TEST));

        // Предметов нет
        assertFalse(semester.isClosed(plan));

        // Сдан только один
        semester.addGrade(new Grade("Math", GradeType.EXAM, Mark.GOOD));
        assertFalse(semester.isClosed(plan));

        // Сданы оба, но один на 2
        semester.addGrade(new Grade("Physics", GradeType.DIFF_TEST, Mark.FAIL));
        assertFalse(semester.isClosed(plan));

        // Сданы оба успешно.
        semester.addGrade(new Grade("Physics", GradeType.DIFF_TEST, Mark.SATISFACTORY));
        assertTrue(semester.isClosed(plan));

    }
}
