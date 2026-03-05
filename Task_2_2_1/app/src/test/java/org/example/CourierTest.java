package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class CourierTest {

    @Test
    void testCourierDeliversFromStorage() throws InterruptedException {
        Storage storage = new Storage(5);
        Order order = new Order(1);
        order.setState(OrderState.QUEUED);
        order.setState(OrderState.COOKING);
        storage.put(order); // Помещаем как готовую пиццу
        storage.shutdown(); // Курьер заберёт 1 и завершится

        Courier courier = new Courier("Тест", 3, storage);
        Thread thread = new Thread(courier);
        thread.start();
        thread.join(5000);

        assertFalse(thread.isAlive());
        assertEquals(0, storage.size());
        assertEquals(OrderState.DELIVERED, order.getState());
    }

    @Test
    void testCourierStopsOnEmptyShutdownStorage() throws InterruptedException {
        Storage storage = new Storage(5);
        storage.shutdown();

        Courier courier = new Courier("Тест", 2, storage);
        Thread thread = new Thread(courier);
        thread.start();
        thread.join(3000);

        assertFalse(thread.isAlive());
    }

    @Test
    void testCourierTakesUpToTrunkCapacity() throws InterruptedException {
        Storage storage = new Storage(10);
        Order o1 = new Order(1);
        Order o2 = new Order(2);
        Order o3 = new Order(3);
        storage.put(o1);
        storage.put(o2);
        storage.put(o3);
        storage.shutdown();

        // Багажник на 2 первый рейс возьмёт 2, второй 1
        Courier courier = new Courier("Тест", 2, storage);
        Thread thread = new Thread(courier);
        thread.start();
        thread.join(5000);

        assertFalse(thread.isAlive());
        assertEquals(0, storage.size());
        assertEquals(OrderState.DELIVERED, o1.getState());
        assertEquals(OrderState.DELIVERED, o2.getState());
        assertEquals(OrderState.DELIVERED, o3.getState());
    }

    @Test
    void testGetName() {
        Courier courier = new Courier("Алексей", 3, new Storage(1));
        assertEquals("Алексей", courier.getName());
    }
}
