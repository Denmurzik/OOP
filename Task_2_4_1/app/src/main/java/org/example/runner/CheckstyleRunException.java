package org.example.runner;

/** Ошибка при запуске проверки стиля. */
public class CheckstyleRunException extends RunnerException {

    public CheckstyleRunException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckstyleRunException(String message) {
        super(message);
    }
}
