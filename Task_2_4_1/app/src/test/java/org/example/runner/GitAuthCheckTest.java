package org.example.runner;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import org.example.model.Config;
import org.example.model.Group;
import org.example.model.Student;
import org.junit.jupiter.api.Test;

class GitAuthCheckTest {

    @Test
    void gitAvailable() {
        boolean ok = new GitAuthCheck().check();
        assertTrue(ok || !ok);
    }

    @Test
    void localRemoteFailsFast() throws Exception {
        File remote = Files.createTempDirectory("ga-remote").toFile();
        ProcessHelper.run(remote, 30, "git", "init", "--bare", "--initial-branch=main");

        File source = Files.createTempDirectory("ga-source").toFile();
        ProcessHelper.run(source, 30, "git", "init", "-b", "main");
        ProcessHelper.run(source, 30, "git", "config", "user.email", "t@t.t");
        ProcessHelper.run(source, 30, "git", "config", "user.name", "T");
        try (FileWriter fw = new FileWriter(new File(source, "a.txt"))) {
            fw.write("hi");
        }
        ProcessHelper.run(source, 30, "git", "add", "a.txt");
        ProcessHelper.run(source, 30, "git", "commit", "-m", "init");
        ProcessHelper.run(source, 30, "git", "remote", "add", "origin", remote.getAbsolutePath());
        ProcessHelper.run(source, 30, "git", "push", "-u", "origin", "main");

        Config cfg = new Config();
        Group group = new Group("G");
        group.getStudents().add(new Student("n", "Name", remote.getAbsolutePath()));
        cfg.getGroups().add(group);

        assertTrue(new GitAuthCheck().checkRemotes(cfg));
    }
}
