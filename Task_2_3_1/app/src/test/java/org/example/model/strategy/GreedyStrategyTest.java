package org.example.model.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import org.example.model.BasicFood;
import org.example.model.Direction;
import org.example.model.Food;
import org.example.model.GameField;
import org.example.model.GameSnapshot;
import org.example.model.Point;
import org.example.model.Snake;
import org.junit.jupiter.api.Test;

class GreedyStrategyTest {

    @Test
    void movesTowardNearestFood() {
        GameField field = new GameField(10, 10);
        Snake self = new Snake(new Point(5, 5), Direction.UP);
        // Еда справа от змейки
        List<Food> foods = List.of(new BasicFood(new Point(8, 5)));
        GameSnapshot world = new GameSnapshot(
                List.of(),  // нет игрока
                foods,
                null, 0, 0,
                field,
                Collections.emptyList()
        );
        GreedyStrategy strategy = new GreedyStrategy();
        Direction d = strategy.nextMove(self, world);
        assertEquals(Direction.RIGHT, d);
    }

    @Test
    void noFoodKeepsCurrentDirection() {
        GameField field = new GameField(10, 10);
        Snake self = new Snake(new Point(5, 5), Direction.UP);
        GameSnapshot world = new GameSnapshot(
                List.of(),
                Collections.emptyList(),
                null, 0, 0,
                field,
                Collections.emptyList()
        );
        Direction d = new GreedyStrategy().nextMove(self, world);
        assertEquals(Direction.UP, d);
    }
}
