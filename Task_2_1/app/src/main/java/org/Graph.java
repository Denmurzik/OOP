package org;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;


/**
 * Интерфейс, представляющий структуру данных "Граф".
 *
 * @param <V> Тип данных, используемый для вершин.
 */
public interface Graph<V> {

    /**
     * Добавляет вершину в граф.
     *
     * @param vertex вершина для добавления.
     */
    void addVertex(V vertex);

    /**
     * Удаляет вершину и все связанные с ней ребра из графа.
     *
     * @param vertex вершина для удаления.
     */
    void removeVertex(V vertex);

    /**
     * Добавляет направленное ребро между двумя вершинами.
     *
     * @param source      исходная вершина.
     * @param destination конечная вершина.
     */
    void addEdge(V source, V destination);

    /**
     * Удаляет ребро между двумя вершинами.
     *
     * @param source      исходная вершина.
     * @param destination конечная вершина.
     */
    void removeEdge(V source, V destination);

    /**
     * Возвращает список всех "соседей" (смежных вершин) для данной вершины.
     * Сосед - это вершина, в которую ведет ребро из указанной.
     *
     * @param vertex вершина, для которой ищутся соседи.
     * @return Список смежных вершин.
     */
    List<V> getNeighbors(V vertex);

    /**
     * Возвращает множество всех вершин в графе.
     *
     * @return Множество вершин.
     */
    Set<V> getAllVertices();


    /**
     * Метод для загрузки данных из файла в существующий граф.
     *
     * @param filePath путь к файлу.
     * @param graph    экземпляр графа, который нужно заполнить.
     * @param <V>      тип вершин.
     * @return тот же самый граф.
     * @throws IOException если возникает ошибка при чтении файла.
     */
    default  <V> Graph<V> fromFile(String filePath, Graph<V> graph) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\s+");
                V source = (V) parts[0];
                graph.addVertex(source);

                for (int i = 1; i < parts.length; i++) {
                    V destination = (V) parts[i];
                    graph.addEdge(source, destination);
                }
            }
        }
        return graph;
    }
}