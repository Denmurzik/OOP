/**
 * Представляет дилера (компьютерного игрока) в игре Блэкджек.
 * Дилер следует строгим правилам: добирает карты, пока его счет меньше 17.
 */
public class Dealer extends Participant {

    /**
     * Создает нового дилера с именем по умолчанию.
     */
    public Dealer() {
        super();
    }

    /**
     * Возвращает строковое представление руки дилера.
     * Может отображать руку либо с одной скрытой картой (в начале раунда),
     * либо полностью, включая итоговый счет.
     *
     * @param hideFirstCard {@code true}, чтобы скрыть вторую карту дилера;
     * {@code false}, чтобы показать все карты и счет.
     * @return отформатированная строка с картами дилера
     */
    public String getHandAsString(boolean hideFirstCard) {
        if (hideFirstCard) {
            if (hand.getCards().isEmpty()) {
                return "[]";
            }
            return "[" + hand.getCards().get(0) + ", <закрытая карта>]";
        } else {
            return hand.toDetailedString() + " ⇒ " + hand.getScore();
        }
    }
}