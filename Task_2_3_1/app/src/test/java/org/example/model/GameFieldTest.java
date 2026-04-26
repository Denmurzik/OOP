package org.example.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GameFieldTest {

    @Test
    void pointInsideBounds() {
        GameField field = new GameField(10, 10);
        assertTrue(field.isInBounds(new Point(0, 0)));
        assertTrue(field.isInBounds(new Point(9, 9)));
        assertTrue(field.isInBounds(new Point(5, 5)));
    }

    @Test
    void pointOutsideBounds() {
        GameField field = new GameField(10, 10);
        assertFalse(field.isInBounds(new Point(-1, 0)));
        assertFalse(field.isInBounds(new Point(0, -1)));
        assertFalse(field.isInBounds(new Point(10, 0)));
        assertFalse(field.isInBounds(new Point(0, 10)));
    }

    @Test
    void obstacleDetection() {
        Set<Point> obstacles = Set.of(new Point(3, 3), new Point(4, 4));
        GameField field = new GameField(10, 10, obstacles);
        assertTrue(field.isObstacle(new Point(3, 3)));
        assertTrue(field.isObstacle(new Point(4, 4)));
        assertFalse(field.isObstacle(new Point(5, 5)));
    }

    @Test
    void randomFreePointNotOnSnakeOrObstacle() {
        Set<Point> obstacles = Set.of(new Point(0, 0));
        GameField field = new GameField(2, 2, obstacles);
        Snake snake = new Snake(new Point(1, 0), Direction.RIGHT);
        Point free = field.getRandomFreePoint(List.of(snake), new ArrayList<>(), new Random(42));
        assertNotNull(free);
        assertNotEquals(new Point(0, 0), free);
        assertNotEquals(new Point(1, 0), free);
    }

    @Test
    void randomFreePointReturnsNullWhenNoSpace() {
        GameField field = new GameField(1, 1);
        Snake snake = new Snake(new Point(0, 0), Direction.RIGHT);
        Point free = field.getRandomFreePoint(List.of(snake), new ArrayList<>(), new Random());
        assertNull(free);
    }
}
