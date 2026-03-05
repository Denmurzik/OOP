package org.example;

/**
 * Пекарь.
 */
public class Baker implements Runnable {
    private final String name;
    private final int cookingTimeMs;
    private final OrderQueue orderQueue;
    private final Storage storage;

    /**
     * Конструктор.
     *
     * @param name          имя
     * @param cookingTimeMs время
     * @param orderQueue    очередь
     * @param storage       склад
     */
    public Baker(String name, int cookingTimeMs, OrderQueue orderQueue, Storage storage) {
        this.name = name;
        this.cookingTimeMs = cookingTimeMs;
        this.orderQueue = orderQueue;
        this.storage = storage;
    }

    /**
     * Цикл.
     */
    @Override
    public void run() {
        while (true) {
            Order order = orderQueue.take();
            if (order == null) {
                break;
            }

            order.setState(OrderState.COOKING);
            System.out.println("  Пекарь " + name + " готовит заказ [" + order.getId() + "]");

            try {
                Thread.sleep(cookingTimeMs);
            } catch (InterruptedException e) {
                break;
            }

            storage.put(order);
            System.out.println("  Пекарь " + name + " положил заказ [" + order.getId() 
                + "] на склад");
        }
    }

    /**
     * Геттер имени.
     *
     * @return имя
     */
    public String getName() {
        return name;
    }
}
