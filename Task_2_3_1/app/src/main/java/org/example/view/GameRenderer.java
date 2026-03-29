package org.example.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.example.model.Food;
import org.example.model.GameModel;
import org.example.model.GameState;
import org.example.model.Point;
import org.example.model.Snake;

import java.util.List;

/**
 * Отрисовка игры на Canvas.
 */
public class GameRenderer {
    private static final Color BACKGROUND = Color.web("#1a1a2e");
    private static final Color GRID_COLOR = Color.web("#16213e");
    private static final Color SNAKE_HEAD = Color.web("#00b894");
    private static final Color SNAKE_BODY = Color.web("#00cec9");
    private static final Color FOOD_COLOR = Color.web("#e17055");
    private static final Color OBSTACLE_COLOR = Color.web("#636e72");

    private final Canvas canvas;
    private final int cellSize;

    public GameRenderer(Canvas canvas, int cellSize) {
        this.canvas = canvas;
        this.cellSize = cellSize;
    }

    /**
     * Отрисовывает текущее состояние игры.
     */
    public void render(GameModel model) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        // Фон
        gc.setFill(BACKGROUND);
        gc.fillRect(0, 0, w, h);

        // Сетка
        gc.setStroke(GRID_COLOR);
        gc.setLineWidth(0.5);
        for (int x = 0; x <= model.getField().getWidth(); x++) {
            gc.strokeLine(x * cellSize, 0, x * cellSize, h);
        }
        for (int y = 0; y <= model.getField().getHeight(); y++) {
            gc.strokeLine(0, y * cellSize, w, y * cellSize);
        }

        // Препятствия
        gc.setFill(OBSTACLE_COLOR);
        for (Point p : model.getField().getObstacles()) {
            gc.fillRect(p.x() * cellSize, p.y() * cellSize, cellSize, cellSize);
        }

        // Еда
        gc.setFill(FOOD_COLOR);
        for (Food food : model.getFoods()) {
            Point p = food.getPosition();
            gc.fillOval(p.x() * cellSize + 2, p.y() * cellSize + 2,
                    cellSize - 4, cellSize - 4);
        }

        // Змейка
        Snake snake = model.getSnake();
        List<Point> segments = snake.getSegments();
        for (int i = 0; i < segments.size(); i++) {
            Point p = segments.get(i);
            gc.setFill(i == 0 ? SNAKE_HEAD : SNAKE_BODY);
            gc.fillRoundRect(p.x() * cellSize + 1, p.y() * cellSize + 1,
                    cellSize - 2, cellSize - 2, 6, 6);
        }

        // Оверлей при окончании игры
        if (model.getState() == GameState.GAME_OVER || model.getState() == GameState.WON) {
            gc.setFill(Color.rgb(0, 0, 0, 0.6));
            gc.fillRect(0, 0, w, h);

            gc.setFill(Color.WHITE);
            gc.setFont(new Font(36));
            gc.setTextAlign(TextAlignment.CENTER);
            String text = model.getState() == GameState.WON ? "ПОБЕДА!" : "ИГРА ОКОНЧЕНА";
            gc.fillText(text, w / 2, h / 2);

            gc.setFont(new Font(18));
            gc.fillText("Нажмите 'Новая игра'", w / 2, h / 2 + 40);
        }
    }
}
