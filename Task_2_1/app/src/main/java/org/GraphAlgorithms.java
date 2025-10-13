package org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Класс, содержащий статические методы для выполнения алгоритмов на графах.
 */
public class GraphAlgorithms {

    /**
     * Выполняет топологическую сортировку вершин графа.
     * Алгоритм работает только для направленных ациклических графов.
     *
     * @param graph граф для сортировки.
     * @param <V>   тип вершин в графе.
     * @return список вершин в топологическом порядке.
     * @throws IllegalArgumentException если граф содержит цикл.
     */
    public static <V> List<V> topologicalSort(Graph<V> graph) {
        Map<V, Integer> inDegree = new HashMap<>();
        Set<V> allVertices = graph.getAllVertices();

        for (V vertex : allVertices) {
            inDegree.put(vertex, 0);
        }

        for (V vertex : allVertices) {
            for (V neighbor : graph.getNeighbors(vertex)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        Queue<V> queue = new LinkedList<>();
        for (Map.Entry<V, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<V> sortedList = new ArrayList<>();
        int visitedCount = 0;

        while (!queue.isEmpty()) {
            V current = queue.poll();
            sortedList.add(current);
            visitedCount++;

            for (V neighbor : graph.getNeighbors(current)) {
                int newInDegree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, newInDegree);

                if (newInDegree == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (visitedCount != allVertices.size()) {
            throw new IllegalArgumentException("Граф содержит цикл!");
        }

        return sortedList;
    }
}