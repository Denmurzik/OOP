package org.example.benchmark;

/**
 * Класс для хранения результатов бенчмарка.
 */
public class BenchmarkResult {
    private final String testName;
    private final long executionTimeNanos;
    private final int threadCount;

    /**
     * Создает результат бенчмарка.
     * 
     * @param testName           название теста
     * @param executionTimeNanos время выполнения
     * @param threadCount        количество использованных потоков
     */
    public BenchmarkResult(String testName, long executionTimeNanos, int threadCount) {
        this.testName = testName;
        this.executionTimeNanos = executionTimeNanos;
        this.threadCount = threadCount;
    }

    public String getTestName() {
        return testName;
    }

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    public double getExecutionTimeMs() {
        return executionTimeNanos / 1_000_000.0;
    }

    public int getThreadCount() {
        return threadCount;
    }

    @Override
    public String toString() {
        return String.format("%-30s | %8.2f ms | %d потоков",
                testName, getExecutionTimeMs(), threadCount);
    }
}
