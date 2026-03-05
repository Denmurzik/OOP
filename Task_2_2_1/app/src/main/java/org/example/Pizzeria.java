package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Пиццерия.
 */
public class Pizzeria {
    private final OrderQueue orderQueue;
    private final Storage storage;
    private final List<Thread> bakerThreads;
    private final List<Thread> courierThreads;
    private final PizzeriaConfig config;

    /**
     * Конструктор пиццерии.
     *
     * @param config конфигурация пиццерии
     */
    public Pizzeria(PizzeriaConfig config) {
        this.config = config;
        this.orderQueue = new OrderQueue();
        this.storage = new Storage(config.storageCapacity);
        this.bakerThreads = new ArrayList<>();
        this.courierThreads = new ArrayList<>();
    }

    /**
     * Запускает пиццерии.
     */
    public void start() {
        // запуск пекарей
        for (PizzeriaConfig.BakerConfig bc : config.bakers) {
            Baker baker = new Baker(bc.name, bc.cookingTimeMs, orderQueue, storage);
            Thread thread = new Thread(baker, "Baker-" + bc.name);
            bakerThreads.add(thread);
            thread.start();
        }

        // запуск курьеров
        for (PizzeriaConfig.CourierConfig cc : config.couriers) {
            Courier courier = new Courier(cc.name, cc.trunkCapacity, storage);
            Thread thread = new Thread(courier, "Courier-" + cc.name);
            courierThreads.add(thread);
            thread.start();
        }

        // заказы в очередь
        for (PizzeriaConfig.OrderConfig oc : config.orders) {
            Order order = new Order(oc.id);
            order.setState(OrderState.QUEUED);
            orderQueue.put(order);
        }


        try {
            Thread.sleep(config.workingTimeMs);
        } catch (InterruptedException e) {
        }

        System.out.println("=== Пиццерия закрывается. Приём заказов остановлен. ===");
        shutdown();
    }

    /**
     * Завершает работу пиццерии.
     */
    private void shutdown() {
        //закрываем очередь
        orderQueue.shutdown();

        //ждём пекарей
        for (Thread t : bakerThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        }
        System.out.println("Все пекари завершили работу.");

        //закрываем склад
        storage.shutdown();

        //ждём  курьеров
        for (Thread t : courierThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        }
        System.out.println("Все курьеры завершили работу. Пиццерия закрыта.");
    }
}
