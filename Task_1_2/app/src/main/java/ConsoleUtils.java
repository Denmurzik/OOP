/**
 * Утилитный класс для вывода всей информации о ходе игры в консоль.
 */
public class ConsoleUtils {

    /**
     * Выводит приветственное сообщение в начале игры.
     */
    public static void printWelcomeMessage() {
        System.out.println("Добро пожаловать в Блэкджек!");
    }

    /**
     * Выводит прощальное сообщение в конце игры.
     */
    public static void printGoodbyeMessage() {
        System.out.println("Спасибо за игру!");
    }

    /**
     * Печатает линию-разделитель для форматирования вывода.
     */
    public static void printSeparator() {
        System.out.println("--------------------");
    }

    /**
     * Отображает заголовок с номером текущего раунда.
     *
     * @param round Номер раунда.
     */
    public static void printRoundHeader(int round) {
        System.out.println("\nРаунд " + round);
    }

    /**
     * Спрашивает игрока, хочет ли он сыграть еще раз.
     */
    public static void promptPlayAgain() {
        System.out.print("Хотите сыграть еще раз? (1 - да, 0 - нет): ");
    }

    /**
     * Сообщает, что дилер раздал карты.
     */
    public static void printCardsDealt() {
        System.out.println("Дилер раздал карты");
    }

    /**
     * Показывает, какую карту вытянул игрок.
     *
     * @param card Вытянутая карта.
     */
    public static void printPlayerDrawnCard(Card card) {
        System.out.println("Вы открыли карту " + card);
    }

    /**
     * Объявляет о начале хода дилера.
     */
    public static void printDealerTurn() {
        System.out.println("\nХод дилера");
        printSeparator();
    }

    /**
     * Показывает скрытую карту дилера.
     *
     * @param card Скрытая карта.
     */
    public static void printDealerRevealsHiddenCard(Card card) {
        System.out.println("Дилер открывает закрытую карту " + card);
    }

    /**
     * Показывает, какую карту вытянул дилер.
     * @param card Вытянутая карта.
     */
    public static void printDealerDrawnCard(Card card) {
        System.out.println("Дилер открывает карту " + card);
    }

    /**
     * Отображает текущую руку и счет игрока.
     *
     * @param handDetails Строковое представление руки.
     * @param score Текущий счет.
     */
    public static void showPlayerHand(String handDetails, int score) {
        System.out.println("    Ваши карты: " + handDetails + " ⇒ " + score);
    }

    /**
     * Отображает текущую руку дилера.
     *
     * @param handDetails Строковое представление руки.
     */
    public static void showDealerHand(String handDetails) {
        System.out.println("    Карты дилера: " + handDetails);
    }

    /**
     * Сообщает о ничьей, когда у обоих участников блэкджек.
     */
    public static void printBlackjackPush() {
        System.out.println("Ничья! У обоих игроков блэкджек.");
    }

    /**
     * Сообщает о победе игрока с блэкджеком.
     */
    public static void printPlayerBlackjack() {
        System.out.println("Блэкджек! Вы выиграли раунд!");
    }

    /**
     * Сообщает о победе дилера с блэкджеком.
     */
    public static void printDealerBlackjack() {
        System.out.println("У дилера блэкджек. Вы проиграли раунд.");
    }

    /**
     * Сообщает, что у игрока перебор.
     */
    public static void printPlayerBust() {
        System.out.print("У вас перебор! Вы проиграли. ");
    }

    /**
     * Сообщает, что у дилера перебор.
     */
    public static void printDealerBust() {
        System.out.print("У дилера перебор! Вы выиграли! ");
    }

    /**
     * Сообщает о ничьей в раунде.
     */
    public static void printPush() {
        System.out.print("Ничья. ");
    }

    /**
     * Сообщает о победе игрока в раунде.
     */
    public static void printPlayerWinsRound() {
        System.out.print("Вы выиграли раунд! ");
    }

    /**
     * Сообщает о проигрыше игрока в раунде.
     */
    public static void printDealerWinsRound() {
        System.out.print("Вы проиграли раунд. ");
    }

    /**
     * Выводит общий счет побед игрока и дилера.
     *
     * @param playerWins Победы игрока.
     * @param dealerWins Победы дилера.
     */
    public static void printOverallScore(int playerWins, int dealerWins) {
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
    public static void printPlayerTurnHeader() {
        System.out.println("\nВаш ход");
        System.out.println("--------------------");
    }

    /**
     * Просит игрока сделать ход (взять карту или остановиться).
     */
    public static void promptPlayerAction() {
        System.out.print("Введите \"1\", чтобы взять карту, и \"0\", чтобы остановиться: ");
    }

    /**
     * Сообщает о неверном вводе данных.
     */
    public static void printInvalidInput() {
        System.out.print("\nНекорректный ввод. Пожалуйста, введите 1 или 0: ");
    }

    /**
     * Просит пользователя ввести количество колод для игры.
     */
    public static void promptNumberOfDecks() {
        System.out.print("Введите количество колод: ");
    }

    /**
     * Сообщает о неверном вводе количества колод.
     */
    public static void printInvalidNumberOfDecks() {
        System.out.print("Некорректный ввод. Пожалуйста, введите положительное число: ");
    }

}