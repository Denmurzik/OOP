package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Склад готовых пицц.
 */
public class Storage {
    private final List<Order> pizzas = new ArrayList<>();
    private final int capacity;
    private boolean closed = false;

    /**
     * Конструктор склада.
     *
     * @param capacity вместимость склада
     */
    public Storage(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Кладёт пиццу на склад.
     *
     * @param order готовый заказ
     */
    public synchronized void put(Order order) {
        while (pizzas.size() >= capacity && !closed) {
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }
        }
        if (closed) {
            return;
        }
        pizzas.add(order);
        order.setState(OrderState.STORED);
        notifyAll();
    }

    /**
     * Забирает пиццы.
     *
     * @param maxCount максимум пицц
     * @return список заказов
     */
    public synchronized List<Order> takeUpTo(int maxCount) {
        while (pizzas.isEmpty() && !closed) {
            try {
                wait();
            } catch (InterruptedException e) {
                return new ArrayList<>();
            }
        }
        if (pizzas.isEmpty()) {
            return new ArrayList<>();
        }
        int count = Math.min(maxCount, pizzas.size());
        List<Order> taken = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            taken.add(pizzas.remove(0));
        }
        notifyAll();
        return taken;
    }

    /**
     * Закрыть склад.
     */
    public synchronized void shutdown() {
        closed = true;
        notifyAll();
    }

    /**
     * Колво пицц.
     *
     * @return размер склада
     */
    public synchronized int size() {
        return pizzas.size();
    }

    /**
     * Закрыт ли склад.
     *
     * @return true если закрыт
     */
    public synchronized boolean isClosed() {
        return closed;
    }
}
