package org.example.runner;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/** Работа с git через консольный клиент. */
public class GitClient {
    private static final List<String> DEFAULT_BRANCHES = List.of("main", "master");

    private final long timeoutSeconds;

    /** Создаёт клиент с таймаутом на каждую команду. */
    public GitClient(long timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    /** Результат подготовки локальной копии репозитория. */
    public static class RepoState {
        public final boolean available;
        public final String branch;
        public final String message;

        /** Создаёт состояние подготовленного репозитория. */
        public RepoState(boolean available, String branch, String message) {
            this.available = available;
            this.branch = branch;
            this.message = message;
        }
    }

    /** Клонирует/обновляет репозиторий и переключает его на main/master. */
    public RepoState prepareRepository(String repoUrl, File targetDir) {
        try {
            if (!new File(targetDir, ".git").exists()) {
                targetDir.getParentFile().mkdirs();
                ProcessHelper.Result clone = ProcessHelper.run(
                        targetDir.getParentFile(), timeoutSeconds,
                        "git", "clone", repoUrl, targetDir.getName());
                if (clone.exitCode != 0 || clone.timedOut) {
                    return new RepoState(false, null, "REPO_UNAVAILABLE");
                }
            }

            ProcessHelper.Result fetch = ProcessHelper.run(
                    targetDir, timeoutSeconds,
                    "git", "fetch", "--all", "--prune");
            if (fetch.exitCode != 0 || fetch.timedOut) {
                return new RepoState(false, null, "REPO_UNAVAILABLE");
            }

            String branch = resolveOfficialBranch(targetDir);
            if (branch == null) {
                return new RepoState(false, null, "BRANCH_NOT_FOUND");
            }

            ProcessHelper.Result checkout = ProcessHelper.run(
                    targetDir, timeoutSeconds,
                    "git", "checkout", "-B", branch, "origin/" + branch);
            if (checkout.exitCode != 0 || checkout.timedOut) {
                return new RepoState(false, null, "BRANCH_NOT_FOUND");
            }

            ProcessHelper.Result reset = ProcessHelper.run(
                    targetDir, timeoutSeconds,
                    "git", "reset", "--hard", "origin/" + branch);
            if (reset.exitCode != 0 || reset.timedOut) {
                return new RepoState(false, branch, "REPO_UNAVAILABLE");
            }

            ProcessHelper.Result clean = ProcessHelper.run(
                    targetDir, timeoutSeconds,
                    "git", "clean", "-fdx");
            if (clean.exitCode != 0 || clean.timedOut) {
                return new RepoState(false, branch, "REPO_UNAVAILABLE");
            }
            return new RepoState(true, branch, "OK");
        } catch (Exception e) {
            return new RepoState(false, null, "REPO_UNAVAILABLE");
        }
    }

    /** Возвращает поддерживаемую официальную ветку (main/master), либо null. */
    public String resolveOfficialBranch(File repoDir) {
        try {
            for (String b : DEFAULT_BRANCHES) {
                ProcessHelper.Result chk = ProcessHelper.run(
                        repoDir, timeoutSeconds,
                        "git", "rev-parse", "--verify", "origin/" + b);
                if (chk.exitCode == 0 && !chk.timedOut) {
                    return b;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /** Даты коммитов официальной ветки в диапазоне. */
    public List<LocalDate> commitDates(File repoDir, String branch, LocalDate from, LocalDate to) {
        List<LocalDate> list = new ArrayList<>();
        try {
            ProcessHelper.Result r = ProcessHelper.run(
                    repoDir, timeoutSeconds,
                    "git", "log",
                    branch,
                    "--since=" + from.toString(),
                    "--until=" + to.toString(),
                    "--format=%cI");
            if (r.exitCode != 0 || r.timedOut) {
                throw new GitOperationException(
                        "Не удалось получить историю коммитов для " + repoDir.getAbsolutePath());
            }
            for (String line : r.output.split("\n")) {
                if (!line.isBlank()) {
                    try {
                        list.add(ZonedDateTime.parse(line.trim()).toLocalDate());
                    } catch (Exception ignore) {
                        // некорректную дату пропускаем
                    }
                }
            }
        } catch (Exception e) {
            throw new GitOperationException(
                    "Не удалось получить историю коммитов для " + repoDir.getAbsolutePath(), e);
        }
        return list;
    }

    /** Последняя дата коммита в ветке, затронувшего указанный путь. */
    public LocalDate lastCommitDateForPath(File repoDir, String branch, File path) {
        try {
            String relativePath = relativeGitPath(repoDir, path);
            ProcessHelper.Result r = ProcessHelper.run(
                    repoDir, timeoutSeconds,
                    "git", "log",
                    "-1",
                    "--format=%cI",
                    branch,
                    "--",
                    relativePath);
            if (r.exitCode != 0 || r.timedOut || r.output.isBlank()) {
                return null;
            }
            return ZonedDateTime.parse(r.output.trim()).toLocalDate();
        } catch (Exception e) {
            return null;
        }
    }

    private String relativeGitPath(File repoDir, File path) {
        Path repo = repoDir.toPath().toAbsolutePath().normalize();
        Path target = path.toPath().toAbsolutePath().normalize();
        if (repo.equals(target)) {
            return ".";
        }
        return repo.relativize(target).toString().replace(File.separatorChar, '/');
    }
}
