package org;

import java.util.Objects;

/**
 * Класс для хранения записи (пары ключ-значение).
 * @param <K> Тип ключа
 * @param <V> Тип значения
 */
public class Node<K, V> {
    final K key;
    V value;
    Node<K, V> next;
    final int hash;

    Node(K key, V value, int hash, Node<K, V> next) {
        this.key = key;
        this.value = value;
        this.hash = hash;
        this.next = next;
    }

    /**
     * Возвращает ключ.
     * @return ключ
     */
    public final K getKey() {
        return key;
    }

    /**
     * @return значение
     */
    public final V getValue() {
        return value;
    }

    public final String toString() {
        return key + "=" + value;
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Node) {
            Node<?, ?> e = (Node<?, ?>) o;
            return Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue());
        }
        return false;
    }
}
