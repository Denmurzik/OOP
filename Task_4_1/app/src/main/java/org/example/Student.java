package org.example;

import java.util.List;

/**
 * Класс Студент.
 * Хранит информацию о студенте и его зачетной книжке.
 */
public class Student {

    private final String name;
    private int currentSemester;
    private final GradeBook gradeBook;

    /**
     * Конструктор.
     *
     * @param name            Имя студента
     * @param currentSemester Текущий семестр
     * @param gradeBook       Зачетная книжка
     */
    public Student(String name, int currentSemester, GradeBook gradeBook) {
        this.name = name;
        this.currentSemester = currentSemester;
        this.gradeBook = gradeBook;
    }

    /**
     * Геттер.
     *
     * @return имя студента
     */
    public String getName() {
        return name;
    }

    /**
     * Геттер.
     *
     * @return текущий семестр
     */
    public int getCurrentSemester() {
        return currentSemester;
    }

    /**
     * Сеттер.
     *
     * @param currentSemester новый семестр
     */
    public void setCurrentSemester(int currentSemester) {
        this.currentSemester = currentSemester;
    }

    /**
     * Геттер.
     *
     * @return зачетная книжка
     */
    public GradeBook getGradeBook() {
        return gradeBook;
    }

    /**
     * Расчет текущего среднего балла за все время обучения.
     */
    public double calculateAverageGrade() {
        return gradeBook.calculateCurrentGpa();
    }

    /**
     * Возможность перевода с платной на бюджетную форму.
     * Нет "3" за последние две сессии.
     * По дифф зачетам "3" допустимы.
     */
    public boolean canTransferToBudget() {
        int lastSession = currentSemester - 1;
        int prevSession = currentSemester - 2;

        if (lastSession < 1) {
            return false;
        }

        List<Grade> grades = gradeBook.getGrades();

        boolean lastSessionOk = checkSessionForBudget(grades, lastSession);
        if (!lastSessionOk) {
            return false;
        }

        if (prevSession > 0) {
            return checkSessionForBudget(grades, prevSession);
        }

        return true;
    }

    private boolean checkSessionForBudget(List<Grade> grades, int semester) {
        return grades.stream()
                .filter(g -> g.getSemester() == semester)
                .filter(g -> g.getType() == GradeType.EXAM)
                .noneMatch(g -> g.getMark() == Mark.SATISFACTORY);
    }

    /**
     * Возможность получения красного диплома.
     * Это прогноз основанный на всех имеющихся оценках.
     */
    public boolean canGetRedDiplom() {
        List<Grade> grades = gradeBook.getGrades();
        List<Grade> finalGrades = grades.stream()
                .filter(Grade::isDiplomGrade)
                .toList();

        if (finalGrades.isEmpty()) {
            return false;
        }

        boolean hasSatisfactory = finalGrades.stream()
                .anyMatch(g -> g.getMark() == Mark.SATISFACTORY);
        if (hasSatisfactory) {
            return false;
        }

        long excellentCount = finalGrades.stream()
                .filter(g -> g.getMark() == Mark.EXCELLENT)
                .count();

        double excellentPercentage = (double) excellentCount / finalGrades.size();
        if (excellentPercentage < 0.75) {
            return false;
        }

        return grades.stream()
                .filter(g -> g.getType() == GradeType.THESIS)
                .findFirst()
                .map(g -> g.getMark() == Mark.EXCELLENT)
                .orElse(false);
    }

    /**
     * Возможность получения повышенной стипендии в текущем семестре.
     * Закрыта последняя сессия без '3'
     * и без 'незачетов'.
     */
    public boolean canGetIncreasedScholarship() {
        int lastSession = currentSemester - 1;
        if (lastSession < 1) {
            return false;
        }

        List<Grade> grades = gradeBook.getGrades();

        boolean hasSatisfactory = grades.stream()
                .filter(g -> g.getSemester() == lastSession
                        && g.isDiplomGrade())
                .anyMatch(g -> g.getMark() == Mark.SATISFACTORY);

        boolean hasFailedPassFail = grades.stream()
                .filter(g -> g.getSemester() == lastSession
                        && g.getType() == GradeType.PASS_FAIL_TEST)
                .anyMatch(g -> g.getMark() != Mark.PASS);

        return !hasSatisfactory && !hasFailedPassFail;
    }
}
