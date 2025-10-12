package org;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Реализация Graph с использованием списка списков для матрицы смежности.
 * @param <V> Тип данных для вершин.
 */
public class AdjacencyMatrixGraph<V> implements Graph<V> {

    private final List<List<Integer>> adjacencyMatrix;
    private final List<V> vertices;
    private final Map<V, Integer> vertexIndices;

    /**
     * Конструктор
     */
    public AdjacencyMatrixGraph() {
        this.adjacencyMatrix = new ArrayList<>();
        this.vertices = new ArrayList<>();
        this.vertexIndices = new HashMap<>();
    }

    @Override
    public void addVertex(V vertex) {
        if (!vertexIndices.containsKey(vertex)) {
            int newIndex = vertices.size();
            vertices.add(vertex);
            vertexIndices.put(vertex, newIndex);

            for (List<Integer> row : adjacencyMatrix) {
                row.add(0);
            }
            List<Integer> newRow = new ArrayList<>(Collections.nCopies(newIndex + 1, 0));
            adjacencyMatrix.add(newRow);
        }
    }

    @Override
    public void removeVertex(V vertex) {
        Integer indexToRemove = vertexIndices.get(vertex);
        if (indexToRemove == null) {
            return;
        }

        int size = vertices.size();
        vertices.remove(indexToRemove.intValue());
        vertexIndices.remove(vertex);
        adjacencyMatrix.remove(indexToRemove.intValue());

        for (List<Integer> row : adjacencyMatrix) {
            row.remove(indexToRemove.intValue());
        }

        for (int i = indexToRemove; i < size - 1; i++) {
            V v = vertices.get(i);
            vertexIndices.put(v, i);
        }
    }

    @Override
    public void addEdge(V source, V destination) {
        addVertex(source);
        addVertex(destination);

        int sourceIndex = vertexIndices.get(source);
        int destIndex = vertexIndices.get(destination);
        adjacencyMatrix.get(sourceIndex).set(destIndex, 1);
    }

    @Override
    public void removeEdge(V source, V destination) {
        if (vertexIndices.containsKey(source) && vertexIndices.containsKey(destination)) {
            int sourceIndex = vertexIndices.get(source);
            int destIndex = vertexIndices.get(destination);
            adjacencyMatrix.get(sourceIndex).set(destIndex, 0);
        }
    }

    @Override
    public List<V> getNeighbors(V vertex) {
        List<V> neighbors = new ArrayList<>();
        Integer sourceIndex = vertexIndices.get(vertex);
        if (sourceIndex != null) {
            List<Integer> row = adjacencyMatrix.get(sourceIndex);
            for (int j = 0; j < row.size(); j++) {
                if (row.get(j) == 1) {
                    neighbors.add(vertices.get(j));
                }
            }
        }
        return neighbors;
    }

    @Override
    public Set<V> getAllVertices() {
        return new HashSet<>(vertices);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdjacencyMatrixGraph<?> that = (AdjacencyMatrixGraph<?>) o;
        return Objects.equals(vertices, that.vertices)
                && Objects.equals(adjacencyMatrix, that.adjacencyMatrix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices, adjacencyMatrix);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Матрица смежности:\n");
        sb.append("Вершины: ").append(vertices).append("\n");
        sb.append("Матрица:\n");
        for (List<Integer> row : adjacencyMatrix) {
            sb.append("  ").append(row).append("\n");
        }
        return sb.toString();
    }
}