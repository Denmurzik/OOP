package org.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BasicFoodTest {

    @Test
    void testBasicFoodProperties() {
        Point p = new Point(5, 5);
        BasicFood food = new BasicFood(p);
        
        assertEquals(p, food.getPosition());
        assertEquals(1, food.getGrowthValue());
    }
}
