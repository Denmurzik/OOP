/**
 * Создаёт раунд игры.
 */
public class GameRoundFactory {
    public GameRound create(Player player, Dealer dealer, Deck deck, GamePrompter prompter, ConsoleView view) {
        return new GameRound(player, dealer, deck, prompter, view);
    }
}