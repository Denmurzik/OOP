package org.example;

/**
 * Оценка.
 */
public enum Mark {
    EXCELLENT(5),
    GOOD(4),
    SATISFACTORY(3),
    PASS(1),
    FAIL(0);

    private final int value;

    Mark(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
