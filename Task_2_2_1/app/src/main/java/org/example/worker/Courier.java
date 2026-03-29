package org.example.worker;

import java.util.List;
import org.example.exception.PizzeriaInterruptException;
import org.example.order.Order;
import org.example.order.OrderState;
import org.example.storage.Storage;

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
                Thread.currentThread().interrupt();
                throw new PizzeriaInterruptException(
                        "Курьер " + name + ": прерван при доставке "
                                + pizzas.size() + " пицц",
                        e);
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
