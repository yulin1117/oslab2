import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

// 合併排序任務類別
class MergeSortTask extends RecursiveTask<int[]> {
    private final int[] array;

    // 建構子，初始化陣列
    public MergeSortTask(int[] array) {
        this.array = array;
    }

    @Override
    protected int[] compute() {
        // 若陣列長度小於 2，直接返回（不需排序）
        if (array.length < 2) {
            return array;
        }

        // 將陣列對半拆分
        int mid = array.length / 2;
        MergeSortTask leftTask = new MergeSortTask(Arrays.copyOfRange(array, 0, mid));
        MergeSortTask rightTask = new MergeSortTask(Arrays.copyOfRange(array, mid, array.length));

        // 使用 fork() 平行處理左半部分
        leftTask.fork();
        int[] rightResult = rightTask.compute();  // 當前執行緒處理右半部分
        int[] leftResult = leftTask.join();  // 等待左半部分完成

        // 合併結果
        return merge(leftResult, rightResult);
    }

    // 合併兩個已排序的陣列
    private int[] merge(int[] left, int[] right) {
        int[] merged = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                merged[k++] = left[i++];
            } else {
                merged[k++] = right[j++];
            }
        }

        // 將剩餘元素加入結果陣列
        while (i < left.length) {
            merged[k++] = left[i++];
        }
        while (j < right.length) {
            merged[k++] = right[j++];
        }

        return merged;
    }
}

// 主程式類別
public class mergesort {
    public static void main(String[] args) {
        // 產生 1 到 100 的數字並打亂順序
        int[] array = IntStream.rangeClosed(1, 100).toArray();  // 產生 1 到 100 的數列
        shuffleArray(array);  // 打亂數列

        System.out.println("before" + Arrays.toString(array));

        // 建立 ForkJoinPool 執行緒池
        ForkJoinPool pool = new ForkJoinPool();

        // 執行合併排序任務
        MergeSortTask task = new MergeSortTask(array);
        int[] sortedArray = pool.invoke(task);

        // 輸出排序結果
        System.out.println("after" + Arrays.toString(sortedArray));
    }

    // 使用 Fisher-Yates 演算法打亂陣列
    private static void shuffleArray(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}
