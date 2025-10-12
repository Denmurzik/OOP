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
 * Реализация интерфейса Graph с использованием матрицы инцидентности.
 *
 * @param <V> Тип данных для вершин.
 */
public class IncidenceMatrixGraph<V> implements Graph<V> {

    private static class Edge<V> {
        V source;
        V destination;

        Edge(V source, V destination) {
            this.source = source;
            this.destination = destination;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Edge<?> edge = (Edge<?>) o;
            return Objects.equals(source, edge.source)
                    && Objects.equals(destination, edge.destination);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, destination);
        }
    }

    private int[][] incidenceMatrix;
    private final List<V> vertices;
    private final List<Edge<V>> edges;
    private final Map<V, Integer> vertexIndices;

    /**
     * Конструктор для создания пустого графа.
     */
    public IncidenceMatrixGraph() {
        this.incidenceMatrix = new int[0][0];
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.vertexIndices = new HashMap<>();
    }


    @Override
    public void addVertex(V vertex) {
        if (!vertexIndices.containsKey(vertex)) {
            int newIndex = vertices.size();
            vertices.add(vertex);
            vertexIndices.put(vertex, newIndex);

            int numVertices = vertices.size();
            int numEdges = edges.size();
            int[][] newMatrix = new int[numVertices][numEdges];

            for (int i = 0; i < numVertices - 1; i++) {
                System.arraycopy(incidenceMatrix[i], 0, newMatrix[i], 0, numEdges);
            }
            incidenceMatrix = newMatrix;
        }
    }

    @Override
    public void addEdge(V source, V destination) {
        addVertex(source);
        addVertex(destination);

        Edge<V> newEdge = new Edge<>(source, destination);
        if (edges.contains(newEdge)) {
            return;
        }

        edges.add(newEdge);
        int sourceIndex = vertexIndices.get(source);
        int destIndex = vertexIndices.get(destination);

        int numVertices = vertices.size();
        int numEdges = edges.size();
        int[][] newMatrix = new int[numVertices][numEdges];

        for (int i = 0; i < numVertices; i++) {
            System.arraycopy(incidenceMatrix[i], 0, newMatrix[i], 0, numEdges - 1);
        }

        newMatrix[sourceIndex][numEdges - 1] = 1;
        newMatrix[destIndex][numEdges - 1] = -1;

        incidenceMatrix = newMatrix;
    }

    @Override
    public void removeVertex(V vertex) {
        Integer indexToRemove = vertexIndices.get(vertex);
        if (indexToRemove == null) {
            return;
        }

        List<Edge<V>> edgesToRemove = new ArrayList<>();
        for (Edge<V> edge : edges) {
            if (edge.source.equals(vertex) || edge.destination.equals(vertex)) {
                edgesToRemove.add(edge);
            }
        }
        for (Edge<V> edge : edgesToRemove) {
            removeEdge(edge.source, edge.destination);
        }

        int oldSize = vertices.size();
        int newSize = oldSize - 1;

        vertices.remove(indexToRemove.intValue());
        vertexIndices.remove(vertex);

        for(int i = indexToRemove; i < newSize; i++) {
            V v = vertices.get(i);
            vertexIndices.put(v, i);
        }

        int numEdges = edges.size();
        int[][] newMatrix = new int[newSize][numEdges];
        for (int i = 0, newI = 0; i < oldSize; i++) {
            if (i == indexToRemove) {
                continue;
            }
            System.arraycopy(incidenceMatrix[i], 0, newMatrix[newI], 0, numEdges);
            newI++;
        }
        incidenceMatrix = newMatrix;
    }


    @Override
    public void removeEdge(V source, V destination) {
        Edge<V> edgeToRemove = new Edge<>(source, destination);
        int edgeIndex = edges.indexOf(edgeToRemove);

        if (edgeIndex == -1) {
            return;
        }

        edges.remove(edgeIndex);

        int numVertices = vertices.size();
        int newNumEdges = edges.size();

        if (newNumEdges == 0) {
            incidenceMatrix = new int[numVertices][0];
            return;
        }

        int[][] newMatrix = new int[numVertices][newNumEdges];

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0, newJ = 0; j < newNumEdges + 1; j++) {
                if (j == edgeIndex) {
                    continue;
                }
                newMatrix[i][newJ] = incidenceMatrix[i][j];
                newJ++;
            }
        }
        incidenceMatrix = newMatrix;
    }

    @Override
    public List<V> getNeighbors(V vertex) {
        List<V> neighbors = new ArrayList<>();
        Integer vertexIndex = vertexIndices.get(vertex);

        if (vertexIndex != null) {
            for (int j = 0; j < edges.size(); j++) {
                if (incidenceMatrix[vertexIndex][j] == 1) {
                    neighbors.add(edges.get(j).destination);
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IncidenceMatrixGraph<?> that = (IncidenceMatrixGraph<?>) o;
        // Для сравнения достаточно сравнить множества вершин и ребер
        return Objects.equals(new HashSet<>(vertices), new HashSet<>(that.vertices)) &&
                Objects.equals(new HashSet<>(edges), new HashSet<>(that.edges));
    }

    @Override
    public int hashCode() {
        return Objects.hash(new HashSet<>(vertices), new HashSet<>(edges));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Матрица инцидентности:\n");
        sb.append("Вершины: ").append(vertices).append("\n");
        sb.append("Рёбра: ").append(edges.stream().map(e -> "(" + e.source + "->" + e.destination + ")").toList()).append("\n");
        sb.append("Матрица:\n");
        for (int i = 0; i < vertices.size(); i++) {
            sb.append(String.format("%5s: ", vertices.get(i)));
            sb.append(Arrays.toString(incidenceMatrix[i])).append("\n");
        }
        return sb.toString();
    }
}