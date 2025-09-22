import core.GameRound;
import logic.GameRoundFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameRoundFactoryTest {

    @Test
    void createShouldReturnNonNullInstanceOfGameRound() {
        GameRoundFactory factory = new GameRoundFactory();
        GameRound gameRound = factory.create(null, null,
                null, null, null);

        assertNotNull(gameRound, "Фабрика должна создавать не-null объект core.GameRound.");
        assertInstanceOf(GameRound.class, gameRound,
                "Созданный объект должен быть экземпляром core.GameRound.");
    }
}