package org.example.report;

import org.example.model.Config;
import org.example.model.Group;
import org.example.model.Lab;
import org.example.model.LabResult;
import org.example.model.Student;
import org.example.model.StudentReport;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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

        StudentReport rep = new StudentReport(s1);
        LabResult r = new LabResult(s1, lab);
        r.setBuildOk(true);
        r.setDocOk(true);
        r.setStyleOk(true);
        r.setTestsPassed(10);
        r.setTotalScore(1);
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
        assertTrue(html.contains("80%"));
        assertTrue(html.contains(">5<"));
    }
}
