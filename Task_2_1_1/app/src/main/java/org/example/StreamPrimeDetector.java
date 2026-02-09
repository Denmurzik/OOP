package org.example;

import java.util.Arrays;

/**
 * Параллельная реализация.
 */
public class StreamPrimeDetector implements PrimeDetector {

    @Override
    public boolean hasNonPrime(int[] numbers) {
        return Arrays.stream(numbers)
                .boxed()
                .toList()
                .parallelStream()
                .anyMatch(n -> !isPrime(n));
    }
}
