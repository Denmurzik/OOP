package org.example;

import java.util.LinkedList;

/**
 * Очередь заказов.
 */
public class OrderQueue {
    private final LinkedList<Order> queue = new LinkedList<>();
    private boolean closed = false;

    /**
     * Добавляет заказ в очередь.
     *
     * @param order заказ
     */
    public synchronized void put(Order order) {
        queue.addLast(order);
        notifyAll();
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
                return null;
            }
        }
        if (queue.isEmpty()) {
            return null;
        }
        return queue.removeFirst();
    }

    /**
     * Зкрывает очередь.
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
