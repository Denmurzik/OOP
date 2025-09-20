/**
 * Фабрика для создания фиктивных игровых раундов с предопределенным результатом.
 */
public class FakeGameRoundFactory extends GameRoundFactory {
    private final GameResult resultToProduce;

    /**
     * Конструктор, принимающий результат, который должен быть возвращен созданным раундом.
     *
     * @param result
     */
    public FakeGameRoundFactory(GameResult result) {
        this.resultToProduce = result;
    }

    /**
     * Создает новый фиктивный игровой раунд, который всегда возвращает предопределенный результат.
     *
     * @param p
     * @param d
     * @param deck
     * @param prompter
     * @param view
     *
     * @return
     */
    @Override
    public GameRound create(Player p, Dealer d, Deck deck,
                            GamePrompter prompter, ConsoleView view) {
        return new FakeGameRound(resultToProduce);
    }
}