package org.example.model.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.example.model.Direction;
import org.example.model.GameSnapshot;
import org.example.model.Point;
import org.example.model.Snake;
import org.example.model.SnakeStrategy;

/**
 * Стратегия случайного движения. Выбирает случайное безопасное направление.
 */
public class RandomStrategy implements SnakeStrategy {
    private final Random random;

    public RandomStrategy() {
        this(new Random());
    }

    public RandomStrategy(Random random) {
        this.random = random;
    }

    @Override
    public Direction nextMove(Snake self, GameSnapshot world) {
        List<Direction> candidates = StrategyUtils.nonReverseDirections(self.getDirection());

        List<Direction> safe = new ArrayList<>();
        for (Direction d : candidates) {
            Point next = self.getHead().move(d);
            if (StrategyUtils.isCellSafe(next, world, self)) {
                safe.add(d);
            }
        }

        if (safe.isEmpty()) {
            // Некуда идти продолжаем в текущем направлении
            return self.getDirection();
        }
        return safe.get(random.nextInt(safe.size()));
    }

    @Override
    public String name() {
        return "Random";
    }
}
