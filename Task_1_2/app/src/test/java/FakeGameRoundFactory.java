import core.GameRound;
import logic.GameRoundFactory;
import model.Deck;
import model.enums.GameResult;
import model.participant.Dealer;
import model.participant.Player;
import ui.ConsoleView;
import ui.GamePrompter;

/**
 * Фабрика для создания фиктивных игровых раундов с предопределенным результатом.
 */
public class FakeGameRoundFactory extends GameRoundFactory {
    private final GameResult resultToProduce;

    /**
     * Конструктор, принимающий результат, который должен быть возвращен созданным раундом.
     *
     * @param result предопределенный результат для фиктивного раунда.
     */
    public FakeGameRoundFactory(GameResult result) {
        this.resultToProduce = result;
    }

    /**
     * Создает новый фиктивный игровой раунд, который всегда возвращает предопределенный результат.
     *
     * @param p Игрок
     * @param d Дилер
     * @param deck Колода карт
     * @param prompter Промптер для взаимодействия с игроком
     * @param view Консольный интерфейс для отображения информации
     *
     * @return Фиктивный игровой раунд с предопределенным результатом.
     */
    @Override
    public GameRound create(Player p, Dealer d, Deck deck,
                            GamePrompter prompter, ConsoleView view) {
        return new FakeGameRound(resultToProduce);
    }
}