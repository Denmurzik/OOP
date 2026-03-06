package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class PizzeriaConfigTest {

    @Test
    void testLoadConfigNotNull() {
        PizzeriaConfig config = PizzeriaConfig.load("/config.json");
        assertNotNull(config);
    }

    @Test
    void testBakersCount() {
        PizzeriaConfig config = PizzeriaConfig.load("/config.json");
        assertEquals(4, config.bakers.length);
    }

    @Test
    void testFirstBakerName() {
        PizzeriaConfig config = PizzeriaConfig.load("/config.json");
        assertEquals("Константин Ивлев", config.bakers[0].name);
    }

    @Test
    void testFirstBakerCookingTime() {
        PizzeriaConfig config = PizzeriaConfig.load("/config.json");
        assertEquals(2000, config.bakers[0].cookingTimeMs);
    }

    @Test
    void testCouriersCount() {
        PizzeriaConfig config = PizzeriaConfig.load("/config.json");
        assertEquals(3, config.couriers.length);
    }

    @Test
    void testFirstCourierName() {
        PizzeriaConfig config = PizzeriaConfig.load("/config.json");
        assertEquals("Еда", config.couriers[0].name);
    }

    @Test
    void testFirstCourierTrunkCapacity() {
        PizzeriaConfig config = PizzeriaConfig.load("/config.json");
        assertEquals(2, config.couriers[0].trunkCapacity);
    }

    @Test
    void testStorageCapacity() {
        PizzeriaConfig config = PizzeriaConfig.load("/config.json");
        assertEquals(8, config.storageCapacity);
    }

    @Test
    void testWorkingTime() {
        PizzeriaConfig config = PizzeriaConfig.load("/config.json");
        assertEquals(30000, config.workingTimeMs);
    }

    @Test
    void testOrdersCount() {
        PizzeriaConfig config = PizzeriaConfig.load("/config.json");
        assertEquals(20, config.orders.length);
    }

    @Test
    void testFirstOrderId() {
        PizzeriaConfig config = PizzeriaConfig.load("/config.json");
        assertEquals(1, config.orders[0].id);
    }

    @Test
    void testLastOrderId() {
        PizzeriaConfig config = PizzeriaConfig.load("/config.json");
        assertEquals(20, config.orders[19].id);
    }

    @Test
    void testLoadMissingFileThrows() {
        assertThrows(RuntimeException.class, () -> {
            PizzeriaConfig.load("/not_exists.json");
        });
    }
}
