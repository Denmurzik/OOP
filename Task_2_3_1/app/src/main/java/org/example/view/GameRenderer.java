package org.example.view;

import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.example.model.EnemySnapshot;
import org.example.model.Food;
import org.example.model.GameSnapshot;
import org.example.model.GameState;
import org.example.model.Point;

/**
 * Отрисовка игры.
 */
public class GameRenderer {
    private static final Color BACKGROUND = Color.web("#ddd345ff");
    private static final Color GRID_COLOR = Color.web("#5e592bff");
    private static final Color SNAKE_HEAD = Color.web("#09b317ff");
    private static final Color SNAKE_BODY = Color.web("#00ce67ff");
    private static final Color FOOD_COLOR = Color.web("#e17055");
    private static final Color OBSTACLE_COLOR = Color.web("#327485ff");

    // Цвета врагов по стратегиям
    private static final Color GREEDY_COLOR = Color.web("#c0392b");   // красный
    private static final Color RANDOM_COLOR = Color.web("#8e44ad");   // фиолетовый
    private static final Color HUNTER_COLOR = Color.web("#2c3e50");   // тёмно-синий

    private final Canvas canvas;

    public GameRenderer(Canvas canvas) {
        this.canvas = canvas;
    }

    /**
     * Отрисовывает снимок состояния игры.
     */
    public void render(GameSnapshot snapshot) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        // Фон
        gc.setFill(BACKGROUND);
        gc.fillRect(0, 0, w, h);

        int fieldW = snapshot.field().getWidth();
        int fieldH = snapshot.field().getHeight();

        // Вычисляем размер клетки и смещение для центрирования
        double cellByWidth = w / fieldW;
        double cellByHeight = h / fieldH;
        double cellSize = Math.min(cellByWidth, cellByHeight);
        double offsetX = (w - cellSize * fieldW) / 2;
        double offsetY = (h - cellSize * fieldH) / 2;

        // Сетка
        gc.setStroke(GRID_COLOR);
        gc.setLineWidth(0.5);
        for (int x = 0; x <= fieldW; x++) {
            double px = offsetX + x * cellSize;
            gc.strokeLine(px, offsetY, px, offsetY + cellSize * fieldH);
        }
        for (int y = 0; y <= fieldH; y++) {
            double py = offsetY + y * cellSize;
            gc.strokeLine(offsetX, py, offsetX + cellSize * fieldW, py);
        }

        // Препятствия
        gc.setFill(OBSTACLE_COLOR);
        for (Point p : snapshot.field().getObstacles()) {
            gc.fillRect(offsetX + p.getX() * cellSize, offsetY + p.getY() * cellSize,
                    cellSize, cellSize);
        }

        // Еда
        gc.setFill(FOOD_COLOR);
        for (Food food : snapshot.foods()) {
            Point p = food.getPosition();
            gc.fillOval(offsetX + p.getX() * cellSize + 2, offsetY + p.getY() * cellSize + 2,
                    cellSize - 4, cellSize - 4);
        }

        // Враги (рисуются до игрока, чтобы игрок был сверху)
        for (EnemySnapshot enemy : snapshot.enemies()) {
            Color baseColor = colorForStrategy(enemy.strategyName());
            Color headColor = baseColor.darker();
            List<Point> enemySegs = enemy.segments();
            for (int i = 0; i < enemySegs.size(); i++) {
                Point p = enemySegs.get(i);
                gc.setFill(i == 0 ? headColor : baseColor);
                gc.fillRoundRect(offsetX + p.getX() * cellSize + 1,
                        offsetY + p.getY() * cellSize + 1,
                        cellSize - 2, cellSize - 2, 6, 6);
            }
        }

        // Змейка игрока
        List<Point> segments = snapshot.snakeSegments();
        for (int i = 0; i < segments.size(); i++) {
            Point p = segments.get(i);
            gc.setFill(i == 0 ? SNAKE_HEAD : SNAKE_BODY);
            gc.fillRoundRect(offsetX + p.getX() * cellSize + 1,
                    offsetY + p.getY() * cellSize + 1,
                    cellSize - 2, cellSize - 2, 6, 6);
        }

        // Оверлей при окончании игры
        if (snapshot.state() == GameState.GAME_OVER) {
            gc.setFill(Color.rgb(0, 0, 0, 0.7));
            gc.fillRect(0, 0, w, h);

            gc.setFill(Color.web("#e17055"));
            gc.setFont(new Font(48));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText("ПОРАЖЕНИЕ", w / 2, h / 2 - 20);

            gc.setFill(Color.WHITE);
            gc.setFont(new Font(24));
            gc.fillText("Ваш счёт: " + snapshot.score(), w / 2, h / 2 + 25);

            gc.setFont(new Font(16));
            gc.fillText("Нажмите 'Новая игра'", w / 2, h / 2 + 60);
        }
    }

    private Color colorForStrategy(String name) {
        switch (name) {
            case "Greedy":
                return GREEDY_COLOR;
            case "Random":
                return RANDOM_COLOR;
            case "Hunter":
                return HUNTER_COLOR;
            default:
                return Color.GRAY;
        }
    }
}
