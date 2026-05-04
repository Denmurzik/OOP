package org.example.runner;

/** Ошибка выполнения git-команды. */
public class GitOperationException extends RunnerException {

    public GitOperationException(String message) {
        super(message);
    }

    public GitOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
