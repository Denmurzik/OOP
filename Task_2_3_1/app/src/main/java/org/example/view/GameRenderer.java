package org.example.view;

import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
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

    private final Canvas canvas;
    private final int cellSize;

    public GameRenderer(Canvas canvas, int cellSize) {
        this.canvas = canvas;
        this.cellSize = cellSize;
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

        // Сетка
        gc.setStroke(GRID_COLOR);
        gc.setLineWidth(0.5);
        for (int x = 0; x <= snapshot.getField().getWidth(); x++) {
            gc.strokeLine(x * cellSize, 0, x * cellSize, h);
        }
        for (int y = 0; y <= snapshot.getField().getHeight(); y++) {
            gc.strokeLine(0, y * cellSize, w, y * cellSize);
        }

        // Препятствия
        gc.setFill(OBSTACLE_COLOR);
        for (Point p : snapshot.getField().getObstacles()) {
            gc.fillRect(p.getX() * cellSize, p.getY() * cellSize, cellSize, cellSize);
        }

        // Еда
        gc.setFill(FOOD_COLOR);
        for (Food food : snapshot.getFoods()) {
            Point p = food.getPosition();
            gc.fillOval(p.getX() * cellSize + 2, p.getY() * cellSize + 2,
                    cellSize - 4, cellSize - 4);
        }

        // Змейка
        List<Point> segments = snapshot.getSnakeSegments();
        for (int i = 0; i < segments.size(); i++) {
            Point p = segments.get(i);
            gc.setFill(i == 0 ? SNAKE_HEAD : SNAKE_BODY);
            gc.fillRoundRect(p.getX() * cellSize + 1, p.getY() * cellSize + 1,
                    cellSize - 2, cellSize - 2, 6, 6);
        }

        // Оверлей при окончании игры
        if (snapshot.getState() == GameState.GAME_OVER || snapshot.getState() == GameState.WON) {
            gc.setFill(Color.rgb(0, 0, 0, 0.6));
            gc.fillRect(0, 0, w, h);

            gc.setFill(Color.WHITE);
            gc.setFont(new Font(36));
            gc.setTextAlign(TextAlignment.CENTER);
            String text = snapshot.getState() == GameState.WON ? "ПОБЕДА!" : "ИГРА ОКОНЧЕНА";
            gc.fillText(text, w / 2, h / 2);

            gc.setFont(new Font(18));
            gc.fillText("Нажмите 'Новая игра'", w / 2, h / 2 + 40);
        }
    }
}
