package org.example.runner;

public class GitAuthCheck {

    /** Проверяет, что git доступен и не требует ввода пароля. */
    public boolean check() {
        try {
            ProcessHelper.Result r = ProcessHelper.run(null, 10, "git", "--version");
            return r.exitCode == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
