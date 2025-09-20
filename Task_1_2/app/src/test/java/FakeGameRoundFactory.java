
public class FakeGameRoundFactory extends GameRoundFactory {
    private final GameResult resultToProduce;


    public FakeGameRoundFactory(GameResult result) {
        this.resultToProduce = result;
    }

    @Override
    public GameRound create(Player p, Dealer d, Deck deck, GamePrompter prompter, ConsoleView view) {
        return new FakeGameRound(resultToProduce);
    }
}