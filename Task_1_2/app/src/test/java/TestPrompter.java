import java.util.Arrays;
import java.util.List;

/**
 * Имитация ввода пользователя.
 */
public class TestPrompter extends GamePrompter {
    private final List<PlayerAction> actionsToReturn;
    private final boolean shouldPlayAgain;
    private int actionIndex = 0;

    /**
     * Основной конструктор для полного контроля.
     *
     * @param playAgain Ответ на вопрос "играть ли снова?".
     * @param actions   Последовательность ходов игрока (HIT/STAND).
     */
    public TestPrompter(boolean playAgain, PlayerAction... actions) {
        // ИСПРАВЛЕНИЕ ЗДЕСЬ: передаем два null, один для Scanner, второй для ConsoleView
        super(null, null);
        this.shouldPlayAgain = playAgain;
        this.actionsToReturn = Arrays.asList(actions);
    }

    /**
     * Упрощенный конструктор для тестов, где важен только ответ "играть ли снова".
     */
    public TestPrompter(boolean playAgain) {
        this(playAgain, PlayerAction.STAND);
    }

    /**
     * Переопределенный метод: всегда возвращает заданное значение.
     */
    @Override
    public boolean askToPlayAgain() {
        return this.shouldPlayAgain;
    }

    /**
     * Переопределенный метод: возвращает следующее действие из списка.
     */
    @Override
    public PlayerAction askPlayerAction() {
        if (actionIndex < actionsToReturn.size()) {
            return actionsToReturn.get(actionIndex++);
        }
        return PlayerAction.STAND;
    }

    /**
     * Переопределенный метод: для простоты всегда возвращает 1.
     */
    @Override
    public int askForNumberOfDecks() {
        return 1;
    }
}