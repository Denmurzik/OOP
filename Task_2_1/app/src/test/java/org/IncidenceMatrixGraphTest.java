package org;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class IncidenceMatrixGraphTest {

    private Graph<String> graph;

    @BeforeEach
    void setUp() {
        graph = new IncidenceMatrixGraph<>();
    }

    @Test
    @DisplayName("Добавление одной вершины в пустой граф")
    void addVertexWhenGraphIsEmptyShouldContainVertex() {
        graph.addVertex("A");
        assertTrue(graph.getAllVertices().contains("A"), "Граф должен содержать вершину A");
        assertEquals(1, graph.getAllVertices().size(), "Размер графа должен быть 1");
    }

    @Test
    @DisplayName("Добавление дублирующейся вершины не изменяет граф")
    void addVertexWhenVertexExistsShouldNotChangeGraph() {
        graph.addVertex("A");
        graph.addVertex("A");
        assertEquals(1, graph.getAllVertices().size(), "Размер графа не должен измениться при добавлении дубликата");
    }

    @Test
    @DisplayName("Добавление ребра между существующими вершинами")
    void addEdgeWhenVerticesExistShouldCreateEdge() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");
        assertTrue(graph.getNeighbors("A").contains("B"), "B должен быть соседом A");
        assertFalse(graph.getNeighbors("B").contains("A"), "A не должен быть соседом B (граф направленный)");
    }

    @Test
    @DisplayName("Добавление ребра создает вершины, если их нет")
    void addEdgeWhenVerticesDoNotExistShouldCreateVerticesAndEdge() {
        graph.addEdge("A", "B");
        assertTrue(graph.getAllVertices().containsAll(Set.of("A", "B")), "Обе вершины A и B должны быть созданы");
        assertTrue(graph.getNeighbors("A").contains("B"), "B должен быть соседом A");
    }

    @Test
    @DisplayName("Удаление существующего ребра")
    void removeEdgeWhenEdgeExistsShouldRemoveIt() {
        graph.addEdge("A", "B");
        assertTrue(graph.getNeighbors("A").contains("B"));
        graph.removeEdge("A", "B");
        assertFalse(graph.getNeighbors("A").contains("B"), "Ребро от A к B должно быть удалено");
        assertEquals(2, graph.getAllVertices().size(), "Вершины должны остаться в графе после удаления ребра");
    }

    @Test
    @DisplayName("Удаление вершины удаляет все связанные с ней ребра")
    void removeVertexShouldRemoveAllIncidentEdges() {
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "A");

        graph.removeVertex("B");

        assertFalse(graph.getAllVertices().contains("B"), "Вершина B должна быть удалена");
        assertEquals(2, graph.getAllVertices().size(), "В графе должно остаться 2 вершины");

        assertTrue(graph.getNeighbors("C").contains("A"), "Ребро от C к A должно сохраниться");

        assertTrue(graph.getNeighbors("A").isEmpty(), "У вершины A не должно быть соседей");

        graph.addEdge("A", "C");
        assertTrue(graph.getNeighbors("A").contains("C"), "Должна быть возможность добавить новое ребро A->C");
    }


    @Test
    @DisplayName("Получение соседей для вершины")
    void getNeighborsShouldReturnCorrectNeighbors() {
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        List<String> neighbors = graph.getNeighbors("A");
        // В HashSet, так как порядок не гарантирован
        assertEquals(Set.of("B", "C"), new HashSet<>(neighbors), "Соседями A должны быть B и C");
        assertEquals(2, neighbors.size(), "У вершины A должно быть 2 соседа");
    }

    @Test
    @DisplayName("Получение соседей для несуществующей вершины возвращает пустой список")
    void getNeighborsForNonExistentVertexShouldReturnEmptyList() {
        assertTrue(graph.getNeighbors("X").isEmpty(), "Список соседей для несуществующей вершины должен быть пустым");
    }

    @Test
    @DisplayName("Получение всех вершин")
    void getAllVerticesShouldReturnAllAddedVertices() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        Set<String> vertices = graph.getAllVertices();
        assertTrue(vertices.containsAll(Set.of("A", "B", "C")), "Множество должно содержать все добавленные вершины");
        assertEquals(3, vertices.size(), "Размер множества должен быть 3");
    }

    @Test
    @DisplayName("Сравнение двух одинаковых по структуре графов")
    void equalsWhenGraphsAreIdenticalShouldReturnTrue() {
        Graph<String> graph1 = new IncidenceMatrixGraph<>();
        graph1.addEdge("A", "B");
        graph1.addEdge("B", "C");

        Graph<String> graph2 = new IncidenceMatrixGraph<>();
        graph2.addEdge("A", "B");
        graph2.addEdge("B", "C");

        assertEquals(graph1, graph2, "Графы с одинаковой структурой должны быть равны");
    }
}