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
        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            if (ex instanceof ConfigLoadException) {
                System.err.println("Ошибка конфигурации " + ex.getMessage());
            } else if (ex instanceof PizzeriaInterruptException) {
                System.err.println(thread.getName() + " Прерывание " + ex.getMessage());
            } else {
                System.err.println(thread.getName() + " Ошибка " + ex.getMessage());
            }
        });

        try {
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
        } catch (ConfigLoadException e) {
            System.err.println("Ошибка конфигурации " + e.getMessage());
        } catch (PizzeriaInterruptException e) {
            System.err.println("Прерывание работы " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Какая-то ошибка " + e.getMessage());
        }
    }
}
