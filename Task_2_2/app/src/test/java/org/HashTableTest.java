package org;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


/**
 * Юнит-тесты.
 */
class HashTableTest {

    private HashTable<String, Integer> table;

    @BeforeEach
    @DisplayName("1. Cоздание пустой хеш-таблицы")
    void setUp() {
        table = new HashTable<>();
    }

    @Test
    @DisplayName("Новая таблица должна быть пустой (size == 0)")
    void testIsEmptyOnCreation() {
        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
    }

    @Nested
    @DisplayName("2, 4. Добавление (put) и Поиск (get)")
    class PutAndGetTests {
        @Test
        @DisplayName("put должен добавлять элемент, get - возвращать его")
        void testPutAndGet() {
            table.put("one", 1);
            assertEquals(1, table.get("one"));
            assertEquals(1, table.size());
        }

        @Test
        @DisplayName("put должен обновлять значение")
        void testPutUpdatesValue() {
            table.put("one", 1);
            table.put("one", 10); 

            assertEquals(10, table.get("one")); 
            assertEquals(1, table.size()); 
        }

        @Test
        @DisplayName("get несуществующего ключа должен вернуть null")
        void testGetNonExistent() {
            assertNull(table.get("non-existent-key"));
        }
    }

    @Test
    @DisplayName("5. Обновление (update) и проверка примера из задачи")
    void testUpdateAndExample() {
        HashTable<String, Number> numTable = new HashTable<>();

        numTable.put("one", 1);
        assertEquals(1, numTable.get("one"));

        numTable.update("one", 1.0);

        assertEquals(1.0, numTable.get("one"));
        assertEquals(1, numTable.size());
    }

    @Nested
    @DisplayName("3. Удаление (remove)")
    class RemoveTests {
        @Test
        @DisplayName("remove должен удалять элемент и возвращать его значение")
        void testRemove() {
            table.put("one", 1);
            table.put("two", 2);
            assertEquals(2, table.size());

            Integer removedValue = table.remove("one");

            assertEquals(1, removedValue);
            assertEquals(1, table.size());
            assertNull(table.get("one"));
            assertEquals(2, table.get("two"));
        }

        @Test
        @DisplayName("remove несуществующего ключа должен вернуть null")
        void testRemoveNonExistent() {
            table.put("one", 1);
            Integer removedValue = table.remove("non-existent-key");

            assertNull(removedValue);
            assertEquals(1, table.size());
        }
    }

    @Test
    @DisplayName("6. Проверка наличия (containsKey)")
    void testContainsKey() {
        assertFalse(table.containsKey("one"));
        table.put("one", 1);
        assertTrue(table.containsKey("one"));

        table.put("two", 2);
        assertTrue(table.containsKey("two"));

        table.remove("one");
        assertFalse(table.containsKey("one"));
    }

    @Nested
    @DisplayName("Итерирование и ConcurrentModificationException")
    class IteratorTests {

        @BeforeEach
        void addData() {
            table.put("one", 1);
            table.put("two", 2);
            table.put("three", 3);
        }

        @Test
        @DisplayName("Итератор должен обойти все элементы")
        void testIteration() {
            Set<String> keysFound = new HashSet<>();
            int count = 0;
            
            for (Node<String, Integer> node : table) {
                keysFound.add(node.getKey());
                count++;
            }

            assertEquals(3, count);
            assertTrue(keysFound.contains("one"));
            assertTrue(keysFound.contains("two"));
            assertTrue(keysFound.contains("three"));
        }

        @Test
        @DisplayName("Итератор должен бросить ConcurrentModificationException при put")
        void testConcurrentModificationExceptionOnPut() {
            Iterator<Node<String, Integer>> iterator = table.iterator();
            iterator.next();

            table.put("four", 4);
            
            assertThrows(ConcurrentModificationException.class, () -> {
                iterator.next();
            });
        }

        @Test
        @DisplayName("Итератор должен бросить ConcurrentModificationException при remove")
        void testConcurrentModificationExceptionOnRemove() {
            Iterator<Node<String, Integer>> iterator = table.iterator();
            iterator.next();

            table.remove("one");

            assertThrows(ConcurrentModificationException.class, () -> {
                iterator.next();
            });
        }

        @Test
        void testIteratorRemove() {
            Iterator<Node<String, Integer>> iterator = table.iterator();

            Node<String, Integer> nodeToRemove = iterator.next();
            String keyToRemove = nodeToRemove.getKey();

            iterator.remove();
            assertEquals(2, table.size());
            assertFalse(table.containsKey(keyToRemove));

            assertTrue(iterator.hasNext());
            assertDoesNotThrow(() -> iterator.next());
        }

        @Test
        @DisplayName("iterator.remove() без next() должен бросить IllegalStateException")
        void testIteratorRemoveWithoutNext() {
            Iterator<Node<String, Integer>> iterator = table.iterator();
            assertThrows(IllegalStateException.class, () -> {
                iterator.remove();
            });
        }
    }

    @Test
    void testEqualsAndHashCode() {
        HashTable<String, Integer> table2 = new HashTable<>();

        table.put("one", 1);
        table.put("two", 2);

        table2.put("two", 2);
        table2.put("one", 1);

        // 1. Равенство
        assertTrue(table.equals(table2));
        assertTrue(table2.equals(table));

        // 2. Равенство с собой
        assertTrue(table.equals(table));

        // 3. hashCode
        assertEquals(table.hashCode(), table2.hashCode());

        // 4. Неравенство (другое значение)
        table2.update("one", 10);
        assertFalse(table.equals(table2));

        // 5. Неравенство (другой размер)
        table2.remove("one");
        assertFalse(table.equals(table2));

        // 6. Неравенство с null
        assertFalse(table.equals(null));

        // 7. Неравенство с другим типом
        assertFalse(table.equals(new Object()));
    }

    @Test
    @DisplayName("Вывод в строку (toString)")
    void testToString() {
        assertEquals("{}", table.toString());

        table.put("one", 1);
        table.put("two", 2);

        String str = table.toString();

        assertTrue(str.startsWith("{") && str.endsWith("}"));
        assertTrue(str.contains("one=1"));
        assertTrue(str.contains("two=2"));
        assertTrue(str.contains(", "));
    }


    @Nested
    @DisplayName("Пограничные случаи (null, коллизии, resize)")
    class EdgeCaseTests {

        @Test
        @DisplayName("Корректная работа с null ключом")
        void testNullKey() {
            table.put(null, 100); // Добавление
            assertEquals(1, table.size());
            assertEquals(100, table.get(null)); // Поиск
            assertTrue(table.containsKey(null)); // Проверка

            table.put(null, 200); // Обновление
            assertEquals(200, table.get(null));

            Integer removedValue = table.remove(null); // Удаление
            assertEquals(200, removedValue);
            assertNull(table.get(null));
            assertFalse(table.containsKey(null));
            assertEquals(0, table.size());
        }

        @Test
        @DisplayName("Корректная работа с null значением")
        void testNullValue() {
            table.put("one", 1);

            table.put("keyWithNull", null); // Добавление
            assertEquals(2, table.size());
            assertNull(table.get("keyWithNull"));
            assertTrue(table.containsKey("keyWithNull")); // get=null, containsKey=true

            table.put("one", null); // Обновление на null
            assertNull(table.get("one"));
            assertTrue(table.containsKey("one"));
        }


        class BadKey {
            final String key;

            BadKey(String key) {
                this.key = key;
            }

            @Override
            public int hashCode() {
                return 1;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                BadKey badKey = (BadKey) o;
                return Objects.equals(key, badKey.key);
            }
        }

        @Test
        @DisplayName("Учет коллизий (метод цепочек)")
        void testCollisions() {
            HashTable<BadKey, Integer> collisionTable = new HashTable<>();
            BadKey key1 = new BadKey("key1");
            BadKey key2 = new BadKey("key2");
            BadKey key3 = new BadKey("key3");

            collisionTable.put(key1, 1);
            collisionTable.put(key2, 2);
            collisionTable.put(key3, 3);

            // Все 3 элемента в одной корзине, но размер должен быть 3
            assertEquals(3, collisionTable.size());

            assertEquals(1, collisionTable.get(key1));
            assertEquals(2, collisionTable.get(key2));
            assertEquals(3, collisionTable.get(key3));

            // Удаляем элемент из середины цепочки
            assertEquals(2, collisionTable.remove(key2));
            assertEquals(2, collisionTable.size());
            assertNull(collisionTable.get(key2));

            // Оставшиеся должны быть на месте
            assertEquals(1, collisionTable.get(key1));
            assertEquals(3, collisionTable.get(key3));
        }

        @Test
        @DisplayName("Учет оптимальной памяти (resize)")
        void testResize() {
            // Стандартная емкость = 16, порог = 16.
            // Добавление 16-го элемента должно вызвать resize().

            // Добавляем 15 элементов. Resize еще не должен произойти.
            for (int i = 0; i < 15; i++) {
                table.put("key" + i, i);
            }
            assertEquals(15, table.size());

            // Добавляем 16-й элемент. Это должно вызвать resize.
            table.put("key15", 15);
            assertEquals(16, table.size());

            // Проверяем, что все 16 элементов на месте после resize.
            for (int i = 0; i < 16; i++) {
                assertEquals(i, table.get("key" + i));
            }
        }
    }
}
