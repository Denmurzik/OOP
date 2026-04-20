package org.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameModelTest {
    private GameModel model;
    private GameConfig config;
    private GameField field;
    private WinCondition winCondition;

    @BeforeEach
    void setUp() {
        config = new GameConfig(10, 10, 1, 5, 200);
        field = new GameField(10, 10);
        winCondition = new WinCondition() {
            @Override
            public boolean checkWin(Snake snake, int score) {
                return snake.size() >= 5;
            }
        };
        model = new GameModel(config, field, winCondition, new Random(0));
    }

    @Test
    void testGetters() {
        assertEquals(config, model.getConfig());
        assertEquals(field, model.getField());
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
        GameConfig config = new GameConfig(5, 5, 0, 100, 200);
        GameField field = new GameField(5, 5);
        WinCondition win = new WinCondition() {
            @Override
            public boolean checkWin(Snake snake, int score) {
                return false;
            }
        };
        GameModel m = new GameModel(config, field, win, new Random(42));


        Snake snake = m.getSnake();
        snake.grow(new Point(3, 2));
        snake.grow(new Point(3, 3));
        snake.grow(new Point(2, 3));

        m.changeDirection(Direction.RIGHT);
        m.tick();

        assertEquals(GameState.GAME_OVER, m.getState());
    }

    @Test
    void directionChangeApplied() {
        model.changeDirection(Direction.UP);
        model.tick();
        Point head = model.getSnake().getHead();
        assertEquals(4, head.getY());
    }

    @Test
    void pauseAndResume() {
        model.togglePause();
        assertEquals(GameState.PAUSED, model.getState());
        Point headBefore = model.getSnake().getHead();
        model.tick();
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
        for (int i = 0; i < 3; i++) {
            model.tick();
        }
        if (model.getState() == GameState.RUNNING) {
            assertTrue(model.getFoods().size() >= 1);
        }
    }

    @Test
    void testThreeArgConstructor() {
        GameModel m = new GameModel(config, field, winCondition);
        assertEquals(GameState.RUNNING, m.getState());
    }

    @Test
    void testGetSnapshot() {
        GameSnapshot snapshot = model.getSnapshot();
        assertEquals(model.getState(), snapshot.state());
        assertEquals(model.getScore(), snapshot.score());
        assertEquals(model.getFoods().size(), snapshot.foods().size());
        assertEquals(model.getSnake().size(), snapshot.snakeSize());
        assertEquals(model.getField(), snapshot.field());
    }

    @Test
    void testObstacleCollision() {
        GameField f = new GameField(10, 10, java.util.Set.of(new Point(6, 5)));
        GameModel m = new GameModel(config, f, winCondition, new Random(0));
        m.tick(); 
        assertEquals(GameState.GAME_OVER, m.getState());
    }

    @Test
    void testFoodWithLargeGrowth() {
        model.getFoods().clear();
        Food superFood = new Food(new Point(6, 5)) {
            @Override
            public int getGrowthValue() {
                return 3;
            }
        };
        model.getFoods().add(superFood);
        model.tick();
        assertEquals(4, model.getSnake().size()); 
        assertEquals(3, model.getScore());
    }
}
