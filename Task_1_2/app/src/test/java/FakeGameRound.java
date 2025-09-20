
public class FakeGameRound extends GameRound {
    private final GameResult resultToReturn;

    public FakeGameRound(GameResult result) {
        super(null, null, null, null, null);
        this.resultToReturn = result;
    }

    @Override
    public GameResult play() {
        return this.resultToReturn;
    }
}
