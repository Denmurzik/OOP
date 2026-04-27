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
                field,
                Collections.emptyList()
        );

        assertSame(GameState.RUNNING, snapshot.state());
        assertEquals(150, snapshot.score());
        assertEquals(8, snapshot.snakeSize());
        assertEquals(0, snapshot.foods().size());
        assertEquals(0, snapshot.snakeSegments().size());
        assertSame(field, snapshot.field());
    }
}
