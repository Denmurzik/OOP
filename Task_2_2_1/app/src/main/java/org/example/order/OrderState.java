package org.example.order;

/**
 * Состояния заказа.
 */
public enum OrderState {
    QUEUED,
    COOKING,
    STORED,
    DELIVERING,
    DELIVERED
}
