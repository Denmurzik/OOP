
package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.example.core.Grade;
import org.example.core.GradeType;
import org.example.core.Mark;
import org.example.model.GradeBook;
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
        Grade grade = new Grade("Math", GradeType.EXAM, Mark.EXCELLENT);

        book.addGrade(1, grade);

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
            grades.add(new Grade("History", GradeType.EXAM, Mark.GOOD));
        });
    }

    @Test
    @DisplayName("Расчет среднего балла (только экзамены и дифф. зачеты)")
    void testCalculateCurrentGpa() {
        GradeBook book = new GradeBook();

        book.addGrade(1, new Grade("Math", GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(1, new Grade("Physics", GradeType.DIFF_TEST, Mark.GOOD));

        book.addGrade(1, new Grade("PE", GradeType.PASS_FAIL_TEST, Mark.PASS));

        book.addGrade(1, new Grade("Chemistry", GradeType.EXAM, Mark.FAIL));

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
        book.addGrade(1, new Grade("Math", GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(8, new Grade("Thesis", GradeType.THESIS, Mark.EXCELLENT));

        assertEquals(3.0, book.calculateCurrentGpa());
    }
}
