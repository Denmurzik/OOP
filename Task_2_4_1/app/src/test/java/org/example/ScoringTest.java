package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.example.model.Config;
import org.example.model.Lab;
import org.junit.jupiter.api.Test;

class ScoringTest {

    @Test
    void scoreForFullScoreWhenAllGreenBeforeSoft() {
        Lab lab = new Lab("2-1-1", "lab", 2,
                LocalDate.of(2099, 1, 1), LocalDate.of(2099, 2, 1));
        assertEquals(2, Scoring.scoreFor(true, lab, LocalDate.of(2026, 1, 1)));
    }

    @Test
    void scoreHalvedAfterSoft() {
        Lab lab = new Lab("2-1-1", "lab", 4,
                LocalDate.of(2026, 1, 1), LocalDate.of(2099, 2, 1));
        assertEquals(2, Scoring.scoreFor(true, lab, LocalDate.of(2026, 1, 15)));
    }

    @Test
    void scoreZeroAfterHard() {
        Lab lab = new Lab("2-1-1", "lab", 4,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10));
        assertEquals(0, Scoring.scoreFor(true, lab, LocalDate.of(2026, 1, 15)));
    }

    @Test
    void scoreZeroIfNotAllGreen() {
        Lab lab = new Lab("2-1-1", "lab", 4,
                LocalDate.of(2099, 1, 1), LocalDate.of(2099, 2, 1));
        assertEquals(0, Scoring.scoreFor(false, lab, LocalDate.now()));
    }

    @Test
    void gradeBasedOnPercent() {
        Config cfg = new Config();
        cfg.getLabs().add(new Lab("a", "a", 4, null, null));
        cfg.getLabs().add(new Lab("b", "b", 6, null, null));
        cfg.getSettings().getGradeMinPercent().put(5, 90);
        cfg.getSettings().getGradeMinPercent().put(4, 70);
        cfg.getSettings().getGradeMinPercent().put(3, 50);
        cfg.getSettings().getGradeMinPercent().put(2, 0);

        assertEquals("5", Scoring.grade(10, cfg));
        assertEquals("4", Scoring.grade(7, cfg));
        assertEquals("3", Scoring.grade(5, cfg));
        assertEquals("2", Scoring.grade(0, cfg));
    }
}
