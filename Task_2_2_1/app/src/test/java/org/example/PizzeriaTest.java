package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты.
 */
class PizzeriaTest {

    @Test
    void testPizzeriaIntegration() throws InterruptedException {
        PizzeriaConfig config = new PizzeriaConfig();

        PizzeriaConfig.BakerConfig baker = new PizzeriaConfig.BakerConfig();
        baker.name = "Тест-пекарь";
        baker.cookingTimeMs = 100;
        config.bakers = new PizzeriaConfig.BakerConfig[] { baker };

        PizzeriaConfig.CourierConfig courier = new PizzeriaConfig.CourierConfig();
        courier.name = "Тест-курьер";
        courier.trunkCapacity = 5;
        config.couriers = new PizzeriaConfig.CourierConfig[] { courier };

        config.storageCapacity = 3;
        config.workingTimeMs = 5000;

        PizzeriaConfig.OrderConfig o1 = new PizzeriaConfig.OrderConfig();
        o1.id = 1;
        PizzeriaConfig.OrderConfig o2 = new PizzeriaConfig.OrderConfig();
        o2.id = 2;
        PizzeriaConfig.OrderConfig o3 = new PizzeriaConfig.OrderConfig();
        o3.id = 3;
        config.orders = new PizzeriaConfig.OrderConfig[] { o1, o2, o3 };

        Thread pizzeriaThread = new Thread(() -> {
            Pizzeria pizzeria = new Pizzeria(config);
            pizzeria.start();
        });
        pizzeriaThread.start();
        pizzeriaThread.join(15000);

        assertFalse(pizzeriaThread.isAlive(), "Пиццерия должна завершиться");
    }
}
