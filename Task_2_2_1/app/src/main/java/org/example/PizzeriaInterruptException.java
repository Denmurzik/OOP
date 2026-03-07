package org.example;

/**
 * Ошибка прерывания работы пиццерии.
 */
public class PizzeriaInterruptException extends RuntimeException {
    /**
     * Конструктор.
     *
     * @param message сообщение
     * @param cause   причина
     */
    public PizzeriaInterruptException(String message, Throwable cause) {
        super(message, cause);
    }
}
