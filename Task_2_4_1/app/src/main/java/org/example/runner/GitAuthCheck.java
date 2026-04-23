package org.example.runner;

/** Проверка, что git установлен и не будет просить пароль интерактивно. */
public class GitAuthCheck {

    /** Проверяет, что git доступен. */
    public boolean check() {
        try {
            ProcessHelper.Result r = ProcessHelper.run(null, 10, "git", "--version");
            return r.exitCode == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
