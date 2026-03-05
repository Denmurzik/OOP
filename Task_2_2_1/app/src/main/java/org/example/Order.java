package org.example;

/**
 * Заказ на пиццу.
 */
public class Order {
    private final int id;
    private OrderState state;

    /**
     * Конструктор заказ.
     *
     * @param id номер заказа
     */
    public Order(int id) {
        this.id = id;
    }

    /**
     * Геттер номера.
     *
     * @return номер заказа
     */
    public int getId() {
        return id;
    }

    /**
     * Состояние заказа.
     *
     * @return текущее состояние
     */
    public OrderState getState() {
        return state;
    }

    /**
     * Сеттер состояния.
     *
     * @param state новое состояние
     */
    public void setState(OrderState state) {
        this.state = state;
        System.out.println("[" + id + "] " + state);
    }
}
