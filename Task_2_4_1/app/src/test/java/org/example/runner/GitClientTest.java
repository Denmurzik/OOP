package org.example.runner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class GitClientTest {

    @Test
    void resolveOfficialBranchPrefersMain() throws Exception {
        File dir = Files.createTempDirectory("gc").toFile();
        ProcessHelper.run(dir, 30, "git", "init", "-b", "main");
        configureGitUser(dir);
        writeFile(new File(dir, "a.txt"), "hi");
        ProcessHelper.run(dir, 30, "git", "add", "a.txt");
        ProcessHelper.run(dir, 30, "git", "commit", "-m", "init");
        ProcessHelper.run(dir, 30, "git", "update-ref", "refs/remotes/origin/master", "HEAD");
        ProcessHelper.run(dir, 30, "git", "update-ref", "refs/remotes/origin/main", "HEAD");

        assertEquals("main", new GitClient(10).resolveOfficialBranch(dir));
    }

    @Test
    void resolveOfficialBranchReturnsNullWhenNoSupportedBranches() throws Exception {
        File dir = Files.createTempDirectory("gc").toFile();
        ProcessHelper.run(dir, 30, "git", "init", "-b", "develop");
        configureGitUser(dir);
        writeFile(new File(dir, "a.txt"), "hi");
        ProcessHelper.run(dir, 30, "git", "add", "a.txt");
        ProcessHelper.run(dir, 30, "git", "commit", "-m", "init");

        assertNull(new GitClient(10).resolveOfficialBranch(dir));
    }

    @Test
    void commitDatesInLocalRepo() throws Exception {
        File dir = Files.createTempDirectory("gc").toFile();
        ProcessHelper.run(dir, 30, "git", "init", "-b", "main");
        configureGitUser(dir);
        writeFile(new File(dir, "a.txt"), "hi");
        ProcessHelper.run(dir, 30, "git", "add", "a.txt");
        ProcessHelper.run(dir, 30, "git", "commit", "-m", "init");

        List<LocalDate> dates = new GitClient(30)
                .commitDates(
                        dir,
                        "main",
                        LocalDate.now().minusDays(1),
                        LocalDate.now().plusDays(1));
        assertNotNull(dates);
        assertTrue(dates.size() >= 1);
        assertEquals(LocalDate.now(), dates.get(0));
    }

    @Test
    void lastCommitDateForPathIgnoresUnrelatedCommits() throws Exception {
        File dir = Files.createTempDirectory("gc").toFile();
        ProcessHelper.run(dir, 30, "git", "init", "-b", "main");
        configureGitUser(dir);
        File labDir = new File(dir, "task_2_1_1");
        labDir.mkdirs();
        writeFile(new File(labDir, "Main.java"), "class Main {}\n");
        ProcessHelper.run(dir, 30, "git", "add", ".");
        commitWithDate(dir, LocalDate.now().minusDays(5));

        writeFile(new File(dir, "README.md"), "later\n");
        ProcessHelper.run(dir, 30, "git", "add", "README.md");
        commitWithDate(dir, LocalDate.now().minusDays(1));

        LocalDate lastDate = new GitClient(30).lastCommitDateForPath(dir, "main", labDir);
        assertEquals(LocalDate.now().minusDays(5), lastDate);
    }

    @Test
    void prepareRepositoryFailsOnInvalidUrl() throws Exception {
        File dir = Files.createTempDirectory("gc").toFile();
        File target = new File(dir, "target");
        GitClient.RepoState state = new GitClient(10).prepareRepository(
                "file:///no-such-repo-nowhere", target);
        assertEquals(false, state.available);
        assertEquals("REPO_UNAVAILABLE", state.message);
    }

    @Test
    void prepareRepositoryFailsWhenMainAndMasterAreMissing() throws Exception {
        File remote = createRemoteRepo("develop");
        File dir = Files.createTempDirectory("gc").toFile();
        File target = new File(dir, "target");

        GitClient.RepoState state = new GitClient(30).prepareRepository(
                remote.getAbsolutePath(), target);

        assertEquals(false, state.available);
        assertEquals("BRANCH_NOT_FOUND", state.message);
    }

    @Test
    void commitDatesThrowsOnNonGitDirectory() throws Exception {
        File dir = Files.createTempDirectory("gc").toFile();

        assertThrows(GitOperationException.class,
                () -> new GitClient(10).commitDates(
                        dir, "main", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1)));
    }

    private File createRemoteRepo(String branch) throws Exception {
        File remote = Files.createTempDirectory("gc-remote").toFile();
        ProcessHelper.run(remote, 30, "git", "init", "--bare", "--initial-branch=" + branch);

        File source = Files.createTempDirectory("gc-source").toFile();
        ProcessHelper.run(source, 30, "git", "init", "-b", branch);
        configureGitUser(source);
        writeFile(new File(source, "a.txt"), "hi");
        ProcessHelper.run(source, 30, "git", "add", "a.txt");
        ProcessHelper.run(source, 30, "git", "commit", "-m", "init");
        ProcessHelper.run(source, 30, "git", "remote", "add", "origin", remote.getAbsolutePath());
        ProcessHelper.run(source, 30, "git", "push", "-u", "origin", branch);
        return remote;
    }

    private void configureGitUser(File dir) throws Exception {
        ProcessHelper.run(dir, 30, "git", "config", "user.email", "t@t.t");
        ProcessHelper.run(dir, 30, "git", "config", "user.name", "T");
    }

    private void commitWithDate(File dir, LocalDate date) throws Exception {
        String isoDate = date + "T12:00:00+07:00";
        boolean windows = System.getProperty("os.name", "").toLowerCase().contains("win");
        if (windows) {
            ProcessHelper.run(dir, 30,
                    "cmd", "/c",
                    "set GIT_AUTHOR_DATE=" + isoDate
                            + "&& set GIT_COMMITTER_DATE=" + isoDate
                            + "&& git commit -m dated");
        } else {
            ProcessHelper.run(dir, 30,
                    "sh", "-c",
                    "GIT_AUTHOR_DATE='" + isoDate + "' "
                            + "GIT_COMMITTER_DATE='" + isoDate + "' "
                            + "git commit -m dated");
        }
    }

    private void writeFile(File file, String content) throws Exception {
        file.getParentFile().mkdirs();
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(content);
        }
    }
}
