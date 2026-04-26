package org.example.model.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import org.example.model.Direction;
import org.example.model.GameField;
import org.example.model.GameSnapshot;
import org.example.model.Point;
import org.example.model.Snake;
import org.junit.jupiter.api.Test;

class HunterStrategyTest {

    @Test
    void movesTowardPlayerHead() {
        GameField field = new GameField(10, 10);
        Snake self = new Snake(new Point(5, 5), Direction.UP);
        // Голова игрока слева
        List<Point> playerSegs = List.of(new Point(2, 5));
        GameSnapshot world = new GameSnapshot(
                playerSegs,
                Collections.emptyList(),
                null, 0, 1,
                field,
                Collections.emptyList()
        );
        Direction d = new HunterStrategy().nextMove(self, world);
        assertEquals(Direction.LEFT, d);
    }
}
