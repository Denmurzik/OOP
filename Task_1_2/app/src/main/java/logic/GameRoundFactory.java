package logic;

import core.GameRound;
import model.Deck;
import model.participant.Dealer;
import model.participant.Player;
import ui.ConsoleView;
import ui.GamePrompter;

/**
 * Создаёт раунд игры.
 */
public class GameRoundFactory {
    public GameRound create(Player player, Dealer dealer, Deck deck,
                            GamePrompter prompter, ConsoleView view) {
        return new GameRound(player, dealer, deck, prompter, view);
    }
}