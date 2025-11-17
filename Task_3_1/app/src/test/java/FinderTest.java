import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Юнит-тесты для класса Finder.
 */
class FinderTest {

    /**
     * JUnit 5 автоматически создаст и очистит эту временную папку
     * для каждого тестового метода.
     */
    @TempDir
    Path tempDir;

    /**
     * Вспомогательный метод для генерации тестового файла с нужным текстом.
     */
    private Path createTestFile(String fileName, String content) throws IOException {
        Path filePath = tempDir.resolve(fileName);
        Files.write(filePath, content.getBytes(StandardCharsets.UTF_8));
        return filePath;
    }

    @Test
    @DisplayName("Тест из примера: 'абракадабра' / 'бра'")
    void testFind_ExampleCase() throws IOException {
        Finder finder = new Finder();
        Path file = createTestFile("input.txt", "абракадабра");

        List<Long> expected = Arrays.asList(1L, 8L);
        List<Long> actual = finder.find(file.toString(), "бра");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Тест на отсутствие совпадений")
    void testFind_NoMatches() throws IOException {
        Finder finder = new Finder();
        Path file = createTestFile("input.txt", "абракадабра");

        List<Long> expected = Collections.emptyList();
        List<Long> actual = finder.find(file.toString(), "what");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Тест на пересекающиеся совпадения: 'azazaza' / 'aza'")
    void testFind_OverlappingMatches() throws IOException {
        Finder finder = new Finder();
        Path file = createTestFile("input.txt", "azazaza");

        List<Long> expected = Arrays.asList(0L, 2L, 4L);
        List<Long> actual = finder.find(file.toString(), "aza");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Тест на пустую подстроку")
    void testFind_EmptySubstring() throws IOException {
        Finder finder = new Finder();
        Path file = createTestFile("input.txt", "абракадабра");

        List<Long> expected = Collections.emptyList();
        List<Long> actual = finder.find(file.toString(), "");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Тест на пустой файл")
    void testFind_EmptyFile() throws IOException {
        Finder finder = new Finder();
        Path file = createTestFile("empty.txt", "");

        List<Long> expected = Collections.emptyList();
        List<Long> actual = finder.find(file.toString(), "тут пусто и нечего искать");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Тест на совпадение в самом начале")
    void testFind_MatchAtStart() throws IOException {
        Finder finder = new Finder();
        Path file = createTestFile("start.txt", "бракадабра");

        List<Long> expected = Arrays.asList(0L, 7L);
        List<Long> actual = finder.find(file.toString(), "бра");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Тест на файл, который не существует")
    void testFind_FileNotFound() {
        Finder finder = new Finder();

        assertThrows(IOException.class, () -> {
            finder.find("non_existent.txt", "а негде искать");
        });
    }

    @Test
    @DisplayName("Тест на совпадение на стыке буферов")
    void testFind_Utf8MatchAcrossBufferBoundary() throws IOException {
        // Ищем "ЁЖИК"
        // Размер буфера = 5. Размер overlap-буфера = 4 - 1 = 3
        // Контент "абвЁЖИКгд" (ЁЖИК на индексе 3)
        //
        // 1. Чтение 1:
        //    Читает "абвЁЖ"
        //    textToSearch = "абвЁЖ"
        //    overlap = "вЁЖ"
        //
        // 2. Чтение 2:
        //     Читает "ИКгд" (4 символа)
        //     textToSearch = "вЁЖ" + "ИКгд" = "вЁЖИКгд"
        //     Найдено "ЁЖИК" на локальном индексе 1
        //     globalIndex = (5 - 3) + 1 = 3
        //    Результат 3

        Finder smallBufferFinder = new Finder(5);
        Path file = createTestFile("utf8.txt", "абвЁЖИКгд");

        List<Long> expected = Collections.singletonList(3L);
        List<Long> actual = smallBufferFinder.find(file.toString(), "ЁЖИК");

        assertEquals(expected, actual);
    }
}