package org.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Collections;
import org.junit.jupiter.api.Test;

class GameSnapshotTest {

    @Test
    void testSnapshotGetters() {
        GameField field = new GameField(10, 10);
        GameSnapshot snapshot = new GameSnapshot(
                Collections.emptyList(),
                Collections.emptyList(),
                GameState.RUNNING,
                150,
                8,
                field
        );

        assertSame(GameState.RUNNING, snapshot.getState());
        assertEquals(150, snapshot.getScore());
        assertEquals(8, snapshot.getSnakeSize());
        assertEquals(0, snapshot.getFoods().size());
        assertEquals(0, snapshot.getSnakeSegments().size());
        assertSame(field, snapshot.getField());
    }
}
