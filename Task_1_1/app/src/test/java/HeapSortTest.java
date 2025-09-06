
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HeapSortTest {
    
    private HeapSort sorter;
    
    @BeforeEach
    void setUp() {
        sorter = new HeapSort();
    }


    @Test
    void SingleElementArray() {
        int[] testArray = {1};
        int[] answerArray = {1};
        sorter.sort(testArray);
        assertArrayEquals(answerArray, testArray);
    }

    @Test
    void ReverseSortedArray() {
        int[] testArray = {9, 8, 5, 2, 1};
        int[] answerArray = {1, 2, 5, 8, 9};
        sorter.sort(testArray);
        assertArrayEquals(answerArray, testArray);
    }


    @Test
    void SimpleArray() {
        int[] testArray = {5, 2, 8, 1, 9};
        int[] answerArray = {1, 2, 5, 8, 9};
        
        sorter.sort(testArray);
        
        assertArrayEquals(answerArray, testArray);
    }

    @Test
    void EmptyArray() {

        int[] testArray = {};
        int[] answerArray = {};
        
        sorter.sort(testArray);

        assertArrayEquals(answerArray, testArray);
    }

    @Test
    void SortedArray() {

        int[] testArray = {1, 2, 3, 4, 5};
        int[] answerArray = {1, 2, 3, 4, 5};

        sorter.sort(testArray);

        assertArrayEquals(answerArray, testArray);
    }

    @Test
    void WithDuplicates() {

        int[] testArray = {5, 2, 8, 2, 5, 1};
        int[] answerArray = {1, 2, 2, 5, 5, 8};

        sorter.sort(testArray);

        assertArrayEquals(answerArray, testArray);
    }


}