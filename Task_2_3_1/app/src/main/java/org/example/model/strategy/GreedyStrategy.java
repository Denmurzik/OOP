package org.example.model.strategy;

import java.util.List;
import org.example.model.Direction;
import org.example.model.Food;
import org.example.model.GameSnapshot;
import org.example.model.Point;
import org.example.model.Snake;
import org.example.model.SnakeStrategy;

/**
 * Жадная стратегия: всегда движется к ближайшей еде.
 */
public class GreedyStrategy implements SnakeStrategy {

    @Override
    public Direction nextMove(Snake self, GameSnapshot world) {
        Point head = self.getHead();
        Point target = findNearestFood(head, world.foods());
        if (target == null) {
            return self.getDirection();
        }

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

    private Point findNearestFood(Point from, List<Food> foods) {
        Point nearest = null;
        int minDist = Integer.MAX_VALUE;
        for (Food food : foods) {
            int dist = StrategyUtils.manhattan(from, food.getPosition());
            if (dist < minDist) {
                minDist = dist;
                nearest = food.getPosition();
            }
        }
        return nearest;
    }

    @Override
    public String name() {
        return "Greedy";
    }
}
