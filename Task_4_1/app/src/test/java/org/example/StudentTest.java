package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Юнит-тесты для класса Student.
 */
class StudentTest {

    @Test
    @DisplayName("Считает средний балл корректно")
    void testGpaCalculation() {
        GradeBook book = new GradeBook();
        book.addGrade(new Grade("Математика", 1, GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(new Grade("Физика", 1, GradeType.DIFF_TEST, Mark.GOOD));
        book.addGrade(new Grade("История", 2, GradeType.EXAM, Mark.SATISFACTORY));

        Student student = new Student("Тестов Тест", 3, book);

        assertEquals(4.0, student.calculateAverageGrade());
    }

    @Test
    @DisplayName("Не учитывает зачеты и 'неуд'")
    void testGpaIgnoresPassFailAndFails() {
        GradeBook book = new GradeBook();
        book.addGrade(new Grade("Математика", 1, GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(new Grade("Физкультура", 1, GradeType.PASS_FAIL_TEST, Mark.PASS));
        book.addGrade(new Grade("Физика", 2, GradeType.EXAM, Mark.FAIL));
        book.addGrade(new Grade("Физика", 2, GradeType.EXAM, Mark.FAIL));
        Student student = new Student("Тестов Тест", 3, book);

        assertEquals(5.0, student.calculateAverageGrade());
    }

    @Test
    @DisplayName("Возвращает 0.0, если нет оценок")
    void testGpaEmptyBook() {
        GradeBook book = new GradeBook();
        Student student = new Student("Тестов Тест", 1, book);
        assertEquals(0.0, student.calculateAverageGrade());
    }

    @Test
    @DisplayName("Бюджет: Можно, если нет '3' по экзаменам за 2 сессии")
    void testBudgetTransferValid() {
        GradeBook book = new GradeBook();
        book.addGrade(new Grade("Сессия 3 Экз", 3, GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(new Grade("Сессия 4 Экз", 4, GradeType.EXAM, Mark.GOOD));
        book.addGrade(new Grade("Сессия 4 Дифф", 4, GradeType.DIFF_TEST, Mark.SATISFACTORY));
        book.addGrade(new Grade("Сессия 2 Экз", 2, GradeType.EXAM, Mark.SATISFACTORY));

        Student student = new Student("Студент А", 5, book);

        assertTrue(student.canTransferToBudget());
    }

    @Test
    @DisplayName("Бюджет: Нельзя, если '3' по экзамену в ПОСЛЕДНЕЙ сессии")
    void testBudgetTransferFailLastSession() {
        GradeBook book = new GradeBook();
        book.addGrade(new Grade("Сессия 3 Экз", 3, GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(new Grade("Сессия 4 Экз", 4, GradeType.EXAM, Mark.SATISFACTORY));

        Student student = new Student("Студент Б", 5, book);

        assertFalse(student.canTransferToBudget());
    }

    @Test
    @DisplayName("Бюджет: Нельзя, если '3' по экзамену в ПРЕДПОСЛЕДНЕЙ сессии")
    void testBudgetTransferFailPrevSession() {
        GradeBook book = new GradeBook();
        book.addGrade(new Grade("Сессия 3 Экз", 3, GradeType.EXAM, Mark.SATISFACTORY));
        book.addGrade(new Grade("Сессия 4 Экз", 4, GradeType.EXAM, Mark.EXCELLENT));

        Student student = new Student("Студент В", 5, book);

        assertFalse(student.canTransferToBudget());
    }

    @Test
    @DisplayName("Бюджет: Нельзя, если нет закрытых сессий")
    void testBudgetTransferNoSessions() {
        GradeBook book = new GradeBook();
        Student student = new Student("Первокурсник", 1, book);
        assertFalse(student.canTransferToBudget());
    }

    @Test
    @DisplayName("Красный диплом: 100%")
    void testRedDiplomPerfect() {
        GradeBook book = new GradeBook();
        book.addGrade(new Grade("Предмет 1", 1, GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(new Grade("Предмет 2", 2, GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(new Grade("Предмет 3", 3, GradeType.DIFF_TEST, Mark.EXCELLENT));
        book.addGrade(new Grade("Предмет 4", 4, GradeType.DIFF_TEST, Mark.EXCELLENT));
        book.addGrade(new Grade("ВКР", 8, GradeType.THESIS, Mark.EXCELLENT));

        Student student = new Student("Отличник", 8, book);

        assertTrue(student.canGetRedDiplom());
    }

    @Test
    @DisplayName("Красный диплом: Ровно 75% 'отлично'")
    void testRedDiplomExactly75Percent() {
        GradeBook book = new GradeBook();
        book.addGrade(new Grade("Предмет 1", 1, GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(new Grade("Предмет 2", 2, GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(new Grade("Предмет 3", 3, GradeType.DIFF_TEST, Mark.EXCELLENT));
        book.addGrade(new Grade("Предмет 4", 4, GradeType.DIFF_TEST, Mark.GOOD));
        book.addGrade(new Grade("ВКР", 8, GradeType.THESIS, Mark.EXCELLENT));

        Student student = new Student("Хорошист", 8, book);

        assertTrue(student.canGetRedDiplom());
    }

    @Test
    @DisplayName("Красный диплом: Меньше 75% 'отлично'")
    void testRedDiplomFailPercentage() {
        GradeBook book = new GradeBook();
        book.addGrade(new Grade("Предмет 1", 1, GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(new Grade("Предмет 2", 2, GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(new Grade("Предмет 3", 3, GradeType.DIFF_TEST, Mark.GOOD));
        book.addGrade(new Grade("Предмет 4", 4, GradeType.DIFF_TEST, Mark.GOOD));
        book.addGrade(new Grade("ВКР", 8, GradeType.THESIS, Mark.EXCELLENT));

        Student student = new Student("Хорошист 2", 8, book);

        assertFalse(student.canGetRedDiplom());
    }

    @Test
    @DisplayName("Красный диплом: Есть 'удовлетворительно' (3)")
    void testRedDiplomFailHasSatisfactory() {
        GradeBook book = new GradeBook();
        book.addGrade(new Grade("Предмет 1", 1, GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(new Grade("Предмет 2", 2, GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(new Grade("Предмет 3", 3, GradeType.DIFF_TEST, Mark.SATISFACTORY));
        book.addGrade(new Grade("ВКР", 8, GradeType.THESIS, Mark.EXCELLENT));

        Student student = new Student("Троечник", 8, book);

        assertFalse(student.canGetRedDiplom());
    }

    @Test
    @DisplayName("Красный диплом: Нет оценки за ВКР")
    void testRedDiplomFailNoThesis() {
        GradeBook book = new GradeBook();
        book.addGrade(new Grade("Предмет 1", 1, GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(new Grade("Предмет 2", 2, GradeType.EXAM, Mark.EXCELLENT));

        Student student = new Student("Отличник без ВКР", 8, book);

        assertFalse(student.canGetRedDiplom());
    }

    @Test
    @DisplayName("Красный диплом: ВКР не 'отлично'")
    void testRedDiplomFailThesisNotExcellent() {
        GradeBook book = new GradeBook();
        book.addGrade(new Grade("Предмет 1", 1, GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(new Grade("Предмет 2", 2, GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(new Grade("ВКР", 8, GradeType.THESIS, Mark.GOOD));

        Student student = new Student("Отличник с ВКР на 4", 8, book);

        assertFalse(student.canGetRedDiplom());
    }

    @Test
    @DisplayName("Стипендия: Можно, последняя сессия без '3' и 'незачет'")
    void testScholarshipValid() {
        GradeBook book = new GradeBook();
        book.addGrade(new Grade("Сессия 3 Экз", 3, GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(new Grade("Сессия 3 Дифф", 3, GradeType.DIFF_TEST, Mark.GOOD));
        book.addGrade(new Grade("Сессия 3 Зачет", 3, GradeType.PASS_FAIL_TEST, Mark.PASS));
        book.addGrade(new Grade("Сессия 2 Экз", 2, GradeType.EXAM, Mark.SATISFACTORY));

        Student student = new Student("Студент", 4, book);

        assertTrue(student.canGetIncreasedScholarship());
    }

    @Test
    @DisplayName("Стипендия: Нельзя, есть '3' в последней сессии")
    void testScholarshipFailHasSatisfactory() {
        GradeBook book = new GradeBook();
        book.addGrade(new Grade("Сессия 3 Экз", 3, GradeType.EXAM, Mark.SATISFACTORY));
        book.addGrade(new Grade("Сессия 3 Зачет", 3, GradeType.PASS_FAIL_TEST, Mark.PASS));

        Student student = new Student("Студент", 4, book);

        assertFalse(student.canGetIncreasedScholarship());
    }

    @Test
    @DisplayName("Стипендия: Нельзя, есть 'незачет' в последней сессии")
    void testScholarshipFailHasFailedPass() {
        GradeBook book = new GradeBook();
        book.addGrade(new Grade("Сессия 3 Экз", 3, GradeType.EXAM, Mark.EXCELLENT));
        book.addGrade(new Grade("Сессия 3 Зачет", 3, GradeType.PASS_FAIL_TEST, Mark.FAIL));

        Student student = new Student("Студент", 4, book);

        assertFalse(student.canGetIncreasedScholarship());
    }

    @Test
    @DisplayName("Стипендия: Нельзя, нет закрытых сессий")
    void testScholarshipFailNoSessions() {
        GradeBook book = new GradeBook();
        Student student = new Student("Первокурсник", 1, book);
        assertFalse(student.canGetIncreasedScholarship());
    }
}
