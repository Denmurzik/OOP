package org.example;

import java.util.List;

/**
 * Курьер.
 */
public class Courier implements Runnable {
    private final String name;
    private final int trunkCapacity;
    private final Storage storage;

    private static final int DELIVERY_TIME_MS = 1500;

    /**
     * Конструктор курьера.
     *
     * @param name          имя
     * @param trunkCapacity багажник
     * @param storage       склад
     */
    public Courier(String name, int trunkCapacity, Storage storage) {
        this.name = name;
        this.trunkCapacity = trunkCapacity;
        this.storage = storage;
    }

    /**
     * Цикл .
     */
    @Override
    public void run() {
        while (true) {
            List<Order> pizzas = storage.takeUpTo(trunkCapacity);
            if (pizzas.isEmpty()) {
                break;
            }

            for (Order order : pizzas) {
                order.setState(OrderState.DELIVERING);
            }

            System.out.println("  Курьер " + name + " доставляет " + pizzas.size() + " пицц(у)");

            try {
                Thread.sleep(DELIVERY_TIME_MS);
            } catch (InterruptedException e) {
                break;
            }

            for (Order order : pizzas) {
                order.setState(OrderState.DELIVERED);
            }
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
