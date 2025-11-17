import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для поиска вхождений подстроки в файле.
 * Позволяет находить подстроки в файлах, размер которых может превышать объем оперативной памяти.
 */
public class Finder {

    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private final int bufferSize;

    /**
     * Конструктор.
     */
    public Finder() {
        this(DEFAULT_BUFFER_SIZE);
    }

    /**
     * Конструктор для тестирования.
     * Позволяет установить произвольный размер буфера.
     *
     * @param bufferSize Размер буфера чтения в символах.
     */
    public Finder(int bufferSize) {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("Buffer size must be positive");
        }
        this.bufferSize = bufferSize;
    }

    /**
     * Находит все вхождения подстроки в файле, обрабатывая его по частям.
     *
     * @param fileName Имя файла для поиска.
     * @param substring Подстрока для поиска.
     * @return Список начальных индексов (с нуля) всех вхождений подстроки.
     * @throws IOException если возникает ошибка при чтении файла.
     */
    public List<Long> find(String fileName, String substring) throws IOException {
        List<Long> indices = new ArrayList<>();

        if (substring == null || substring.isEmpty()) {
            return indices;
        }

        char[] pattern = substring.toCharArray();
        int patternLen = pattern.length;

        char[] buffer = new char[this.bufferSize];

        if (patternLen > 0 && this.bufferSize < patternLen - 1) {
            throw new IllegalArgumentException(
                    "Buffer size must be at least pattern length - 1"
            );
        }

        int overlapLength;
        if (patternLen > 0) {
            overlapLength = patternLen - 1;
        } else {
            overlapLength = 0;
        }
        char[] overlap = new char[overlapLength];

        int overlapSize = 0;
        long globalCharOffset = 0;
        long lastReportedMatch = -1;
        StringBuilder textToSearch = new StringBuilder(bufferSize + overlapLength);

        try (Reader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(fileName),
                        StandardCharsets.UTF_8
                )
        )) {

            int charsRead;

            while (true) {
                charsRead = reader.read(buffer);
                if (charsRead == -1) {
                    break;
                }

                textToSearch.setLength(0);
                textToSearch.append(overlap, 0, overlapSize);
                textToSearch.append(buffer, 0, charsRead);

                int searchFrom = 0;
                int localIndex;

                while ((localIndex = textToSearch.indexOf(substring, searchFrom)) != -1) {
                    long globalIndex = (globalCharOffset - overlapSize) + localIndex;
                    if (globalIndex > lastReportedMatch) {
                        indices.add(globalIndex);
                        lastReportedMatch = globalIndex;
                    }
                    searchFrom = localIndex + 1;
                }

                globalCharOffset += charsRead;


                if (textToSearch.length() >= patternLen - 1) {
                    overlapSize = patternLen - 1;
                    int sourceStart = textToSearch.length() - overlapSize;
                    textToSearch.getChars(sourceStart, textToSearch.length(), overlap, 0);
                } else {
                    overlapSize = textToSearch.length();
                    textToSearch.getChars(0, textToSearch.length(), overlap, 0);
                }
            }
        }
        return indices;
    }
}