// Импортируем всё необходимое из JUnit 5
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HeapSortTest {

    // 1. ПОДГОТОВКА (Arrange) - общая для всех тестов
    // Создаём экземпляр нашего класса, который будем тестировать.
    private HeapSort sorter;

    // Эта аннотация говорит JUnit выполнять этот метод перед КАЖДЫМ тестом.
    // Это полезно, чтобы каждый тест начинался с "чистого листа".
    @BeforeEach
    void setUp() {
        sorter = new HeapSort();
    }

    // 2. ДЕЙСТВИЕ (Act) и ПРОВЕРКА (Assert) - в каждом тестовом методе

    // Аннотация @Test помечает метод как юнит-тест.
    @Test
    void shouldSortSimpleArray() {
        // Arrange (Подготовка данных для этого конкретного теста)
        int[] actualArray = {5, 2, 8, 1, 9};
        int[] expectedArray = {1, 2, 5, 8, 9};

        // Act (Действие - вызываем метод, который хотим протестировать)
        sorter.sort(actualArray);

        // Assert (Проверка - сравниваем фактический результат с ожидаемым)
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    void shouldHandleEmptyArray() {
        // Arrange
        int[] actualArray = {};
        int[] expectedArray = {};

        // Act
        sorter.sort(actualArray);

        // Assert
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    void shouldHandleAlreadySortedArray() {
        // Arrange
        int[] actualArray = {1, 2, 3, 4, 5};
        int[] expectedArray = {1, 2, 3, 4, 5};

        // Act
        sorter.sort(actualArray);

        // Assert
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    void shouldSortArrayWithDuplicateElements() {
        // Arrange
        int[] actualArray = {5, 2, 8, 2, 5, 1};
        int[] expectedArray = {1, 2, 2, 5, 5, 8};

        // Act
        sorter.sort(actualArray);

        // Assert
        assertArrayEquals(expectedArray, actualArray);
    }
}