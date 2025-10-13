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
 * Реализация интерфейса Graph с использованием матрицы инцидентности на основе List<List<Integer>>.
 *
 * @param <V> Тип данных для вершин.
 */
public class IncidenceMatrixGraph<V> implements Graph<V> {

    private final List<List<Integer>> incidenceMatrix;
    private final List<V> vertices;
    private final List<Edge<V>> edges;
    private final Map<V, Integer> vertexIndices;

    /**
     * Конструктор для создания пустого графа.
     */
    public IncidenceMatrixGraph() {
        this.incidenceMatrix = new ArrayList<>();
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

            List<Integer> newRow = new ArrayList<>(Collections.nCopies(edges.size(), 0));
            incidenceMatrix.add(newRow);
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
        int newEdgeIndex = edges.size() - 1;

        for (List<Integer> row : incidenceMatrix) {
            row.add(0);
        }

        incidenceMatrix.get(sourceIndex).set(newEdgeIndex, 1);
        incidenceMatrix.get(destIndex).set(newEdgeIndex, -1);
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
        for (int i = edgesToRemove.size() - 1; i >= 0; i--) {
            Edge<V> edge = edgesToRemove.get(i);
            removeEdge(edge.source, edge.destination);
        }

        incidenceMatrix.remove(indexToRemove.intValue());

        vertices.remove(indexToRemove.intValue());
        vertexIndices.remove(vertex);

        for (int i = indexToRemove; i < vertices.size(); i++) {
            V v = vertices.get(i);
            vertexIndices.put(v, i);
        }
    }


    @Override
    public void removeEdge(V source, V destination) {
        Edge<V> edgeToRemove = new Edge<>(source, destination);
        int edgeIndex = edges.indexOf(edgeToRemove);

        if (edgeIndex == -1) {
            return;
        }

        edges.remove(edgeIndex);

        if (!incidenceMatrix.isEmpty()) {
            for (List<Integer> row : incidenceMatrix) {
                if (row.size() > edgeIndex) {
                    row.remove(edgeIndex);
                }
            }
        }
    }

    @Override
    public List<V> getNeighbors(V vertex) {
        List<V> neighbors = new ArrayList<>();
        Integer vertexIndex = vertexIndices.get(vertex);

        if (vertexIndex != null) {
            List<Integer> row = incidenceMatrix.get(vertexIndex);
            for (int j = 0; j < row.size(); j++) {
                if (row.get(j) == 1) {
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
        return Objects.equals(new HashSet<>(vertices), new HashSet<>(that.vertices))
                && Objects.equals(new HashSet<>(edges), new HashSet<>(that.edges));
    }

    @Override
    public int hashCode() {
        return Objects.hash(new HashSet<>(vertices), new HashSet<>(edges));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Матрица инцидентности:\n");
        sb.append("Вершины: ").append(vertices).append("\n");
        sb.append("Рёбра: ").append(edges.stream().map(e -> "(" + e.source
                + "->" + e.destination + ")").toList()).append("\n");
        sb.append("Матрица:\n");
        for (int i = 0; i < vertices.size(); i++) {
            sb.append(String.format("%5s: ", vertices.get(i)));
            sb.append(incidenceMatrix.get(i)).append("\n");
        }
        return sb.toString();
    }
}