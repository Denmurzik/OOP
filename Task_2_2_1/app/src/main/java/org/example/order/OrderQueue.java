package org.example.order;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.example.exception.PizzeriaInterruptException;

/**
 * Очередь заказов.
 */
public class OrderQueue {
    private final LinkedList<Order> queue = new LinkedList<>();
    private final int capacity;
    private boolean closed = false;

    /**
     * Безлимитная очередь.
     */
    public OrderQueue() {
        this(-1);
    }

    /**
     * Очередь с ограниченной вместимостью.
     *
     * @param capacity вместимость (-1  безлимитная)
     */
    public OrderQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Добавляет заказ в очередь.
     *
     * @param order заказ
     * @return true если заказ добавлен, false если очередь закрыта
     */
    public synchronized boolean put(Order order) {
        while (capacity > 0 && queue.size() >= capacity && !closed) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new PizzeriaInterruptException(
                        "OrderQueue: прерывание при добавлении заказа ["
                                + order.getId() + "]", e);
            }
        }
        if (closed) {
            return false;
        }
        queue.addLast(order);
        notifyAll();
        return true;
    }

    /**
     * Забирает заказ из очереди.
     *
     * @return заказ или null если очередь закрыта и пуста
     */
    public synchronized Order take() {
        while (queue.isEmpty() && !closed) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new PizzeriaInterruptException(
                        "OrderQueue: прерывание при ожидании заказа", e);
            }
        }
        if (queue.isEmpty()) {
            return null;
        }
        Order order = queue.removeFirst();
        notifyAll();
        return order;
    }

    /**
     * Забирает несколько заказов.
     *
     * @param maxCount максимум заказов
     * @return список заказов
     */
    public synchronized List<Order> takeUpTo(int maxCount) {
        while (queue.isEmpty() && !closed) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new PizzeriaInterruptException(
                        "OrderQueue: прерывание при ожидании заказов", e);
            }
        }
        if (queue.isEmpty()) {
            return new ArrayList<>();
        }
        int count = Math.min(maxCount, queue.size());
        List<Order> taken = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            taken.add(queue.removeFirst());
        }
        notifyAll();
        return taken;
    }

    /**
     * Закрывает очередь.
     */
    public synchronized void shutdown() {
        closed = true;
        notifyAll();
    }

    /**
     * Проверяет закрыта ли очередь.
     *
     * @return true если закрыта
     */
    public synchronized boolean isClosed() {
        return closed;
    }

    /**
     * Возвращает размер очереди.
     *
     * @return количество заказов
     */
    public synchronized int size() {
        return queue.size();
    }
}
