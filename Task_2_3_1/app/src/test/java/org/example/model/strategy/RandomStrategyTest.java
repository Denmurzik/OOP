package org.example.model.strategy;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.example.model.Direction;
import org.example.model.GameField;
import org.example.model.GameSnapshot;
import org.example.model.Point;
import org.example.model.Snake;
import org.junit.jupiter.api.Test;

class RandomStrategyTest {

    @Test
    void returnsValidDirection() {
        GameField field = new GameField(10, 10);
        Snake self = new Snake(new Point(5, 5), Direction.UP);
        GameSnapshot world = new GameSnapshot(
                List.of(),
                Collections.emptyList(),
                null, 0, 0,
                field,
                Collections.emptyList()
        );
        RandomStrategy strategy = new RandomStrategy(new Random(42));
        Direction d = strategy.nextMove(self, world);
        // Не должна быть противоположной текущей
        assertNotEquals(Direction.DOWN, d);
        assertTrue(d == Direction.UP || d == Direction.LEFT || d == Direction.RIGHT);
    }
}
