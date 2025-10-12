package org;

import java.io.IOException;


public class Main {
    /**
     * Точка входа в программу.
     * Демонстрирует работу с графами и алгоритмом топологической сортировки.*
     *
     * @param args args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Graph<String> graph = new IncidenceMatrixGraph<>();
        Graph<String> fileGraph = Graph.fromFile("graph.txt", graph);
        System.out.println(fileGraph);
        System.out.println(GraphAlgorithms.topologicalSort(fileGraph));


        Graph<Integer> cyclicGraph = new AdjacencyMatrixGraph<>();
        cyclicGraph.addEdge(1, 2);
        cyclicGraph.addEdge(2, 3);
        cyclicGraph.addEdge(3, 1);
        System.out.println("Граф с циклом:");
        System.out.println(cyclicGraph);

        try {
            GraphAlgorithms.topologicalSort(cyclicGraph);
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка при сортировке: " + e.getMessage());
        }

    }
}