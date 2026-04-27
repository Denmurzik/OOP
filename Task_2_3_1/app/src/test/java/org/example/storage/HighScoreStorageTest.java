package org.example.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class HighScoreStorageTest {

    @Test
    void loadReturnsZeroWhenFileMissing(@TempDir Path tempDir) {
        File file = tempDir.resolve("missing.txt").toFile();
        HighScoreStorage storage = new HighScoreStorage(file);
        assertEquals(0, storage.load());
    }

    @Test
    void loadReadsExistingScore(@TempDir Path tempDir) throws IOException {
        File file = tempDir.resolve("score.txt").toFile();
        try (FileWriter w = new FileWriter(file)) {
            w.write("42");
        }
        HighScoreStorage storage = new HighScoreStorage(file);
        assertEquals(42, storage.load());
    }

    @Test
    void loadReturnsZeroOnCorruptedFile(@TempDir Path tempDir) throws IOException {
        File file = tempDir.resolve("bad.txt").toFile();
        try (FileWriter w = new FileWriter(file)) {
            w.write("not a number");
        }
        HighScoreStorage storage = new HighScoreStorage(file);
        assertEquals(0, storage.load());
    }

    @Test
    void saveIfBetterUpdatesWhenScoreIsHigher(@TempDir Path tempDir) {
        File file = tempDir.resolve("score.txt").toFile();
        HighScoreStorage storage = new HighScoreStorage(file);
        // первый раз — рекорд 0, новый 10 -> сохраняется
        assertTrue(storage.saveIfBetter(10));
        assertEquals(10, storage.load());
        // 20 > 10 -> обновляется
        assertTrue(storage.saveIfBetter(20));
        assertEquals(20, storage.load());
    }

    @Test
    void saveIfBetterIgnoresLowerOrEqualScore(@TempDir Path tempDir) {
        File file = tempDir.resolve("score.txt").toFile();
        HighScoreStorage storage = new HighScoreStorage(file);
        storage.saveIfBetter(10);
        // 5 <= 10 -> не сохраняется
        assertFalse(storage.saveIfBetter(5));
        assertEquals(10, storage.load());
        // 10 == 10 -> не сохраняется
        assertFalse(storage.saveIfBetter(10));
        assertEquals(10, storage.load());
    }

    @Test
    void saveIfBetterFromZero(@TempDir Path tempDir) {
        File file = tempDir.resolve("score.txt").toFile();
        HighScoreStorage storage = new HighScoreStorage(file);
        // 0 не больше дефолтного 0 -> не сохраняется
        assertFalse(storage.saveIfBetter(0));
        // 1 больше -> сохраняется
        assertTrue(storage.saveIfBetter(1));
    }
}
