package org.example.report;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.example.model.Checkpoint;
import org.example.model.Config;
import org.example.model.Group;
import org.example.model.Lab;
import org.example.model.LabResult;
import org.example.model.Student;
import org.example.model.StudentReport;
import org.junit.jupiter.api.Test;

class HtmlReporterTest {

    @Test
    void htmlContainsGroupAndTables() {
        Config cfg = new Config();
        Lab lab = new Lab("2-1-1", "Простые числа", 1, null, null);
        cfg.getLabs().add(lab);
        Group group = new Group("12345");
        Student s1 = new Student("s1", "Студент №1", "u");
        group.getStudents().add(s1);
        cfg.getGroups().add(group);
        LabResult r = new LabResult(s1, lab);
        r.setBuildOk(true);
        r.setDocOk(true);
        r.setStyleOk(true);
        r.setTestsPassed(10);
        r.setSubmissionDate(LocalDate.of(2026, 3, 10));
        r.setStatus("OK");
        r.setTotalScore(1);
        StudentReport rep = new StudentReport(s1);
        rep.getResults().add(r);
        rep.setActivity(0.8);
        rep.setGrade("5");
        List<StudentReport> list = new ArrayList<>();
        list.add(rep);
        Map<Group, List<StudentReport>> data = new LinkedHashMap<>();
        data.put(group, list);

        String html = new HtmlReporter().build(cfg, data);

        assertTrue(html.contains("Группа 12345"));
        assertTrue(html.contains("Лабораторная 2-1-1"));
        assertTrue(html.contains("Простые числа"));
        assertTrue(html.contains("Студент №1"));
        assertTrue(html.contains("10/0/0"));
        assertTrue(html.contains("2026-03-10"));
        assertTrue(html.contains("OK"));
        assertTrue(html.contains("80%"));
        assertTrue(html.contains(">5<"));
    }

    @Test
    void htmlWithCheckpointsAndNullValues() {
        Config cfg = new Config();
        Lab lab = new Lab("2-1-1", "<Простые>", 1, null, null);
        cfg.getLabs().add(lab);
        cfg.getCheckpoints().add(new Checkpoint("КТ1", LocalDate.of(2026, 4, 1)));
        Group group = new Group("101");
        Student s = new Student("x", null, "u");
        group.getStudents().add(s);
        cfg.getGroups().add(group);

        StudentReport rep = new StudentReport(s);
        LabResult result = new LabResult(s, lab);
        result.setStatus("REPO_UNAVAILABLE");
        rep.getResults().add(result);
        rep.setGrade("3");
        List<StudentReport> list = new ArrayList<>();
        list.add(rep);
        Map<Group, List<StudentReport>> data = new LinkedHashMap<>();
        data.put(group, list);

        String html = new HtmlReporter().build(cfg, data);
        assertTrue(html.contains("Оценки по контрольным точкам"));
        assertTrue(html.contains("&lt;Простые&gt;"));
        assertTrue(html.contains("КТ1"));
        assertTrue(html.contains("REPO_UNAVAILABLE"));
    }

    @Test
    void htmlSkipsEmptyGroups() {
        Config cfg = new Config();
        Group group = new Group("empty");
        cfg.getGroups().add(group);
        Map<Group, List<StudentReport>> data = new LinkedHashMap<>();
        data.put(group, new ArrayList<>());

        String html = new HtmlReporter().build(cfg, data);
        assertTrue(!html.contains("Группа empty"));
    }
}
