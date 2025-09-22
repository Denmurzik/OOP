package ui;

import model.Card;

/**
 * Утилитный класс для вывода всей информации о ходе игры в консоль.
 */
public class ConsoleView {

    /**
     * Выводит приветственное сообщение в начале игры.
     */
    public void printWelcomeMessage() {
        System.out.println("Добро пожаловать в Блэкджек!");
    }

    /**
     * Выводит прощальное сообщение в конце игры.
     */
    public void printGoodbyeMessage() {
        System.out.println("Спасибо за игру!");
    }

    /**
     * Печатает линию-разделитель для форматирования вывода.
     */
    public void printSeparator() {
        System.out.println("--------------------");
    }

    /**
     * Отображает заголовок с номером текущего раунда.
     *
     * @param round Номер раунда.
     */
    public void printRoundHeader(int round) {
        System.out.println("\nРаунд " + round);
    }

    /**
     * Спрашивает игрока, хочет ли он сыграть еще раз.
     */
    public void promptPlayAgain() {
        System.out.print("Хотите сыграть еще раз? (1 - да, 0 - нет): ");
    }

    /**
     * Сообщает, что дилер раздал карты.
     */
    public void printCardsDealt() {
        System.out.println("Дилер раздал карты");
    }

    /**
     * Показывает, какую карту вытянул игрок.
     *
     * @param card Вытянутая карта.
     */
    public void printPlayerDrawnCard(Card card) {
        System.out.println("Вы открыли карту " + card);
    }

    /**
     * Объявляет о начале хода дилера.
     */
    public void printDealerTurn() {
        System.out.println("\nХод дилера");
        printSeparator();
    }

    /**
     * Показывает скрытую карту дилера.
     *
     * @param card Скрытая карта.
     */
    public void printDealerRevealsHiddenCard(Card card) {
        System.out.println("Дилер открывает закрытую карту " + card);
    }

    /**
     * Показывает, какую карту вытянул дилер.
     *
     * @param card Вытянутая карта.
     */
    public void printDealerDrawnCard(Card card) {
        System.out.println("Дилер открывает карту " + card);
    }

    /**
     * Отображает текущую руку и счет игрока.
     *
     * @param handDetails Строковое представление руки.
     * @param score       Текущий счет.
     */
    public void showPlayerHand(String handDetails, int score) {
        System.out.println("    Ваши карты: " + handDetails + " ⇒ " + score);
    }

    /**
     * Отображает текущую руку дилера.
     *
     * @param handDetails Строковое представление руки.
     */
    public void showDealerHand(String handDetails) {
        System.out.println("    Карты дилера: " + handDetails);
    }

    /**
     * Сообщает о ничьей, когда у обоих участников блэкджек.
     */
    public void printBlackjackPush() {
        System.out.println("Ничья! У обоих игроков блэкджек.");
    }

    /**
     * Сообщает о победе игрока с блэкджеком.
     */
    public void printPlayerBlackjack() {
        System.out.println("Блэкджек! Вы выиграли раунд!");
    }

    /**
     * Сообщает о победе дилера с блэкджеком.
     */
    public void printDealerBlackjack() {
        System.out.println("У дилера блэкджек. Вы проиграли раунд.");
    }

    /**
     * Сообщает, что у игрока перебор.
     */
    public void printPlayerBust() {
        System.out.print("У вас перебор! Вы проиграли. ");
    }

    /**
     * Сообщает, что у дилера перебор.
     */
    public void printDealerBust() {
        System.out.print("У дилера перебор! Вы выиграли! ");
    }

    /**
     * Сообщает о ничьей в раунде.
     */
    public void printPush() {
        System.out.print("Ничья. ");
    }

    /**
     * Сообщает о победе игрока в раунде.
     */
    public void printPlayerWinsRound() {
        System.out.print("Вы выиграли раунд! ");
    }

    /**
     * Сообщает о проигрыше игрока в раунде.
     */
    public void printDealerWinsRound() {
        System.out.print("Вы проиграли раунд. ");
    }

    /**
     * Выводит общий счет побед игрока и дилера.
     *
     * @param playerWins Победы игрока.
     * @param dealerWins Победы дилера.
     */
    public void printOverallScore(int playerWins, int dealerWins) {
        if (playerWins > dealerWins) {
            System.out.println("Счёт " + playerWins + " : "
                    + dealerWins + " в вашу пользу. ");
        } else if (dealerWins > playerWins) {
            System.out.println("Счёт " + playerWins + " : "
                    + dealerWins + " в пользу дилера. ");
        } else {
            System.out.println("Счёт " + playerWins + " : "
                    + dealerWins + " Ничья. ");
        }
    }

    /**
     * Отображает заголовок для хода игрока.
     */
    public void printPlayerTurnHeader() {
        System.out.println("\nВаш ход");
        System.out.println("--------------------");
    }

    /**
     * Просит игрока сделать ход (взять карту или остановиться).
     */
    public void promptPlayerAction() {
        System.out.print("Введите \"1\", чтобы взять карту, и \"0\", чтобы остановиться: ");
    }

    /**
     * Сообщает о неверном вводе данных.
     */
    public void printInvalidInput() {
        System.out.print("\nНекорректный ввод. Пожалуйста, введите 1 или 0: ");
    }

    /**
     * Просит пользователя ввести количество колод для игры.
     */
    public void promptNumberOfDecks() {
        System.out.print("Введите количество колод: ");
    }

    /**
     * Сообщает о неверном вводе количества колод.
     */
    public void printInvalidNumberOfDecks() {
        System.out.print("Некорректный ввод. Пожалуйста, введите положительное число: ");
    }

}