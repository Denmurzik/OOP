package org.example;

/**
 * Main.
 */
public class Main {
    /**
     * Запуск.
     *
     * @param args аргументы
     */
    public static void main(String[] args) {
        PizzeriaConfig config = PizzeriaConfig.load("/config.json");
        System.out.println("Пиццерия открывается!");
        System.out.println("Пекарей: " + config.bakers.length);
        System.out.println("Курьеров: " + config.couriers.length);
        System.out.println("Вместимость склада: " + config.storageCapacity);
        System.out.println("Время работы: " + config.workingTimeMs + " мс");
        System.out.println("Заказов: " + config.orders.length);
        System.out.println();

        Pizzeria pizzeria = new Pizzeria(config);
        pizzeria.start();
    }
}
