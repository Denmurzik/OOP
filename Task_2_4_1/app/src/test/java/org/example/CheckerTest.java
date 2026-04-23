package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.example.model.Assignment;
import org.example.model.Checkpoint;
import org.example.model.Config;
import org.example.model.Group;
import org.example.model.Lab;
import org.example.model.Student;
import org.example.model.StudentReport;
import org.junit.jupiter.api.Test;

class CheckerTest {

    @Test
    void runProducesReportsPerGroup() throws Exception {
        Config cfg = new Config();
        Lab lab = new Lab("2-1-1", "l", 1,
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 12, 30));
        cfg.getLabs().add(lab);
        Group g = new Group("G");
        g.getStudents().add(new Student("n", "ФИО", "file:///no-repo"));
        cfg.getGroups().add(g);
        cfg.getAssignments().add(new Assignment("n", List.of("2-1-1")));
        cfg.getCheckpoints().add(new Checkpoint("КТ", LocalDate.of(2026, 5, 1)));
        cfg.getSettings().getGradeMinPercent().put(5, 90);
        cfg.getSettings().getGradeMinPercent().put(2, 0);
        cfg.getSettings().setTestTimeoutSeconds(5);

        File work = Files.createTempDirectory("checker").toFile();
        Map<Group, List<StudentReport>> data = new Checker(cfg, work).run();

        assertNotNull(data);
        assertEquals(1, data.size());
        List<StudentReport> reports = data.values().iterator().next();
        assertEquals(1, reports.size());
        StudentReport r = reports.get(0);
        // Клон не удастся — значит всё по нулям
        assertEquals(0, r.totalScore());
        assertNotNull(r.getCheckpointGrades().get("КТ"));
    }

    @Test
    void runSkipsUnknownStudent() throws Exception {
        Config cfg = new Config();
        cfg.getLabs().add(new Lab("1", "l", 1, null, null));
        cfg.getAssignments().add(new Assignment("ghost", List.of("1")));
        File work = Files.createTempDirectory("checker").toFile();
        Map<Group, List<StudentReport>> data = new Checker(cfg, work).run();
        assertNotNull(data);
    }
}
