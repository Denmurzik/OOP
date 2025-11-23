package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Юнит тесты для GradeBook.
 */
class GradeBookTest {

    @Test
    @DisplayName("Добавление оценки и получение списка")
    void testAddAndGetGrades() {
        GradeBook book = new GradeBook();
        Grade grade = new Grade("Math", 1, GradeType.EXAM, Mark.EXCELLENT);

        book.addGrade(grade);

        List<Grade> grades = book.getGrades();
        assertEquals(1, grades.size());
        assertEquals(grade, grades.get(0));
    }

    @Test
    @DisplayName("Список оценок неизменяем извне")
    void testGetGradesIsImmutable() {
        GradeBook book = new GradeBook();
        List<Grade> grades = book.getGrades();

        assertThrows(UnsupportedOperationException.class, () -> {
            grades.add(new Grade("History", 1, GradeType.EXAM, Mark.GOOD));
        });
    }

    @Test
    @DisplayName("Расчет среднего балла (только экзамены и дифф. зачеты)")
    void testCalculateCurrentGpa() {
        GradeBook book = new GradeBook();

        book.addGrade(new Grade("Math", 1, GradeType.EXAM, Mark.EXCELLENT));

        book.addGrade(new Grade("Physics", 1, GradeType.DIFF_TEST, Mark.GOOD));

        book.addGrade(new Grade("PE", 1, GradeType.PASS_FAIL_TEST, Mark.PASS));

        book.addGrade(new Grade("Chemistry", 1, GradeType.EXAM, Mark.FAIL));

        assertEquals(4.5, book.calculateCurrentGpa());
    }

    @Test
    @DisplayName("Средний балл 0.0 если нет оценок")
    void testCalculateCurrentGpaEmpty() {
        GradeBook book = new GradeBook();
        assertEquals(0.0, book.calculateCurrentGpa());
    }

    @Test
    @DisplayName("Средний балл игнорирует неудовлетворительные оценки")
    void testCalculateCurrentGpaIgnoresFails() {
        GradeBook book = new GradeBook();
        book.addGrade(new Grade("Math", 1, GradeType.EXAM, Mark.SATISFACTORY));
        book.addGrade(new Grade("Physics", 1, GradeType.EXAM, Mark.FAIL));

        assertEquals(3.0, book.calculateCurrentGpa());
    }
}
