package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SnakeTest {

    @Test
    void snakeStartsWithOneSegment() {
        Snake snake = new Snake(new Point(5, 5), Direction.RIGHT);
        assertEquals(1, snake.size());
        assertEquals(new Point(5, 5), snake.getHead());
    }

    @Test
    void moveKeepsSameLength() {
        Snake snake = new Snake(new Point(5, 5), Direction.RIGHT);
        snake.grow(new Point(6, 5));
        snake.grow(new Point(7, 5));
        assertEquals(3, snake.size());

        snake.move(new Point(8, 5));
        assertEquals(3, snake.size());
        assertEquals(new Point(8, 5), snake.getHead());
    }

    @Test
    void growIncreasesLength() {
        Snake snake = new Snake(new Point(5, 5), Direction.RIGHT);
        snake.grow(new Point(6, 5));
        assertEquals(2, snake.size());
        assertEquals(new Point(6, 5), snake.getHead());
        assertEquals(new Point(5, 5), snake.getTail());
    }

    @Test
    void containsDetectsOccupiedCells() {
        Snake snake = new Snake(new Point(5, 5), Direction.RIGHT);
        snake.grow(new Point(6, 5));
        assertTrue(snake.contains(new Point(5, 5)));
        assertTrue(snake.contains(new Point(6, 5)));
        assertFalse(snake.contains(new Point(7, 5)));
    }

    @Test
    void cannotReverseDirection() {
        Snake snake = new Snake(new Point(5, 5), Direction.RIGHT);
        snake.setDirection(Direction.LEFT);
        assertEquals(Direction.RIGHT, snake.getDirection());
    }

    @Test
    void canChangeToPerpendicularDirection() {
        Snake snake = new Snake(new Point(5, 5), Direction.RIGHT);
        snake.setDirection(Direction.UP);
        assertEquals(Direction.UP, snake.getDirection());
    }

}
