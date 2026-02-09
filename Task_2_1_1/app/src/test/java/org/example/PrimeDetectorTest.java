package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


/**
 * тесты для всех реализаций PrimeDetector.
 */
class PrimeDetectorTest {

    /**
     * тест проверки простоты отдельных чисел.
     */
    @Test
    void testIsPrime() {
        PrimeDetector detector = new SequentialPrimeDetector();

        // простые числа
        assertTrue(detector.isPrime(2));
        assertTrue(detector.isPrime(3));
        assertTrue(detector.isPrime(5));
        assertTrue(detector.isPrime(7));
        assertTrue(detector.isPrime(13));
        assertTrue(detector.isPrime(6997901));

        // непростые числа
        assertFalse(detector.isPrime(1));
        assertFalse(detector.isPrime(4));
        assertFalse(detector.isPrime(6));
        assertFalse(detector.isPrime(8));
        assertFalse(detector.isPrime(9));
        assertFalse(detector.isPrime(100));

        // граничные случаи
        assertFalse(detector.isPrime(0));
        assertFalse(detector.isPrime(-5));
    }

    /**
     * тест примера 1 из задания: [6, 8, 7, 13, 5, 9, 4].
     */
    @Test
    void testExample1_Sequential() {
        int[] numbers = { 6, 8, 7, 13, 5, 9, 4 };
        PrimeDetector detector = new SequentialPrimeDetector();
        assertTrue(detector.hasNonPrime(numbers));
    }

    @Test
    void testExample1_Threaded() {
        int[] numbers = { 6, 8, 7, 13, 5, 9, 4 };
        PrimeDetector detector = new ThreadedPrimeDetector(4);
        assertTrue(detector.hasNonPrime(numbers));
    }

    @Test
    void testExample1_Stream() {
        int[] numbers = { 6, 8, 7, 13, 5, 9, 4 };
        PrimeDetector detector = new StreamPrimeDetector();
        assertTrue(detector.hasNonPrime(numbers));
    }

    /**
     * тест примера 2 из задания: все простые числа.
     */
    @Test
    void testExample2_Sequential() {
        int[] numbers = { 20319251, 6997901, 6997927, 6997937, 17858849,
                6997967, 6998009, 6998029, 6998039, 20165149,
                6998051, 6998053 };
        PrimeDetector detector = new SequentialPrimeDetector();
        assertFalse(detector.hasNonPrime(numbers));
    }

    @Test
    void testExample2_Threaded() {
        int[] numbers = { 20319251, 6997901, 6997927, 6997937, 17858849,
                6997967, 6998009, 6998029, 6998039, 20165149,
                6998051, 6998053 };
        PrimeDetector detector = new ThreadedPrimeDetector(4);
        assertFalse(detector.hasNonPrime(numbers));
    }

    @Test
    void testExample2_Stream() {
        int[] numbers = { 20319251, 6997901, 6997927, 6997937, 17858849,
                6997967, 6998009, 6998029, 6998039, 20165149,
                6998051, 6998053 };
        PrimeDetector detector = new StreamPrimeDetector();
        assertFalse(detector.hasNonPrime(numbers));
    }

    /**
     * тесты граничных случаев.
     */
    @Test
    void testEmptyArray() {
        int[] numbers = {};

        assertFalse(new SequentialPrimeDetector().hasNonPrime(numbers));
        assertFalse(new ThreadedPrimeDetector(4).hasNonPrime(numbers));
        assertFalse(new StreamPrimeDetector().hasNonPrime(numbers));
    }

    @Test
    void testSinglePrime() {
        int[] numbers = { 7 };

        assertFalse(new SequentialPrimeDetector().hasNonPrime(numbers));
        assertFalse(new ThreadedPrimeDetector(4).hasNonPrime(numbers));
        assertFalse(new StreamPrimeDetector().hasNonPrime(numbers));
    }

    @Test
    void testSingleNonPrime() {
        int[] numbers = { 4 };

        assertTrue(new SequentialPrimeDetector().hasNonPrime(numbers));
        assertTrue(new ThreadedPrimeDetector(4).hasNonPrime(numbers));
        assertTrue(new StreamPrimeDetector().hasNonPrime(numbers));
    }

    /**
     * тест согласованности всех реализаций на одинаковых данных.
     */
    @Test
    void testAllDetectorsAgree() {
        int[] testCases = {
                2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
                100, 101, 102, 103, 104, 997, 998, 999, 1000
        };

        PrimeDetector sequential = new SequentialPrimeDetector();
        PrimeDetector threaded = new ThreadedPrimeDetector(4);
        PrimeDetector stream = new StreamPrimeDetector();

        boolean result1 = sequential.hasNonPrime(testCases);
        boolean result2 = threaded.hasNonPrime(testCases);
        boolean result3 = stream.hasNonPrime(testCases);

        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }

    /**
     * тест многопоточности с различным количеством потоков.
     */
    @Test
    void testDifferentThreadCounts() {
        int[] numbers = { 6, 8, 7, 13, 5, 9, 4 };

        assertTrue(new ThreadedPrimeDetector(1).hasNonPrime(numbers));
        assertTrue(new ThreadedPrimeDetector(2).hasNonPrime(numbers));
        assertTrue(new ThreadedPrimeDetector(4).hasNonPrime(numbers));
        assertTrue(new ThreadedPrimeDetector(8).hasNonPrime(numbers));
    }
}
