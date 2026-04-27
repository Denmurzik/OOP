package org.example.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Хранилище рекорда. Сохраняет в файл в домашней директории пользователя.
 */
public class HighScoreStorage {
    private static final String FILE_NAME = ".snake_highscore";

    private final File file;

    public HighScoreStorage() {
        String home = System.getProperty("user.home");
        this.file = new File(home, FILE_NAME);
    }

    /**
     * Конструктор для тестов — позволяет указать произвольный файл.
     */
    HighScoreStorage(File file) {
        this.file = file;
    }

    /**
     * Загружает текущий рекорд из файла. Если файла нет  возвращает 0.
     */
    public int load() {
        if (!file.exists()) {
            return 0;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null) {
                return 0;
            }
            return Integer.parseInt(line.trim());
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Сохраняет новое значение, если оно больше текущего рекорда.
     * Возвращает true, если рекорд обновлён.
     */
    public boolean saveIfBetter(int score) {
        int current = load();
        if (score <= current) {
            return false;
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(Integer.toString(score));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
