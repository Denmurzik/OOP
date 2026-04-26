package org.example.model.strategy;

import java.util.List;
import org.example.model.Direction;
import org.example.model.GameSnapshot;
import org.example.model.Point;
import org.example.model.Snake;
import org.example.model.SnakeStrategy;

/**
 * Стратегия охотника: преследует голову игрока.
 * Если игрока нет переключается на ближайшую еду.
 */
public class HunterStrategy implements SnakeStrategy {

    private final GreedyStrategy fallback = new GreedyStrategy();

    @Override
    public Direction nextMove(Snake self, GameSnapshot world) {
        List<Point> playerSegments = world.snakeSegments();
        if (playerSegments == null || playerSegments.isEmpty()) {
            return fallback.nextMove(self, world);
        }
        Point target = playerSegments.get(0);  

        Point head = self.getHead();
        List<Direction> candidates = StrategyUtils.nonReverseDirections(self.getDirection());

        Direction best = self.getDirection();
        int bestDist = Integer.MAX_VALUE;
        for (Direction d : candidates) {
            Point next = head.move(d);
            if (!StrategyUtils.isCellSafe(next, world, self)) {
                continue;
            }
            int dist = StrategyUtils.manhattan(next, target);
            if (dist < bestDist) {
                bestDist = dist;
                best = d;
            }
        }
        return best;
    }

    @Override
    public String name() {
        return "Hunter";
    }
}
