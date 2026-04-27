package org.example.model;

import java.util.List;

/**
 * Снимок состояния враждебной змейки для отрисовки.
 */
public record EnemySnapshot(List<Point> segments, StrategyType type) {
}
