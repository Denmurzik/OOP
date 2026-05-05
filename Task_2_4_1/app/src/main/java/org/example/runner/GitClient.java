package org.example.runner;

import java.io.File;
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

    /** Клонирует репозиторий в targetDir или обновляет его до origin/HEAD. */
    public boolean cloneOrUpdate(String repoUrl, File targetDir) {
        try {
            if (new File(targetDir, ".git").exists()) {
                ProcessHelper.Result fetch = ProcessHelper.run(
                        targetDir, timeoutSeconds,
                        "git", "fetch", "--all", "--prune");
                if (fetch.exitCode != 0) {
                    return false;
                }
                String branch = defaultBranch(targetDir);
                if (branch == null) {
                    return false;
                }
                ProcessHelper.Result reset = ProcessHelper.run(
                        targetDir, timeoutSeconds,
                        "git", "reset", "--hard", "origin/" + branch);
                return reset.exitCode == 0;
            } else {
                targetDir.getParentFile().mkdirs();
                ProcessHelper.Result clone = ProcessHelper.run(
                        targetDir.getParentFile(), timeoutSeconds,
                        "git", "clone", repoUrl, targetDir.getName());
                return clone.exitCode == 0;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /** Возвращает имя ветки origin/HEAD (main или master), или null. */
    public String defaultBranch(File repoDir) {
        try {
            ProcessHelper.Result r = ProcessHelper.run(
                    repoDir, timeoutSeconds,
                    "git", "symbolic-ref", "refs/remotes/origin/HEAD");
            if (r.exitCode == 0) {
                String out = r.output.trim();
                int idx = out.lastIndexOf('/');
                if (idx >= 0) {
                    return out.substring(idx + 1);
                }
            }
            for (String b : DEFAULT_BRANCHES) {
                ProcessHelper.Result chk = ProcessHelper.run(
                        repoDir, timeoutSeconds,
                        "git", "rev-parse", "--verify", "origin/" + b);
                if (chk.exitCode == 0) {
                    return b;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /** Даты коммитов в диапазоне. */
    public List<LocalDate> commitDates(File repoDir, LocalDate from, LocalDate to) {
        List<LocalDate> list = new ArrayList<>();
        try {
            ProcessHelper.Result r = ProcessHelper.run(
                    repoDir, timeoutSeconds,
                    "git", "log",
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
}
