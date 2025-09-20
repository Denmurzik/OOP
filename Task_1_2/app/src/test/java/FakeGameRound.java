/**
 * A fake implementation of GameRound for testing purposes.
 */
public class FakeGameRound extends GameRound {
    private final GameResult resultToReturn;

    /**
     * Constructor that accepts a predefined result to return when play() is called.
     *
     * @param result The predefined game result to return.
     */
    public FakeGameRound(GameResult result) {
        super(null, null, null, null, null);
        this.resultToReturn = result;
    }

    /**
     * Overrides the play method to return the predefined result.
     *
     * @return The predefined game result.
     */
    @Override
    public GameResult play() {
        return this.resultToReturn;
    }
}
