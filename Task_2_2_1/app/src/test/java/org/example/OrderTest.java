package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testNewOrderHasNoState() {
        Order order = new Order(1);
        assertNull(order.getState());
    }

    @Test
    void testGetId() {
        Order order = new Order(42);
        assertEquals(42, order.getId());
    }

    @Test
    void testSetStateUpdatesState() {
        Order order = new Order(1);
        order.setState(OrderState.QUEUED);
        assertEquals(OrderState.QUEUED, order.getState());
    }

    @Test
    void testStateTransitions() {
        Order order = new Order(1);
        order.setState(OrderState.QUEUED);
        order.setState(OrderState.COOKING);
        order.setState(OrderState.STORED);
        order.setState(OrderState.DELIVERING);
        order.setState(OrderState.DELIVERED);
        assertEquals(OrderState.DELIVERED, order.getState());
    }
}
