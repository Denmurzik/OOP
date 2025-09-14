import java.util.Scanner;

/**
 * Главный класс, управляющий всей логикой и процессом игры в Блэкджек.
 */
public class BlackjackGame {
    private Player player;
    private Dealer dealer;
    private Deck deck;
    private Scanner scanner;

    private int playerWins;
    private int dealerWins;

    /**
     * Инициализирует новую игру, создавая игрока, дилера, колоду и сканер для ввода.
     */
    public BlackjackGame() {
        this.player = new Player();
        this.dealer = new Dealer();
        this.deck = new Deck();
        this.scanner = new Scanner(System.in);
        this.playerWins = 0;
        this.dealerWins = 0;
    }

    /**
     * Запускает основной игровой цикл, который продолжается до тех пор,
     * пока пользователь не решит закончить игру.
     */
    public void startGame() {
        System.out.println("Добро пожаловать в Блэкджек!");

        int round = 1;
        while (true) {
            System.out.println("\nРаунд " + round);
            playRound();

            System.out.println("--------------------");
            System.out.print("Хотите сыграть еще раз? (1 - да, 0 - нет): ");
            String choice = scanner.nextLine();
            if (!"1".equals(choice)) {
                break;
            }
            round++;
        }

        System.out.println("Спасибо за игру!");
        scanner.close();
    }

    /**
     * Содержит полную логику одного игрового раунда: от раздачи карт до определения победителя.
     */
    private void playRound() {
        deck.shuffle();
        player.clearHand();
        dealer.clearHand();

        player.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());
        player.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());

        System.out.println("Дилер раздал карты");
        showHands(true);

        boolean playerHasBlackjack = (player.getScore() == 21);
        boolean dealerHasBlackjack = (dealer.getScore() == 21);

        if (playerHasBlackjack || dealerHasBlackjack) {
            showHands(false);

            if (playerHasBlackjack && dealerHasBlackjack) {
                System.out.println("Ничья! У обоих игроков блэкджек.");
            } else if (playerHasBlackjack) {
                System.out.println("Блэкджек! Вы выиграли раунд!");
                playerWins++;
            } else {
                System.out.println("У дилера блэкджек. Вы проиграли раунд.");
                dealerWins++;
            }

            printScore(playerWins, dealerWins);

            return;
        }

        while (player.getScore() < 21 && player.wantsToHit(scanner)) {
            player.addCard(deck.dealCard());
            System.out.println("Вы открыли карту " + player.getHand().getCards().get(player.getHand().getCards().size() - 1));
            showHands(true);
        }

        if (!player.isBusted()) {
            System.out.println("\nХод дилера");
            System.out.println("--------------------");
            System.out.println("Дилер открывает закрытую карту " + dealer.getHand().getCards().get(dealer.getHand().getCards().size() - 1));
            showHands(false);

            while (dealer.getScore() < 17) {
                Card newCard = deck.dealCard();
                dealer.addCard(newCard);
                System.out.println("Дилер открывает карту " + newCard);
                showHands(false);
            }
        }
        determineWinner();
    }

    /**
     * Отображает в консоли текущие руки и очки игрока и дилера.
     *
     * @param hideDealerCard {@code true}, если нужно скрыть вторую карту дилера, иначе {@code false}
     */
    private void showHands(boolean hideDealerCard) {
        System.out.println("    Ваши карты: " + player.getHand().toDetailedString() + " ⇒ " + player.getScore());
        System.out.println("    Карты дилера: " + dealer.getHandAsString(hideDealerCard));
    }

    /**
     * Определяет и объявляет победителя раунда на основе финальных очков.
     * Этот метод вызывается, если в начале раунда не было натурального блэкджека.
     */
    private void determineWinner() {

        int playerScore = player.getScore();
        int dealerScore = dealer.getScore();

        if (player.isBusted()) {
            System.out.print("У вас перебор! Вы проиграли. ");
            dealerWins++;
        } else if (dealer.isBusted()) {
            System.out.print("У дилера перебор! Вы выиграли! ");
            playerWins++;
        } else if (playerScore == dealerScore) {
            System.out.print("Ничья. ");
        } else if (playerScore > dealerScore) {
            System.out.print("Вы выиграли раунд! ");
            playerWins++;
        } else {
            System.out.print("Вы проиграли раунд. ");
            dealerWins++;
        }

        printScore(playerWins, dealerWins);
    }

    /**
     * Выводит в консоль общий счет побед игрока и дилера.
     *
     * @param playerWins количество побед игрока
     * @param dealerWins количество побед дилера
     */
    private void printScore(int playerWins, int dealerWins) {
        if (playerWins > dealerWins) {
            System.out.println("Счёт " + playerWins + " : " + dealerWins + " в вашу пользу. ");
        } else if (dealerWins > playerWins) {
            System.out.println("Счёт " + playerWins + " : " + dealerWins + " в пользу дилера. ");
        } else {
            System.out.println("Счёт " + playerWins + " : " + dealerWins + " Ничья. ");
        }
    }
}