import core.BlackjackGame;
import logic.GameRoundFactory;
import model.enums.GameResult;
import model.participant.Dealer;
import model.participant.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.ConsoleView;
import ui.GamePrompter;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BlackjackGameTest {

    private Player player;
    private Dealer dealer;
    private ConsoleView silentView;

    @BeforeEach
    void setUp() {
        // Готовим базовые объекты перед каждым тестом
        player = new Player();
        dealer = new Dealer();
        silentView = new SilentView();
    }

    @Test
    void startGameWhenRoundResultsInPlayerWin_shouldIncrementPlayerScore() {

        GamePrompter prompter = new TestPrompter(false);
        GameRoundFactory fakeFactory = new FakeGameRoundFactory(GameResult.PLAYER_WINS);

        BlackjackGame game = new BlackjackGame(player, dealer, prompter, silentView, fakeFactory);

        game.startGame();

        assertEquals(1, game.getPlayerWins(), "Счет побед игрока должен увеличиться на 1.");
        assertEquals(0, game.getDealerWins(), "Счет побед дилера не должен измениться.");
    }

    @Test
    void startGameWhenRoundResultsInDealerWin_shouldIncrementDealerScore() {
        GamePrompter prompter = new TestPrompter(false);
        GameRoundFactory fakeFactory = new FakeGameRoundFactory(GameResult.DEALER_WINS);

        BlackjackGame game = new BlackjackGame(player, dealer, prompter, silentView, fakeFactory);

        game.startGame();

        assertEquals(0, game.getPlayerWins(), "Счет побед игрока не должен измениться.");
        assertEquals(1, game.getDealerWins(), "Счет побед дилера должен увеличиться на 1.");
    }

    @Test
    void startGameWhenRoundResultsInPush_shouldNotChangeScores() {
        GamePrompter prompter = new TestPrompter(false);
        GameRoundFactory fakeFactory = new FakeGameRoundFactory(GameResult.PUSH);
        BlackjackGame game = new BlackjackGame(player, dealer, prompter, silentView, fakeFactory);

        game.startGame();

        assertEquals(0, game.getPlayerWins(), "Счет не должен меняться при ничьей.");
        assertEquals(0, game.getDealerWins(), "Счет не должен меняться при ничьей.");
    }
}