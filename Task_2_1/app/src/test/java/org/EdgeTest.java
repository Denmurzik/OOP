package org;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 * Тесты для класса Edge.
 */
class EdgeTest {

    @Test
    @DisplayName("Два ребра с одинаковыми вершинами должны быть равны")
    void equalsWhenEdgesAreIdenticalShouldReturnTrue() {
        Edge<String> edge1 = new Edge<>("A", "B");
        Edge<String> edge2 = new Edge<>("A", "B");
        
        assertEquals(edge1, edge2,
                "Ребра с одинаковыми source и destination должны быть равны");
    }

    @Test
    @DisplayName("Ребра с разными вершинами не должны быть равны")
    void equalsWhenEdgesAreDifferentShouldReturnFalse() {
        Edge<String> edge1 = new Edge<>("A", "B");
        Edge<String> edge2 = new Edge<>("A", "C"); 
        Edge<String> edge3 = new Edge<>("C", "B"); 

        assertNotEquals(edge1, edge2,
                "Ребра с разной destination не должны быть равны");
        assertNotEquals(edge1, edge3,
                "Ребра с разной source не должны быть равны");
    }

    @Test
    @DisplayName("Ребро не должно быть равно null или объекту другого класса")
    void equals_withNullAndDifferentClassShouldReturnFalse() {
        Edge<String> edge = new Edge<>("A", "B");
        String notAnEdge = "Not an edge";

        assertNotEquals(null, edge,
                "Ребро не должно быть равно null");
        assertNotEquals(edge, notAnEdge,
                "Ребро не должно быть равно объекту другого класса");
    }

    @Test
    @DisplayName("Хеш-коды одинаковых ребер должны совпадать")
    void hashCodeWhenEdgesAreIdenticalShouldBeEqual() {
        Edge<String> edge1 = new Edge<>("A", "B");
        Edge<String> edge2 = new Edge<>("A", "B");

        assertEquals(edge1.hashCode(), edge2.hashCode(),
                "Хеш-коды одинаковых ребер должны совпадать");
    }

    @Test
    @DisplayName("Хеш-коды разных ребер (вероятно) не должны совпадать")
    void hashCodeWhenEdgesAreDifferentShouldBeDifferent() {
        Edge<String> edge1 = new Edge<>("A", "B");
        Edge<String> edge2 = new Edge<>("B", "A");

        assertNotEquals(edge1.hashCode(), edge2.hashCode(),
                "Хеш-коды разных ребер, скорее всего, не совпадут");
    }
}