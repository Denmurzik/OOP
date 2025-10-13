package org;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Реализация интерфейса Graph с использованием списка смежности.
 *
 * @param <V> Тип данных для вершин.
 */
public class AdjacencyListGraph<V> implements Graph<V> {

    private final Map<V, List<V>> adjacencyMap;

    public AdjacencyListGraph() {
        this.adjacencyMap = new HashMap<>();
    }

    @Override
    public void addVertex(V vertex) {
        adjacencyMap.putIfAbsent(vertex, new LinkedList<>());
    }

    @Override
    public void removeVertex(V vertex) {
        adjacencyMap.remove(vertex);

        for (List<V> neighbors : adjacencyMap.values()) {
            neighbors.remove(vertex);
        }
    }

    @Override
    public void addEdge(V source, V destination) {
        addVertex(source);
        addVertex(destination);

        if (!adjacencyMap.get(source).contains(destination)) {
            adjacencyMap.get(source).add(destination);
        }
    }

    @Override
    public void removeEdge(V source, V destination) {
        if (adjacencyMap.containsKey(source)) {
            adjacencyMap.get(source).remove(destination);
        }
    }

    @Override
    public List<V> getNeighbors(V vertex) {
        return adjacencyMap.getOrDefault(vertex, new LinkedList<>());
    }

    @Override
    public Set<V> getAllVertices() {
        return adjacencyMap.keySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdjacencyListGraph<?> that = (AdjacencyListGraph<?>) o;
        return Objects.equals(adjacencyMap, that.adjacencyMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adjacencyMap);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Списки смежности:\n");
        for (Map.Entry<V, List<V>> entry : adjacencyMap.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(" -> ")
                    .append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}