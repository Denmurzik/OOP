package org.example;

import java.util.List;
import org.example.benchmark.BenchmarkResult;
import org.example.benchmark.PrimeBenchmark;

/**
 * main класс.
 */
public class Main {

    /**
     * main.
     */
    public static void main(String[] args) {
        runBenchmarks();
    }

    /**
     * Запускает бенчмарки для всех реализаций.
     */
    private static void runBenchmarks() {

        int warmupRuns = 5; 
        int benchmarkRuns = 10; 

        int[] threadCounts = { 2, 4, 8, 16 };

        PrimeBenchmark benchmark = new PrimeBenchmark(warmupRuns, benchmarkRuns);

        List<BenchmarkResult> results = benchmark.runAllBenchmarks(threadCounts);

        PrimeBenchmark.printResults(results);
    }
}
