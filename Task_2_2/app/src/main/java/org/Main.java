package org;

/**
 * Класс для демонстрации работы HashTable.
 */
public final class Main {
    private Main() {
        // Утилитарный класс
    }

    /**
     * Точка входа в программу.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        HashTable<String, Number> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        hashTable.update("one", 1.0);
        System.out.println(hashTable.get("one"));
    }
}