package org.example;

/**
 * Последовательная реализация.
 */
public class SequentialPrimeDetector implements PrimeDetector {

    @Override
    public boolean hasNonPrime(int[] numbers) {
        for (int number : numbers) {
            if (!isPrime(number)) {
                return true;
            }
        }
        return false;
    }
}
