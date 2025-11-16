package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Юнит-тесты для класса GradeBook.
 */
class GradeBookTest {


    @Test
    @DisplayName("Считает средний балл корректно")
    void testGpaCalculation() {
        GradeBook book = new GradeBook("Тестов Тест", 3);
        book.addGrade(new Grade("Математика", 1, GradeType.EXAM, 5));
        book.addGrade(new Grade("Физика", 1, GradeType.DIFF_TEST, 4));
        book.addGrade(new Grade("История", 2, GradeType.EXAM, 3));

        assertEquals(4.0, book.calculateCurrentGpa());
    }

    @Test
    @DisplayName("Не учитывает зачеты и 'неуд'")
    void testGpaIgnoresPassFailAndFails() {
        GradeBook book = new GradeBook("Тестов Тест", 3);
        book.addGrade(new Grade("Математика", 1, GradeType.EXAM, 5));
        book.addGrade(new Grade("Физкультура", 1, GradeType.PASS_FAIL_TEST, 1));
        book.addGrade(new Grade("Физика", 2, GradeType.EXAM, 2));

        assertEquals(5.0, book.calculateCurrentGpa());
    }

    @Test
    @DisplayName("Возвращает 0.0, если нет оценок")
    void testGpaEmptyBook() {
        GradeBook book = new GradeBook("Тестов Тест", 1);
        assertEquals(0.0, book.calculateCurrentGpa());
    }


    @Test
    @DisplayName("Бюджет: Можно, если нет '3' по экзаменам за 2 сессии")
    void testBudgetTransferValid() {
        GradeBook book = new GradeBook("Студент А", 5);
        book.addGrade(new Grade("Сессия 3 Экз", 3, GradeType.EXAM, 5));
        book.addGrade(new Grade("Сессия 4 Экз", 4, GradeType.EXAM, 4));
        book.addGrade(new Grade("Сессия 4 Дифф", 4, GradeType.DIFF_TEST, 3));
        book.addGrade(new Grade("Сессия 2 Экз", 2, GradeType.EXAM, 3));

        assertTrue(book.canTransferToBudget());
    }

    @Test
    @DisplayName("Бюджет: Нельзя, если '3' по экзамену в ПОСЛЕДНЕЙ сессии")
    void testBudgetTransferFailLastSession() {
        GradeBook book = new GradeBook("Студент Б", 5);
        book.addGrade(new Grade("Сессия 3 Экз", 3, GradeType.EXAM, 5));
        book.addGrade(new Grade("Сессия 4 Экз", 4, GradeType.EXAM, 3));

        assertFalse(book.canTransferToBudget());
    }

    @Test
    @DisplayName("Бюджет: Нельзя, если '3' по экзамену в ПРЕДПОСЛЕДНЕЙ сессии")
    void testBudgetTransferFailPrevSession() {
        GradeBook book = new GradeBook("Студент В", 5);
        book.addGrade(new Grade("Сессия 3 Экз", 3, GradeType.EXAM, 3));
        book.addGrade(new Grade("Сессия 4 Экз", 4, GradeType.EXAM, 5));

        assertFalse(book.canTransferToBudget());
    }

    @Test
    @DisplayName("Бюджет: Нельзя, если нет закрытых сессий")
    void testBudgetTransferNoSessions() {
        GradeBook book = new GradeBook("Первокурсник", 1);
        assertFalse(book.canTransferToBudget());
    }


    @Test
    @DisplayName("Красный диплом: 100%")
    void testRedDiplomPerfect() {
        GradeBook book = new GradeBook("Отличник", 8);
        book.addGrade(new Grade("Предмет 1", 1, GradeType.EXAM, 5));
        book.addGrade(new Grade("Предмет 2", 2, GradeType.EXAM, 5));
        book.addGrade(new Grade("Предмет 3", 3, GradeType.DIFF_TEST, 5));
        book.addGrade(new Grade("Предмет 4", 4, GradeType.DIFF_TEST, 5));
        book.addGrade(new Grade("ВКР", 8, GradeType.THESIS, 5));

        assertTrue(book.canGetRedDiplom());
    }

    @Test
    @DisplayName("Красный диплом: Ровно 75% 'отлично'")
    void testRedDiplomExactly75Percent() {
        GradeBook book = new GradeBook("Хорошист", 8);
        book.addGrade(new Grade("Предмет 1", 1, GradeType.EXAM, 5));
        book.addGrade(new Grade("Предмет 2", 2, GradeType.EXAM, 5));
        book.addGrade(new Grade("Предмет 3", 3, GradeType.DIFF_TEST, 5));
        book.addGrade(new Grade("Предмет 4", 4, GradeType.DIFF_TEST, 4));
        book.addGrade(new Grade("ВКР", 8, GradeType.THESIS, 5));

        assertTrue(book.canGetRedDiplom());
    }

    @Test
    @DisplayName("Красный диплом: Меньше 75% 'отлично'")
    void testRedDiplomFailPercentage() {
        GradeBook book = new GradeBook("Хорошист 2", 8);
        book.addGrade(new Grade("Предмет 1", 1, GradeType.EXAM, 5));
        book.addGrade(new Grade("Предмет 2", 2, GradeType.EXAM, 5));
        book.addGrade(new Grade("Предмет 3", 3, GradeType.DIFF_TEST, 4));
        book.addGrade(new Grade("Предмет 4", 4, GradeType.DIFF_TEST, 4));
        book.addGrade(new Grade("ВКР", 8, GradeType.THESIS, 5));

        assertFalse(book.canGetRedDiplom());
    }

    @Test
    @DisplayName("Красный диплом: Есть 'удовлетворительно' (3)")
    void testRedDiplomFailHasSatisfactory() {
        GradeBook book = new GradeBook("Троечник", 8);
        book.addGrade(new Grade("Предмет 1", 1, GradeType.EXAM, 5));
        book.addGrade(new Grade("Предмет 2", 2, GradeType.EXAM, 5));
        book.addGrade(new Grade("Предмет 3", 3, GradeType.DIFF_TEST, 3));
        book.addGrade(new Grade("ВКР", 8, GradeType.THESIS, 5));

        assertFalse(book.canGetRedDiplom());
    }

    @Test
    @DisplayName("Красный диплом: Нет оценки за ВКР")
    void testRedDiplomFailNoThesis() {
        GradeBook book = new GradeBook("Отличник без ВКР", 8);
        book.addGrade(new Grade("Предмет 1", 1, GradeType.EXAM, 5));
        book.addGrade(new Grade("Предмет 2", 2, GradeType.EXAM, 5));

        assertFalse(book.canGetRedDiplom());
    }

    @Test
    @DisplayName("Красный диплом: ВКР не 'отлично'")
    void testRedDiplomFailThesisNotExcellent() {
        GradeBook book = new GradeBook("Отличник с ВКР на 4", 8);
        book.addGrade(new Grade("Предмет 1", 1, GradeType.EXAM, 5));
        book.addGrade(new Grade("Предмет 2", 2, GradeType.EXAM, 5));
        book.addGrade(new Grade("ВКР", 8, GradeType.THESIS, 4));

        assertFalse(book.canGetRedDiplom());
    }


    @Test
    @DisplayName("Стипендия: Можно, последняя сессия без '3' и 'незачет'")
    void testScholarshipValid() {
        GradeBook book = new GradeBook("Студент", 4);
        book.addGrade(new Grade("Сессия 3 Экз", 3, GradeType.EXAM, 5));
        book.addGrade(new Grade("Сессия 3 Дифф", 3, GradeType.DIFF_TEST, 4));
        book.addGrade(new Grade("Сессия 3 Зачет", 3, GradeType.PASS_FAIL_TEST, 1));
        book.addGrade(new Grade("Сессия 2 Экз", 2, GradeType.EXAM, 3));

        assertTrue(book.canGetIncreasedScholarship());
    }

    @Test
    @DisplayName("Стипендия: Нельзя, есть '3' в последней сессии")
    void testScholarshipFailHasSatisfactory() {
        GradeBook book = new GradeBook("Студент", 4);
        book.addGrade(new Grade("Сессия 3 Экз", 3, GradeType.EXAM, 3));
        book.addGrade(new Grade("Сессия 3 Зачет", 3, GradeType.PASS_FAIL_TEST, 1));

        assertFalse(book.canGetIncreasedScholarship());
    }

    @Test
    @DisplayName("Стипендия: Нельзя, есть 'незачет' в последней сессии")
    void testScholarshipFailHasFailedPass() {
        GradeBook book = new GradeBook("Студент", 4);
        book.addGrade(new Grade("Сессия 3 Экз", 3, GradeType.EXAM, 5));
        book.addGrade(new Grade("Сессия 3 Зачет", 3, GradeType.PASS_FAIL_TEST, 0));

        assertFalse(book.canGetIncreasedScholarship());
    }

    @Test
    @DisplayName("Стипендия: Нельзя, нет закрытых сессий")
    void testScholarshipFailNoSessions() {
        GradeBook book = new GradeBook("Первокурсник", 1);
        assertFalse(book.canGetIncreasedScholarship());
    }
}