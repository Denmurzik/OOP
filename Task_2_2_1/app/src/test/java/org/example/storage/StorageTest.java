package org.example.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.example.exception.PizzeriaInterruptException;
import org.example.order.Order;
import org.junit.jupiter.api.Test;

class StorageTest {

    @Test
    void testPutAndSize() {
        Storage storage = new Storage(5);
        assertEquals(0, storage.size());
        storage.put(new Order(1));
        assertEquals(1, storage.size());
    }

    @Test
    void testTakeUpToReturnsAvailable() {
        Storage storage = new Storage(5);
        storage.put(new Order(1));
        storage.put(new Order(2));

        List<Order> taken = storage.takeUpTo(10);
        assertEquals(2, taken.size());
        assertEquals(0, storage.size());
    }

    @Test
    void testTakeUpToRespectsLimit() {
        Storage storage = new Storage(5);
        storage.put(new Order(1));
        storage.put(new Order(2));
        storage.put(new Order(3));

        List<Order> taken = storage.takeUpTo(2);
        assertEquals(2, taken.size());
        assertEquals(1, storage.size());
    }

    @Test
    void testCapacityBlocksWhenFull() throws InterruptedException {
        Storage storage = new Storage(1);
        storage.put(new Order(1));

        Thread putter = new Thread(() -> storage.put(new Order(2)));
        putter.start();
        Thread.sleep(200);
        assertTrue(putter.isAlive(), "Поток должен ждать склад полный");

        storage.takeUpTo(1);
        putter.join(2000);
        assertFalse(putter.isAlive());
        assertEquals(1, storage.size());
    }

    @Test
    void testShutdownWakesWaitingConsumer() throws InterruptedException {
        Storage storage = new Storage(5);

        Thread consumer = new Thread(() -> {
            List<Order> result = storage.takeUpTo(3);
            assertTrue(result.isEmpty());
        });
        consumer.start();
        Thread.sleep(200);

        storage.shutdown();
        consumer.join(2000);
        assertFalse(consumer.isAlive());
    }

    @Test
    void testIsClosedAfterShutdown() {
        Storage storage = new Storage(3);
        assertFalse(storage.isClosed());
        storage.shutdown();
        assertTrue(storage.isClosed());
    }

    @Test
    void testTakeUpToThrowsExceptionOnInterrupt() {
        Storage storage = new Storage(5);

        Thread.currentThread().interrupt();

        try {
            storage.takeUpTo(1);
        } catch (PizzeriaInterruptException e) {
            assertEquals("Storage: прерывание при ожидании пицц", e.getMessage());
        }
    }
}
