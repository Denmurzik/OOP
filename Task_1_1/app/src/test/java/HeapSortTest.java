import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Тестовый класс для проверки алгоритма пирамидальной сортировки HeapSort.
 */
class HeapSortTest {

    /**
     * Проверяет сортировку массива из одного элемента.
     */
    @Test
    void shouldHandleSingleElementArray() {
        int[] testArray = {1};
        int[] answerArray = {1};
        HeapSort.sort(testArray);
        assertArrayEquals(answerArray, testArray);
    }

    /**
     * Проверяет сортировку массива, отсортированного в обратном порядке.
     */
    @Test
    void shouldSortReverseSortedArray() {
        int[] testArray = {985345, 76, 9, 8, 4, 5, 2, 1};
        int[] answerArray = {1, 2, 4, 5, 8, 9, 76, 985345};
        HeapSort.sort(testArray);
        assertArrayEquals(answerArray, testArray);
    }

    /**
     * Проверяет сортировку простого массива с элементами в случайном порядке.
     */
    @Test
    void shouldSortSimpleArray() {
        int[] testArray = {5, 2, 8, 1, 9};
        int[] answerArray = {1, 2, 5, 8, 9};
        HeapSort.sort(testArray);
        assertArrayEquals(answerArray, testArray);
    }

    /**
     * Проверяет корректную обработку пустого массива.
     */
    @Test
    void shouldHandleEmptyArray() {
        int[] testArray = {};
        int[] answerArray = {};
        HeapSort.sort(testArray);
        assertArrayEquals(answerArray, testArray);
    }

    /**
     * Проверяет корректную обработку уже отсортированного массива.
     */
    @Test
    void shouldHandleAlreadySortedArray() {
        int[] testArray = {1, 2, 3, 4, 5};
        int[] answerArray = {1, 2, 3, 4, 5};
        HeapSort.sort(testArray);
        assertArrayEquals(answerArray, testArray);
    }

    /**
     * Проверяет сортировку массива с дублирующимися элементами.
     */
    @Test
    void shouldSortArrayWithDuplicates() {
        int[] testArray = {23, 5, 2, 8, 2, 5, 1, 23};
        int[] answerArray = {1, 2, 2, 5, 5, 8, 23, 23};
        HeapSort.sort(testArray);
        assertArrayEquals(answerArray, testArray);
    }
}