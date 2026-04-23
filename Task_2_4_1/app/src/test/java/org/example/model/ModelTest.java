package org.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class ModelTest {

    @Test
    void labGettersSetters() {
        Lab lab = new Lab();
        lab.setId("2-1-1");
        lab.setName("Простые числа");
        lab.setMaxScore(1);
        lab.setSoftDeadline(LocalDate.of(2026, 3, 1));
        lab.setHardDeadline(LocalDate.of(2026, 3, 15));
        assertEquals("2-1-1", lab.getId());
        assertEquals("Простые числа", lab.getName());
        assertEquals(1, lab.getMaxScore());
        assertEquals(LocalDate.of(2026, 3, 1), lab.getSoftDeadline());
        assertEquals(LocalDate.of(2026, 3, 15), lab.getHardDeadline());

        Lab lab2 = new Lab("a", "b", 2, null, null);
        assertEquals("a", lab2.getId());
        assertEquals(2, lab2.getMaxScore());
    }

    @Test
    void studentGettersSetters() {
        Student s = new Student();
        s.setNick("n");
        s.setFullName("fn");
        s.setRepo("r");
        assertEquals("n", s.getNick());
        assertEquals("fn", s.getFullName());
        assertEquals("r", s.getRepo());
        Student s2 = new Student("x", "y", "z");
        assertEquals("z", s2.getRepo());
    }

    @Test
    void groupGettersSetters() {
        Group g = new Group();
        g.setName("42");
        assertEquals("42", g.getName());
        g.setStudents(new ArrayList<>());
        g.getStudents().add(new Student("a", "b", "c"));
        assertEquals(1, g.getStudents().size());

        Group g2 = new Group("x");
        assertEquals("x", g2.getName());
    }

    @Test
    void checkpointGettersSetters() {
        Checkpoint c = new Checkpoint();
        c.setName("КТ");
        c.setDate(LocalDate.of(2026, 5, 1));
        assertEquals("КТ", c.getName());
        assertEquals(LocalDate.of(2026, 5, 1), c.getDate());
        Checkpoint c2 = new Checkpoint("КТ2", LocalDate.of(2026, 6, 1));
        assertEquals("КТ2", c2.getName());
    }

    @Test
    void assignmentGettersSetters() {
        Assignment a = new Assignment();
        a.setNick("nick");
        a.setLabIds(Arrays.asList("1", "2"));
        assertEquals("nick", a.getNick());
        assertEquals(2, a.getLabIds().size());
        Assignment a2 = new Assignment("x", Arrays.asList("y"));
        assertEquals("x", a2.getNick());
    }

    @Test
    void settingsGettersAndBonus() {
        Settings s = new Settings();
        s.setTestTimeoutSeconds(42);
        s.setActivityThreshold(0.7);
        s.getGradeMinPercent().put(5, 90);
        s.getBonuses().put("nick::1", 3);
        assertEquals(42, s.getTestTimeoutSeconds());
        assertEquals(0.7, s.getActivityThreshold());
        assertEquals(3, s.getBonusFor("nick", "1"));
        assertEquals(0, s.getBonusFor("other", "1"));
        s.setGradeMinPercent(s.getGradeMinPercent());
        s.setBonuses(s.getBonuses());
        assertNotNull(s.getBonuses());
        assertNotNull(s.getGradeMinPercent());
    }

    @Test
    void configFinders() {
        Config cfg = new Config();
        Lab lab = new Lab("1", "l", 1, null, null);
        cfg.getLabs().add(lab);
        Group g = new Group("G");
        Student st = new Student("n", "fn", "r");
        g.getStudents().add(st);
        cfg.getGroups().add(g);
        cfg.getAssignments().add(new Assignment("n", Arrays.asList("1")));
        cfg.getCheckpoints().add(new Checkpoint("КТ", LocalDate.now()));

        assertEquals(lab, cfg.findLab("1"));
        assertNull(cfg.findLab("nope"));
        assertEquals(st, cfg.findStudent("n"));
        assertNull(cfg.findStudent("nope"));
        assertEquals(g, cfg.findGroupOf("n"));
        assertNull(cfg.findGroupOf("nope"));

        cfg.setSettings(new Settings());
        cfg.setLabs(cfg.getLabs());
        cfg.setGroups(cfg.getGroups());
        cfg.setAssignments(cfg.getAssignments());
        cfg.setCheckpoints(cfg.getCheckpoints());
        assertNotNull(cfg.getSettings());
        assertEquals(1, cfg.getAssignments().size());
        assertEquals(1, cfg.getCheckpoints().size());
    }

    @Test
    void labResultGettersSetters() {
        Lab lab = new Lab("1", "l", 1, null, null);
        Student s = new Student("n", "fn", "r");
        LabResult r = new LabResult(s, lab);
        r.setBuildOk(true);
        r.setDocOk(true);
        r.setStyleOk(false);
        r.setTestsPassed(5);
        r.setTestsFailed(1);
        r.setTestsSkipped(2);
        r.setBonus(1);
        r.setTotalScore(4);
        assertTrue(r.isBuildOk());
        assertTrue(r.isDocOk());
        assertEquals(false, r.isStyleOk());
        assertEquals(5, r.getTestsPassed());
        assertEquals(1, r.getTestsFailed());
        assertEquals(2, r.getTestsSkipped());
        assertEquals(1, r.getBonus());
        assertEquals(4, r.getTotalScore());
        assertEquals(s, r.getStudent());
        assertEquals(lab, r.getLab());
    }

    @Test
    void studentReportAggregates() {
        Student s = new Student("n", "fn", "r");
        StudentReport rep = new StudentReport(s);
        Lab lab1 = new Lab("1", "l1", 2, null, null);
        Lab lab2 = new Lab("2", "l2", 3, null, null);
        LabResult r1 = new LabResult(s, lab1);
        r1.setTotalScore(2);
        LabResult r2 = new LabResult(s, lab2);
        r2.setTotalScore(3);
        rep.getResults().add(r1);
        rep.getResults().add(r2);
        rep.setActivity(0.9);
        rep.setGrade("5");
        rep.getCheckpointGrades().put("КТ", "5");

        assertEquals(5, rep.totalScore());
        assertEquals(r1, rep.findResult("1"));
        assertNull(rep.findResult("nope"));
        assertEquals(0.9, rep.getActivity());
        assertEquals("5", rep.getGrade());
        assertEquals("5", rep.getCheckpointGrades().get("КТ"));
        assertEquals(s, rep.getStudent());
    }
}
