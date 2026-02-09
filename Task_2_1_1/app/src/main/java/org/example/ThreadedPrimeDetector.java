package org.example;

/**
 * многопоточная реализация.
 */
public class ThreadedPrimeDetector implements PrimeDetector {
    private final int threadCount;

    /**
     * создает многопоточный детектор с заданным количеством потоков.
     *
     * @param threadCount количество потоков
     */
    public ThreadedPrimeDetector(int threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public boolean hasNonPrime(int[] numbers) {
        if (numbers == null || numbers.length == 0) {
            return false;
        }

        ResultHolder resultHolder = new ResultHolder();

        Thread[] threads = new Thread[threadCount];
        int chunkSize = (numbers.length + threadCount - 1) / threadCount;

        for (int i = 0; i < threadCount; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, numbers.length);

            if (start >= numbers.length) {
                break;
            }

            threads[i] = new WorkerThread(numbers, start, end, resultHolder);
            threads[i].start();
        }

        for (Thread thread : threads) {
            if (thread != null) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread was interrupted", e);
                }
            }
        }

        return resultHolder.isNonPrimeFound();
    }

    /**
     * Класс для хранения результата проверки.
     */
    private static class ResultHolder {
        private boolean hasNonPrime = false;

        /**
         * Устанавливает флаг что найдено непростое число.
         */
        public synchronized void setNonPrimeFound() {
            hasNonPrime = true;
        }

        /**
         * Проверяет было ли найдено непростое число.
         */
        public synchronized boolean isNonPrimeFound() {
            return hasNonPrime;
        }
    }

    /**
     * рабочий поток для проверки части массива.
     */
    private class WorkerThread extends Thread {
        private final int[] numbers;
        private final int start;
        private final int end;
        private final ResultHolder resultHolder;

        public WorkerThread(int[] numbers, int start, int end, ResultHolder resultHolder) {
            this.numbers = numbers;
            this.start = start;
            this.end = end;
            this.resultHolder = resultHolder;
        }

        @Override
        public void run() {
            for (int i = start; i < end && !resultHolder.isNonPrimeFound(); i++) {
                if (!isPrime(numbers[i])) {
                    resultHolder.setNonPrimeFound();
                    return;
                }
            }
        }
    }
}
