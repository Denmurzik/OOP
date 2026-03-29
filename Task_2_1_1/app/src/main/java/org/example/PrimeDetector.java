package org.example;

/**
 * Интерфейс для детектора простых чисел.
 */
public interface PrimeDetector {

    /**
     * Проверяет есть ли в массиве хотя бы одно непростое число.
     *
     * @param numbers массив целых чисел для проверки
     * @return true, если есть хотя бы одно непростое число, иначе false
     */
    boolean hasNonPrime(int[] numbers);

    /**
     * Проверяет является ли число простым.
     *
     * @param n число для проверки
     * @return true простое, иначе false
     */
    default boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        if (n % 2 == 0) {
            return false;
        }

        // Проверяем нечетные делители до sqrt(n)
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
