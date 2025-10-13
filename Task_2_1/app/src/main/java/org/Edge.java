package org;

import java.util.Objects;

/**
 * Класс, представляющий направленное ребро графа.
 *
 * @param <V> Тип данных для вершин.
 */
public class Edge<V> {
    final V source;
    final V destination;

    /**
     * Конструктор для создания ребра.
     *
     * @param source исходная вершина.
     * @param destination конечная вершина.
     */
    public Edge(V source, V destination) {
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