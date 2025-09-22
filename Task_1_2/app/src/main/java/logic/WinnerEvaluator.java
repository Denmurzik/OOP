package logic;

import model.enums.GameResult;
import model.participant.Dealer;
import model.participant.Player;

/**
 * Утилитный класс для определения победителя в раунде Блэкджека.
 */
public class WinnerEvaluator {

    /**
     * Определяет победителя раунда на основе финальных очков игрока и дилера.
     *
     * @param player Игрок
     * @param dealer Дилер
     * @return model.enums.GameResult, указывающий на исход раунда (победа игрока, победа дилера или ничья).
     */
    public static GameResult determineWinner(Player player, Dealer dealer) {
        int playerScore = player.getScore();
        int dealerScore = dealer.getScore();

        if (player.isBusted()) {
            return GameResult.DEALER_WINS;
        }
        if (dealer.isBusted()) {
            return GameResult.PLAYER_WINS;
        }
        if (playerScore > dealerScore) {
            return GameResult.PLAYER_WINS;
        }
        if (dealerScore > playerScore) {
            return GameResult.DEALER_WINS;
        }
        return GameResult.PUSH; // Ничья
    }
}