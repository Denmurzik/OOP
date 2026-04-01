package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    private GameModel model;

    @BeforeEach
    void setUp() {
        GameConfig config = new GameConfig(10, 10, 1, 5, 200);
        GameField field = new GameField(10, 10);
        WinCondition winCondition = new WinCondition() {
            @Override
            public boolean checkWin(Snake snake, int score) {
                return snake.size() >= 5;
            }
        };
        model = new GameModel(config, field, winCondition, new Random(42));
    }

    @Test
    void initialState() {
        assertEquals(GameState.RUNNING, model.getState());
        assertEquals(1, model.getSnake().size());
        assertEquals(0, model.getScore());
        assertEquals(1, model.getFoods().size());
    }

    @Test
    void snakeMovesOnTick() {
        Point headBefore = model.getSnake().getHead();
        model.tick();
        Point headAfter = model.getSnake().getHead();
        assertEquals(headBefore.getX() + 1, headAfter.getX());
        assertEquals(headBefore.getY(), headAfter.getY());
    }

    @Test
    void gameOverOnWallCollision() {
        // Двигаем змейку вправо до стены
        for (int i = 0; i < 20; i++) {
            model.tick();
            if (model.getState() == GameState.GAME_OVER) {
                break;
            }
        }
        assertEquals(GameState.GAME_OVER, model.getState());
    }

    @Test
    void gameOverOnSelfCollision() {
        // Маленькое поле 5x5, змейка наращивается вручную и врезается в себя
        GameConfig config = new GameConfig(5, 5, 0, 100, 200);
        GameField field = new GameField(5, 5);
        WinCondition win = new WinCondition() {
            @Override
            public boolean checkWin(Snake snake, int score) {
                return false;
            }
        };
        GameModel m = new GameModel(config, field, win, new Random(42));

        // Наращиваем змейку, формируя петлю
        Snake snake = m.getSnake(); // голова (2,2), направление RIGHT
        snake.grow(new Point(3, 2)); // [(3,2), (2,2)]
        snake.grow(new Point(3, 3)); // [(3,3), (3,2), (2,2)]
        snake.grow(new Point(2, 3)); // [(2,3), (3,3), (3,2), (2,2)]

        // Направление RIGHT, тик: голова (2,3) -> (3,3), а (3,3) уже в теле
        m.changeDirection(Direction.RIGHT);
        m.tick();

        assertEquals(GameState.GAME_OVER, m.getState());
    }

    @Test
    void directionChangeApplied() {
        model.changeDirection(Direction.UP);
        model.tick();
        Point head = model.getSnake().getHead();
        // Начальная позиция (5, 5), после UP -> (5, 4)
        assertEquals(4, head.getY());
    }

    @Test
    void pauseAndResume() {
        model.togglePause();
        assertEquals(GameState.PAUSED, model.getState());
        Point headBefore = model.getSnake().getHead();
        model.tick(); // не должен сработать на паузе
        assertEquals(headBefore, model.getSnake().getHead());
        model.togglePause();
        assertEquals(GameState.RUNNING, model.getState());
    }

    @Test
    void resetRestoresInitialState() {
        model.tick();
        model.tick();
        model.reset();
        assertEquals(GameState.RUNNING, model.getState());
        assertEquals(1, model.getSnake().size());
        assertEquals(0, model.getScore());
    }

    @Test
    void foodCountMaintained() {
        assertEquals(1, model.getFoods().size());
        // После нескольких тиков еда всё ещё есть
        for (int i = 0; i < 3; i++) {
            model.tick();
        }
        if (model.getState() == GameState.RUNNING) {
            assertTrue(model.getFoods().size() >= 1);
        }
    }
}
