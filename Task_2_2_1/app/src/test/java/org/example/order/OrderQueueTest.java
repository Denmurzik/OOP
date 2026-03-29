package org.example.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class OrderQueueTest {

    @Test
    void testPutAndTakeOrder() {
        OrderQueue queue = new OrderQueue();
        Order order = new Order(1);
        queue.put(order);
        assertSame(order, queue.take());
    }

    @Test
    void testFifoOrder() {
        OrderQueue queue = new OrderQueue();
        Order o1 = new Order(1);
        Order o2 = new Order(2);
        Order o3 = new Order(3);
        queue.put(o1);
        queue.put(o2);
        queue.put(o3);

        assertSame(o1, queue.take());
        assertSame(o2, queue.take());
        assertSame(o3, queue.take());
    }

    @Test
    void testSize() {
        OrderQueue queue = new OrderQueue();
        assertEquals(0, queue.size());
        queue.put(new Order(1));
        assertEquals(1, queue.size());
        queue.put(new Order(2));
        assertEquals(2, queue.size());
        queue.take();
        assertEquals(1, queue.size());
    }

    @Test
    void testShutdownReturnNullOnEmpty() {
        OrderQueue queue = new OrderQueue();
        queue.shutdown();
        assertTrue(queue.isClosed());
        assertNull(queue.take());
    }

    @Test
    void testShutdownAfterPut() {
        OrderQueue queue = new OrderQueue();
        queue.put(new Order(1));
        queue.shutdown();
        // можно забрать оставшийся заказ даже после shutdown
        assertNotNull(queue.take());
        assertNull(queue.take());
    }

    @Test
    void testShutdownWakesWaitingThread() throws InterruptedException {
        OrderQueue queue = new OrderQueue();

        Thread consumer = new Thread(() -> {
            Order result = queue.take();
            assertNull(result);
        });
        consumer.start();
        Thread.sleep(200);

        queue.shutdown();
        consumer.join(2000);
        assertFalse(consumer.isAlive());
    }
}
