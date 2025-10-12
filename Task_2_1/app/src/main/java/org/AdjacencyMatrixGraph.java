package org;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Реализация интерфейса Graph с использованием матрицы смежности.
 *
 * @param <V> Тип данных для вершин.
 */
public class AdjacencyMatrixGraph<V> implements Graph<V> {

    private int[][] adjacencyMatrix;
    private final List<V> vertices;
    private final Map<V, Integer> vertexIndices;

    /**
     * Конструктор для создания пустого графа.
     */
    public AdjacencyMatrixGraph() {
        this.adjacencyMatrix = new int[0][0];
        this.vertices = new ArrayList<>();
        this.vertexIndices = new HashMap<>();
    }

    @Override
    public void addVertex(V vertex) {
        if (!vertexIndices.containsKey(vertex)) {
            int newIndex = vertices.size();
            vertices.add(vertex);
            vertexIndices.put(vertex, newIndex);

            // Увеличиваем размер матрицы
            int newSize = newIndex + 1;
            int[][] newMatrix = new int[newSize][newSize];
            for (int i = 0; i < adjacencyMatrix.length; i++) {
                System.arraycopy(adjacencyMatrix[i], 0, newMatrix[i], 0, adjacencyMatrix.length);
            }
            adjacencyMatrix = newMatrix;
        }
    }

    @Override
    public void removeVertex(V vertex) {
        Integer indexToRemove = vertexIndices.get(vertex);
        if (indexToRemove == null) {
            return; // Вершины нет в графе
        }

        int oldSize = vertices.size();
        int newSize = oldSize - 1;

        vertices.remove(indexToRemove.intValue());
        vertexIndices.remove(vertex);

        for (int i = indexToRemove; i < newSize; i++) {
            V v = vertices.get(i);
            vertexIndices.put(v, i);
        }

        int[][] newMatrix = new int[newSize][newSize];
        for (int i = 0, newI = 0; i < oldSize; i++) {
            if (i == indexToRemove) {
                continue;
            }
            for (int j = 0, newJ = 0; j < oldSize; j++) {
                if (j == indexToRemove) {
                    continue;
                }
                newMatrix[newI][newJ] = adjacencyMatrix[i][j];
                newJ++;
            }
            newI++;
        }
        adjacencyMatrix = newMatrix;
    }


    @Override
    public void addEdge(V source, V destination) {
        addVertex(source);
        addVertex(destination);

        int sourceIndex = vertexIndices.get(source);
        int destIndex = vertexIndices.get(destination);
        adjacencyMatrix[sourceIndex][destIndex] = 1;
    }

    @Override
    public void removeEdge(V source, V destination) {
        if (vertexIndices.containsKey(source) && vertexIndices.containsKey(destination)) {
            int sourceIndex = vertexIndices.get(source);
            int destIndex = vertexIndices.get(destination);
            adjacencyMatrix[sourceIndex][destIndex] = 0;
        }
    }

    @Override
    public List<V> getNeighbors(V vertex) {
        List<V> neighbors = new ArrayList<>();
        Integer sourceIndex = vertexIndices.get(vertex);
        if (sourceIndex != null) {
            for (int j = 0; j < adjacencyMatrix[sourceIndex].length; j++) {
                if (adjacencyMatrix[sourceIndex][j] == 1) {
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
        return Objects.equals(vertices, that.vertices) &&
                Arrays.deepEquals(adjacencyMatrix, that.adjacencyMatrix);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(vertices);
        result = 31 * result + Arrays.deepHashCode(adjacencyMatrix);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Матрица смежности:\n");
        sb.append("Веришины: ").append(vertices).append("\n");
        sb.append("Матрица:\n");
        for (int[] row : adjacencyMatrix) {
            sb.append("  ").append(Arrays.toString(row)).append("\n");
        }
        return sb.toString();
    }
}