import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class heapsortTest {

    @Test
    void SingleElementArray() {
        int[] testArray = {1};
        int[] answerArray = {1};
        heapsort.sort(testArray);
        assertArrayEquals(answerArray, testArray);
    }

    @Test
    void ReverseSortedArray() {
        int[] testArray = {985345, 76, 9, 8, 4, 5, 2, 1};
        int[] answerArray = {1, 2, 4, 5, 8, 9, 76, 985345,};
        heapsort.sort(testArray);
        assertArrayEquals(answerArray, testArray);
    }


    @Test
    void SimpleArray() {
        int[] testArray = {5, 2, 8, 1, 9};
        int[] answerArray = {1, 2, 5, 8, 9};
        
        heapsort.sort(testArray);
        
        assertArrayEquals(answerArray, testArray);
    }

    @Test
    void EmptyArray() {

        int[] testArray = {};
        int[] answerArray = {};
        
        heapsort.sort(testArray);

        assertArrayEquals(answerArray, testArray);
    }

    @Test
    void SortedArray() {

        int[] testArray = {1, 2, 3, 4, 5};
        int[] answerArray = {1, 2, 3, 4, 5};

        heapsort.sort(testArray);

        assertArrayEquals(answerArray, testArray);
    }

    @Test
    void WithDuplicates() {

        int[] testArray = {23, 5, 2, 8, 2, 5, 1, 23};
        int[] answerArray = {1, 2, 2, 5, 5, 8, 23, 23};

        heapsort.sort(testArray);

        assertArrayEquals(answerArray, testArray);
    }


}