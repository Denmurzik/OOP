package org.example.storage;

import java.util.List;
import org.example.order.Order;
import org.example.order.OrderQueue;
import org.example.order.OrderState;

/**
 * Склад готовых пицц.
 */
public class Storage {
    private final OrderQueue queue;

    /**
     * Конструктор склада.
     *
     * @param capacity вместимость склада
     */
    public Storage(int capacity) {
        this.queue = new OrderQueue(capacity);
    }

    /**
     * Кладёт пиццу на склад.
     *
     * @param order готовый заказ
     */
    public void put(Order order) {
        if (queue.put(order)) {
            order.setState(OrderState.STORED);
        }
    }

    /**
     * Забирает пиццы.
     *
     * @param maxCount максимум пицц
     * @return список заказов
     */
    public List<Order> takeUpTo(int maxCount) {
        return queue.takeUpTo(maxCount);
    }

    /**
     * Закрыть склад.
     */
    public void shutdown() {
        queue.shutdown();
    }

    /**
     * Колво пицц.
     *
     * @return размер склада
     */
    public int size() {
        return queue.size();
    }

    /**
     * Закрыт ли склад.
     *
     * @return true если закрыт
     */
    public boolean isClosed() {
        return queue.isClosed();
    }
}
