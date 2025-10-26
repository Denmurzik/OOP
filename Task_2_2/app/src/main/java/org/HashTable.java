package org;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Реализация параметризованной хеш-таблицы. Реализует Iterable для поддержки итерации (включая
 * fail-fast).
 *
 * @param <K> Тип ключа
 * @param <V> Тип значения
 */
public class HashTable<K, V> implements Iterable<Node<K, V>> {

    /**
     * Массив корзин.
     */
    private Node<K, V>[] table;

    /**
     * Количество пар ключ-значение.
     */
    private int size;

    /**
     * Текущая емкость (размер массива table).
     */
    private int capacity;

    /**
     * Порог, при котором произойдет resize.
     */
    private int threshold;

    /**
     * Емкость по умолчанию.
     */
    private static final int DEFAULT_CAPACITY = 16;

    /**
     * Максимальная емкость.
     */
    private static final int MAXIMUM_CAPACITY = 1 << 30; // 2^30

    /**
     * Счетчик модификаций. Используется для обнаружения одновременных изменений во время итерации.
     */
    private int modCount = 0;

    /**
     * Создает пустую хеш-таблицу с емкостью по умолчанию (16).
     */
    public HashTable() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Создает пустую хеш-таблицу с указанной начальной емкостью.
     *
     * @param initialCapacity Начальная емкость.
     */
    public HashTable(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }
        this.capacity = initialCapacity;
        this.threshold = this.capacity;
        this.table = (Node<K, V>[]) new Node[this.capacity];
        this.size = 0;
    }

    /**
     * Вычисляет хеш-код для ключа.
     */
    /**
     * Возвращает количество пар ключ-значение в хеш-таблице.
     */
    public int size() {
        return size;
    }

    /**
     * Проверяет, пуста ли хеш-таблица.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    private int hash(Object key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode();
    }

    /**
     * Вычисляет индекс корзины в массиве table.
     */
    private int indexFor(int hash) {
        return Math.abs(hash % capacity);
    }


    /**
     * Добавляет пару ключ-значение. Если ключ уже существует, обновляет его значение.
     *
     * @param key   Ключ
     * @param value Значение
     * @return Старое значение или null, если ключа не было
     */
    public void put(K key, V value) {
        int hash = hash(key);
        int index = indexFor(hash);

        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash && (Objects.equals(key, current.key))) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        modCount++;
        Node<K, V> head = table[index];
        Node<K, V> newNode = new Node<>(key, value, hash, head);
        table[index] = newNode;
        size++;

        if (size >= threshold) {
            resize(capacity * 2);
        }
    }

    /**
     * Поиск значения по ключу.
     *
     * @param key Ключ
     * @return Значение или null, если ключ не найден
     */
    public V get(Object key) {
        int hash = hash(key);
        int index = indexFor(hash);

        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash && (Objects.equals(key, current.key))) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Удаляет пару ключ-значение по ключу.
     *
     * @param key Ключ
     * @return Удаленное значение или null, если ключ не был найден
     */
    public V remove(Object key) {
        int hash = hash(key);
        int index = indexFor(hash);

        Node<K, V> current = table[index];
        Node<K, V> prev = null;

        while (current != null) {
            if (current.hash == hash && (Objects.equals(key, current.key))) {
                modCount++;
                size--;

                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    /**
     * Увеличивает размер хеш-таблицы и перераспределяет все существующие элементы.
     *
     * @param newCapacity Новая емкость
     */
    private void resize(int newCapacity) {
        if (capacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        if (newCapacity > MAXIMUM_CAPACITY) {
            newCapacity = MAXIMUM_CAPACITY;
        }
        Node<K, V>[] oldTable = table;
        // Создаем новую таблицу и обновляем поля
        this.table = (Node<K, V>[]) new Node[newCapacity];
        this.capacity = newCapacity;
        this.threshold = newCapacity;

        // Переносим все элементы из старой таблицы в новую
        for (Node<K, V> headNode : oldTable) {
            Node<K, V> current = headNode;
            while (current != null) {
                Node<K, V> next = current.next;

                // Вычисляем новый индекс и вставляем узел
                int newIndex = indexFor(current.hash);
                current.next = table[newIndex];
                table[newIndex] = current;

                current = next;
            }
        }
    }

    /**
     * Обновляет значение по ключу.
     *
     * @param key   Ключ
     * @param value Новое значение
     */
    public void update(K key, V value) {
        this.put(key, value);
    }

    /**
     * Проверяет наличие ключа в таблице.
     *
     * @param key Ключ
     * @return true, если ключ найден, иначе false
     */
    public boolean containsKey(Object key) {
        int hash = hash(key);
        int index = indexFor(hash);

        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash && (Objects.equals(key, current.key))) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Возвращает строковое представление хеш-таблицы.
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('{');

        Iterator<Node<K, V>> it = this.iterator();
        while (it.hasNext()) {
            Node<K, V> node = it.next();
            sb.append(node.key == this ? "(this Map)" : node.key);
            sb.append('=');
            sb.append(node.value == this ? "(this Map)" : node.value);
            if (it.hasNext()) {
                sb.append(", ");
            }
        }

        sb.append('}');
        return sb.toString();
    }

    /**
     * Сравнивает эту хеш-таблицу с другой на равенство.
     *
     * @param o Объект для сравнения
     * @return true, если таблицы равны
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof HashTable)) {
            return false;
        }

        HashTable<?, ?> other;
        try {
            other = (HashTable<?, ?>) o;
        } catch (ClassCastException e) {
            return false;
        }

        if (this.size() != other.size()) {
            return false;
        }

        try {
            for (Node<K, V> node : this) {
                K key = node.key;
                V value = node.value;

                Object otherValue = other.get(key);

                if (value == null) {
                    if (otherValue != null || !other.containsKey(key)) {
                        return false;
                    }
                } else {
                    if (!value.equals(otherValue)) {
                        return false;
                    }
                }
            }
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }

        return true;
    }

    /**
     * Возвращает хеш-код для таблицы.
     */
    @Override
    public int hashCode() {
        int h = 0;
        for (Node<K, V> node : this) {
            h += node.hashCode();
        }
        return h;
    }

    /**
     * Возвращает итератор по элементам (парам) в хеш-таблице.
     *
     * @return Итератор
     */
    @Override
    public Iterator<Node<K, V>> iterator() {
        return new HashIterator();
    }

    /**
     * Внутренний класс реализующий итератор.
     */
    private class HashIterator implements Iterator<Node<K, V>> {

        private int expectedModCount; // Ожидаемое число модификаций
        private int bucketIndex;      // Текущая корзина
        private Node<K, V> currentNode;  // Следующий узел для возврата
        private Node<K, V> lastReturned; // Последний возвращенный

        HashIterator() {
            this.expectedModCount = modCount;
            this.currentNode = null;
            this.lastReturned = null;
            this.bucketIndex = 0;


            if (size > 0) {
                while (bucketIndex < capacity && table[bucketIndex] == null) {
                    bucketIndex++;
                }
                if (bucketIndex < capacity) {
                    currentNode = table[bucketIndex];
                }
            }
        }

        /**
         * Проверяет, есть ли еще элементы для итерации.
         */
        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        /**
         * Возвращает следующий элемент итерации.
         *
         * @throws ConcurrentModificationException если таблица была изменена извне во время итерации.
         * @throws NoSuchElementException          если элементы закончились.
         */
        @Override
        public Node<K, V> next() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node<K, V> nodeToReturn = currentNode;
            lastReturned = nodeToReturn;


            currentNode = nodeToReturn.next;
            if (currentNode == null) {
                bucketIndex++;
                while (bucketIndex < capacity && table[bucketIndex] == null) {
                    bucketIndex++;
                }
                if (bucketIndex < capacity) {
                    currentNode = table[bucketIndex];
                }
            }

            return nodeToReturn;
        }

        /**
         * Удаляет из хеш-таблицы последний элемент, возвращенный этим итератором (методом
         * next()).
         */
        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException("Метод next() должен быть вызван перед remove()");
            }
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            HashTable.this.remove(lastReturned.key);

            expectedModCount = modCount;

            lastReturned = null;
        }
    }
}
