package org.example.runner;

import java.util.LinkedHashSet;
import java.util.Set;
import org.example.model.Config;
import org.example.model.Group;
import org.example.model.Student;

/** Проверка, что git установлен и не будет просить пароль интерактивно. */
public class GitAuthCheck {
    private static final long REMOTE_TIMEOUT_SECONDS = 15;

    /** Проверяет, что git доступен. */
    public boolean check() {
        try {
            ProcessHelper.Result r = ProcessHelper.run(null, 10, "git", "--version");
            return r.exitCode == 0;
        } catch (Exception e) {
            return false;
        }
    }

    /** Проверяет, что обращения к удалённым репозиториям завершаются без зависания на аутентификации. */
    public boolean checkRemotes(Config config) {
        if (!check()) {
            return false;
        }
        Set<String> repos = new LinkedHashSet<>();
        for (Group group : config.getGroups()) {
            for (Student student : group.getStudents()) {
                repos.add(student.getRepo());
            }
        }
        for (String repo : repos) {
            if (!failsFast(repo)) {
                return false;
            }
        }
        return true;
    }

    private boolean failsFast(String repoUrl) {
        try {
            ProcessHelper.Result r = ProcessHelper.run(
                    null, REMOTE_TIMEOUT_SECONDS,
                    "git", "ls-remote", "--heads", repoUrl, "main", "master");
            return !r.timedOut;
        } catch (Exception e) {
            return false;
        }
    }
}
