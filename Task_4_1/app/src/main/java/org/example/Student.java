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
    private Curriculum curriculum;

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

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
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

        if (checkSessionForBudget(lastSession)) {
            return false;
        }

        if (prevSession > 0) {
            return !checkSessionForBudget(prevSession);
        }

        return true;
    }

    private boolean checkSessionForBudget(int semesterNum) {
        Semester semester = gradeBook.getSemester(semesterNum);
        return semester != null && semester.hasSatisfactoryInExams();
    }

    /**
     * Возможность получения красного диплома.
     * Это прогноз основанный на всех имеющихся оценках.
     */
    public boolean canGetRedDiplom() {
        if (curriculum != null && !curriculum.checkAllSubjectsPassed(gradeBook)) {
            return false;
        }

        List<Grade> grades = gradeBook.getGrades();
        List<Grade> finalGrades = grades.stream()
                .filter(Grade::isDiplomGrade)
                .toList();

        if (finalGrades.isEmpty()) {
            return false;
        }

        if (hasSatisfactory(finalGrades)) {
            return false;
        }

        if (!hasExcellentPercentage(finalGrades)) {
            return false;
        }

        return isThesisExcellent(grades);
    }

    private boolean hasSatisfactory(List<Grade> grades) {
        return grades.stream()
                .anyMatch(g -> g.getMark() == Mark.SATISFACTORY);
    }

    private boolean hasExcellentPercentage(List<Grade> grades) {
        long excellentCount = grades.stream()
                .filter(g -> g.getMark() == Mark.EXCELLENT)
                .count();
        double excellentPercentage = (double) excellentCount / grades.size();
        return excellentPercentage >= 0.75;
    }

    private boolean isThesisExcellent(List<Grade> grades) {
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

        Semester semester = gradeBook.getSemester(lastSession);
        if (semester == null) {
            return false;
        }

        return !semester.hasSatisfactoryInDiplomGrades() && !semester.hasFailedPassFail();
    }
}
