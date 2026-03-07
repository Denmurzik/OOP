package org.example;

/**
 * Ошибка загрузки конфигурации.
 */
public class ConfigLoadException extends RuntimeException {
    /**
     * Конструктор.
     *
     * @param message сообщение
     */
    public ConfigLoadException(String message) {
        super(message);
    }
}
