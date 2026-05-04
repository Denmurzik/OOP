package org.example.runner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class GitClientTest {

    @Test
    void defaultBranchOnNonGitDir() throws Exception {
        File dir = Files.createTempDirectory("gc").toFile();
        String b = new GitClient(10).defaultBranch(dir);
        // В пустой папке веток нет
        assertEquals(null, b);
    }

    @Test
    void commitDatesInLocalRepo() throws Exception {
        File dir = Files.createTempDirectory("gc").toFile();
        ProcessHelper.run(dir, 30, "git", "init", "-b", "main");
        ProcessHelper.run(dir, 30, "git", "config", "user.email", "t@t.t");
        ProcessHelper.run(dir, 30, "git", "config", "user.name", "T");
        File f = new File(dir, "a.txt");
        FileWriter fw = new FileWriter(f);
        fw.write("hi");
        fw.close();
        ProcessHelper.run(dir, 30, "git", "add", "a.txt");
        ProcessHelper.run(dir, 30, "git", "commit", "-m", "init");

        List<String> dates = new GitClient(30)
                .commitDates(dir, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        assertNotNull(dates);
        assertEquals(true, dates.size() >= 1);
    }

    @Test
    void cloneOrUpdateFailsOnInvalidUrl() throws Exception {
        File dir = Files.createTempDirectory("gc").toFile();
        File target = new File(dir, "target");
        boolean ok = new GitClient(10).cloneOrUpdate("file:///no-such-repo-nowhere", target);
        assertEquals(false, ok);
    }

    @Test
    void commitDatesThrowsOnNonGitDirectory() throws Exception {
        File dir = Files.createTempDirectory("gc").toFile();

        assertThrows(GitOperationException.class,
                () -> new GitClient(10).commitDates(
                        dir, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1)));
    }
}
