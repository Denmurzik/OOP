package org.example.runner;

/** Базовое исключение для ошибок запуска внешних проверок. */
public class RunnerException extends RuntimeException {

    public RunnerException(String message) {
        super(message);
    }

    public RunnerException(String message, Throwable cause) {
        super(message, cause);
    }
}
