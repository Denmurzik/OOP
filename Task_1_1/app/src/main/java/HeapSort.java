
/**
 * Класс, реализующий классический алгоритм пирамидальной сортировки (Heapsort).
 */
public class HeapSort {
    /**
     * arr Массив для сортировки. Может быть null или пустым.
     */
    public static void sort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }

        int n = arr.length;


        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }


        for (int i = n - 1; i > 0; i--) {
            swap(arr, 0, i);
            heapify(arr, i, 0);
        }
    }

    /**
     * Восстанавливает свойство max-heap для поддерева с корнем в узле i.
     *
     *  arr Массив, представляющий кучу.
     *  n  Размер кучи.
     *  i Индекс корневого узла поддерева.
     */
    private static void heapify(int[] arr, int n, int i) {
        int largest = i; // Инициализируем наибольший элемент как корень.
        int left = 2 * i + 1;
        int right = 2 * i + 2;


        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }
        


        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }


        if (largest != i) {
            swap(arr, i, largest);
            heapify(arr, n, largest);
        }
    }

    /**
     * Меняет местами два элемента в массиве.
     *
     * arr Массив, в котором производится обмен.
     *  i   Индекс первого элемента.
     *  j   Индекс второго элемента.
     */
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }



}