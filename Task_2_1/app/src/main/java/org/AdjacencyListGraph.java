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

    private final Map<V, List<V>> adjacencyList;

    public AdjacencyListGraph() {
        this.adjacencyList = new HashMap<>();
    }

    @Override
    public void addVertex(V vertex) {
        adjacencyList.putIfAbsent(vertex, new LinkedList<>());
    }

    @Override
    public void removeVertex(V vertex) {
        adjacencyList.remove(vertex);

        for (List<V> neighbors : adjacencyList.values()) {
            neighbors.remove(vertex);
        }
    }

    @Override
    public void addEdge(V source, V destination) {
        addVertex(source);
        addVertex(destination);

        if (!adjacencyList.get(source).contains(destination)) {
            adjacencyList.get(source).add(destination);
        }
    }

    @Override
    public void removeEdge(V source, V destination) {
        if (adjacencyList.containsKey(source)) {
            adjacencyList.get(source).remove(destination);
        }
    }

    @Override
    public List<V> getNeighbors(V vertex) {
        return adjacencyList.getOrDefault(vertex, new LinkedList<>());
    }

    @Override
    public Set<V> getAllVertices() {
        return adjacencyList.keySet();
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
        return Objects.equals(adjacencyList, that.adjacencyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adjacencyList);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Списки смежности:\n");
        for (Map.Entry<V, List<V>> entry : adjacencyList.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(" -> ")
                    .append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}