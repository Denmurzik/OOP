import java.util.Arrays;
import java.util.Random;

public class HeapSort {

    public void sort(int[] arr) {
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

    public void heapify(int[] arr, int n, int i) {
        int largest = i;
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


    public void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


    public static void measurePerformance() {
        int[] sizes = {10000, 50000, 100000, 500000, 1000000, 5000000};

        Random random = new Random();
        HeapSort sorter = new HeapSort();

        for (int size : sizes) {

            int[] array = new int[size];

            for (int i = 0; i < size; i++) {
                array[i] = random.nextInt();
            }

            long startTime = System.nanoTime();
            sorter.sort(array);
            long endTime = System.nanoTime();


            long nanos = endTime - startTime;

            double millis = nanos / 1000000.0;

            System.out.printf("sorted %d items by %.2f ms%n", size, millis);
        }

    }

    public static void main(String[] args) {

        measurePerformance();

    }
}