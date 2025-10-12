package org;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class AdjacencyMatrixGraphTest {

    private Graph<String> graph;

    @BeforeEach
    void setUp() {
        graph = new AdjacencyMatrixGraph<>();
    }

    @Test
    @DisplayName("Добавление одной вершины в пустой граф")
    void addVertex_whenGraphIsEmpty_shouldContainVertex() {
        graph.addVertex("A");
        assertTrue(graph.getAllVertices().contains("A"),
                "Граф должен содержать вершину A");
        assertEquals(1, graph.getAllVertices().size(),
                "Размер графа должен быть 1");
    }

    @Test
    @DisplayName("Добавление дублирующейся вершины не изменяет граф")
    void addVertex_whenVertexExists_shouldNotChangeGraph() {
        graph.addVertex("A");
        graph.addVertex("A");
        assertEquals(1, graph.getAllVertices().size(),
                "Размер графа не должен измениться при добавлении дубликата");
    }

    @Test
    @DisplayName("Добавление ребра между существующими вершинами")
    void addEdge_whenVerticesExist_shouldCreateEdge() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");
        assertTrue(graph.getNeighbors("A").contains("B"),
                "B должен быть соседом A");
        assertFalse(graph.getNeighbors("B").contains("A"),
                "A не должен быть соседом B (граф направленный)");
    }

    @Test
    @DisplayName("Добавление ребра создает вершины, если их нет")
    void addEdge_whenVerticesDoNotExist_shouldCreateVerticesAndEdge() {
        graph.addEdge("A", "B");
        assertTrue(graph.getAllVertices().containsAll(Set.of("A", "B")),
                "Обе вершины A и B должны быть созданы");
        assertTrue(graph.getNeighbors("A").contains("B"),
                "B должен быть соседом A");
    }

    @Test
    @DisplayName("Удаление существующего ребра")
    void removeEdge_whenEdgeExists_shouldRemoveIt() {
        graph.addEdge("A", "B");
        assertTrue(graph.getNeighbors("A").contains("B"));
        graph.removeEdge("A", "B");
        assertFalse(graph.getNeighbors("A").contains("B"),
                "Ребро от A к B должно быть удалено");
    }

    @Test
    @DisplayName("Удаление вершины корректно перестраивает матрицу")
    void removeVertex_shouldRebuildMatrixAndIndices() {
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "A");

        graph.removeVertex("B");

        assertFalse(graph.getAllVertices().contains("B"),
                "Вершина B должна быть удалена");
        assertEquals(2, graph.getAllVertices().size(),
                "В графе должно остаться 2 вершины");

        // Проверяем, что оставшиеся ребра на месте
        assertTrue(graph.getNeighbors("C").contains("A"),
                "Ребро от C к A должно сохраниться");

        // Проверяем, что ребер, связанных с B, больше нет
        assertFalse(graph.getNeighbors("A").contains("B"));

        // Добавим новое ребро, чтобы проверить, что индексы в матрице корректны
        graph.addEdge("A", "C");
        assertTrue(graph.getNeighbors("A").contains("C"),
                "Новое ребро A->C должно быть добавлено успешно");
    }


    @Test
    @DisplayName("Получение соседей для вершины")
    void getNeighbors_shouldReturnCorrectNeighbors() {
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        List<String> neighbors = graph.getNeighbors("A");
        assertTrue(neighbors.containsAll(List.of("B", "C")),
                "Соседями A должны быть B и C");
        assertEquals(2, neighbors.size(),
                "У вершины A должно быть 2 соседа");
    }

    @Test
    @DisplayName("Получение соседей для несуществующей вершины возвращает пустой список")
    void getNeighbors_forNonExistentVertex_shouldReturnEmptyList() {
        assertTrue(graph.getNeighbors("X").isEmpty(),
                "Список соседей для несуществующей вершины должен быть пустым");
    }

    @Test
    @DisplayName("Получение всех вершин")
    void getAllVertices_shouldReturnAllAddedVertices() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        Set<String> vertices = graph.getAllVertices();
        assertTrue(vertices.containsAll(Set.of("A", "B", "C")),
                "Множество должно содержать все добавленные вершины");
        assertEquals(3, vertices.size(),
                "Размер множества должен быть 3");
    }

    @Test
    @DisplayName("Сравнение двух одинаковых по структуре графов")
    void equals_whenGraphsAreIdentical_shouldReturnTrue() {
        Graph<String> graph1 = new AdjacencyMatrixGraph<>();
        graph1.addEdge("A", "B");
        graph1.addEdge("B", "C");

        Graph<String> graph2 = new AdjacencyMatrixGraph<>();
        graph2.addEdge("A", "B");
        graph2.addEdge("B", "C");

        assertEquals(graph1, graph2,
                "Графы с одинаковой структурой должны быть равны");
        assertEquals(graph1.hashCode(), graph2.hashCode(),
                "Хеш-коды равных графов должны совпадать");
    }

    @Test
    @DisplayName("Сравнение двух разных графов")
    void equals_whenGraphsAreDifferent_shouldReturnFalse() {
        Graph<String> graph1 = new AdjacencyMatrixGraph<>();
        graph1.addEdge("A", "B");

        Graph<String> graph2 = new AdjacencyMatrixGraph<>();
        graph2.addEdge("A", "C"); // Другое ребро

        assertNotEquals(graph1, graph2,
                "Графы с разной структурой не должны быть равны");
    }
}