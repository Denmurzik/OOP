package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class BakerTest {

    @Test
    void testBakerCooksAndPutsToStorage() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Order order = new Order(1);
        order.setState(OrderState.QUEUED);
        queue.put(order);
        queue.shutdown(); // Пекарь обработает 1 заказ и завершится

        Storage storage = new Storage(5);
        Baker baker = new Baker("Тест", 100, queue, storage);
        Thread thread = new Thread(baker);
        thread.start();
        thread.join(3000);

        assertFalse(thread.isAlive());
        assertEquals(1, storage.size());
        assertEquals(OrderState.STORED, order.getState());
    }

    @Test
    void testBakerStopsOnEmptyShutdownQueue() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Storage storage = new Storage(5);
        queue.shutdown();

        Baker baker = new Baker("Тест", 100, queue, storage);
        Thread thread = new Thread(baker);
        thread.start();
        thread.join(2000);

        assertFalse(thread.isAlive());
        assertEquals(0, storage.size());
    }

    @Test
    void testBakerProcessesMultipleOrders() throws InterruptedException {
        OrderQueue queue = new OrderQueue();
        Storage storage = new Storage(5);

        Order o1 = new Order(1);
        Order o2 = new Order(2);
        queue.put(o1);
        queue.put(o2);
        queue.shutdown();

        Baker baker = new Baker("Тест", 50, queue, storage);
        Thread thread = new Thread(baker);
        thread.start();
        thread.join(3000);

        assertFalse(thread.isAlive());
        assertEquals(2, storage.size());
        assertEquals(OrderState.STORED, o1.getState());
        assertEquals(OrderState.STORED, o2.getState());
    }

    @Test
    void testGetName() {
        Baker baker = new Baker("Иван", 100, new OrderQueue(), new Storage(1));
        assertEquals("Иван", baker.getName());
    }

    @Test
    void testBakerThrowsExceptionOnInterrupt() {
        OrderQueue queue = new OrderQueue();
        Order order = new Order(1);
        order.setState(OrderState.QUEUED);
        queue.put(order);

        Storage storage = new Storage(5);
        Baker baker = new Baker("Иван", 10000, queue, storage);

        Thread.currentThread().interrupt();

        try {
            baker.run();
        } catch (PizzeriaInterruptException e) {
            assertEquals("Пекарь Иван: прерван при готовке заказа [1]", e.getMessage());
        }

        assertEquals(OrderState.COOKING, order.getState());
        assertEquals(0, storage.size());
    }
}
