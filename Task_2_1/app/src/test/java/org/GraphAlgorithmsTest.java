package org;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class GraphAlgorithmsTest {

    private Graph<String> graph;

    @BeforeEach
    void setUp() {
        graph = new AdjacencyListGraph<>();
    }

    /**
     * Проверяет, что для каждого ребра u -> v, u находится раньше v в списке.
     *
     * @param sortedList    отсортированный список вершин.
     * @param originalGraph исходный граф.
     */
    private <V> void assertIsValidTopologicalSort(
            List<V> sortedList, Graph<V> originalGraph) {
        assertEquals(originalGraph.getAllVertices().size(), sortedList.size(),
                "Размер отсортированного списка "
                        + "должен совпадать с количеством вершин в графе");
        assertTrue(sortedList.containsAll(originalGraph.getAllVertices()),
                "Отсортированный список должен содержать все вершины графа");
        for (V u : originalGraph.getAllVertices()) {
            for (V v : originalGraph.getNeighbors(u)) {
                assertTrue(sortedList.indexOf(u) < sortedList.indexOf(v),
                        "Для ребра " + u + " -> " + v + ", вершина "
                                + u + " должна быть раньше " + v);
            }
        }
    }

    @Test
    @DisplayName("Сортировка простого направленного ациклического графа")
    void topologicalSortForSimpleDagShouldReturnValidOrder() {
        // Зависимости: 5->0, 5->2, 4->0, 4->1, 2->3, 3->1
        graph.addEdge("5", "0");
        graph.addEdge("5", "2");
        graph.addEdge("4", "0");
        graph.addEdge("4", "1");
        graph.addEdge("2", "3");
        graph.addEdge("3", "1");

        List<String> sortedList = GraphAlgorithms.topologicalSort(graph);
        assertIsValidTopologicalSort(sortedList, graph);
    }

    @Test
    @DisplayName("Сортировка графа с несколькими компонентами связности")
    void topologicalSortForDisconnectedGraphShouldReturnValidOrder() {
        graph.addEdge("A", "B");
        graph.addEdge("C", "D");
        graph.addVertex("E"); // Изолированная вершина

        List<String> sortedList = GraphAlgorithms.topologicalSort(graph);
        assertIsValidTopologicalSort(sortedList, graph);
    }

    @Test
    @DisplayName("Сортировка линейного графа (цепочки)")
    void topologicalSortForLinearGraphShouldReturnExactOrder() {
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");

        List<String> sortedList = GraphAlgorithms.topologicalSort(graph);
        assertEquals(List.of("A", "B", "C", "D"), sortedList,
                "Для линейного графа порядок должен быть строгим");
    }


    @Test
    @DisplayName("Сортировка графа, содержащего цикл, должна выбрасывать исключение")
    void topologicalSortForCyclicGraphShouldThrowException() {
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "A");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            GraphAlgorithms.topologicalSort(graph);
        });

        assertEquals("Граф содержит цикл!", exception.getMessage());
    }

    @Test
    @DisplayName("Сортировка пустого графа возвращает пустой список")
    void topologicalSortForEmptyGraphShouldReturnEmptyList() {
        List<String> sortedList = GraphAlgorithms.topologicalSort(graph);
        assertTrue(sortedList.isEmpty(),
                "Для пустого графа результат должен быть пустым списком");
    }

    @Test
    @DisplayName("Сортировка графа с одной вершиной")
    void topologicalSortForSingleVertexGraphShouldReturnListWithOneVertex() {
        graph.addVertex("A");
        List<String> sortedList = GraphAlgorithms.topologicalSort(graph);
        assertEquals(List.of("A"), sortedList,
                "Результат должен содержать одну вершину");
    }
}