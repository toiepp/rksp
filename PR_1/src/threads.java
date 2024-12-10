import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class threads extends Thread {
    static class MaxFinderTask extends RecursiveTask<Integer> {
        private final int[] arr;
        private final int start;
        private final int end;

        MaxFinderTask(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start < 1000) {
                // sequential processing for small chunks
                return findMaxOne(Arrays.copyOfRange(arr, start, end));
            } else {
                // split the task into two sub-tasks
                int mid = (start + end) / 2;
                MaxFinderTask leftTask = new MaxFinderTask(arr, start, mid);
                MaxFinderTask rightTask = new MaxFinderTask(arr, mid + 1, end);
                invokeAll(leftTask, rightTask);
                return Math.max(leftTask.join(), rightTask.join());
            }
        }
    }

    public static int[] generateArray(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int) (Math.random() * (n / 2));
        }
        return arr;
    }

    public static int findMaxOne(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int number : arr) {
            try {
                Thread.sleep(1);
                if (number > max) {
                    max = number;
                }
            } catch (Exception e) {
                return 0;
            }
        }
        return max;
    }

    public static int findMaxThreads(int[] arr) {
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        int threadMax = Integer.MIN_VALUE;
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
            List<Callable<Integer>> tasks = new ArrayList<>();
            int batchSize = arr.length / numberOfThreads;
            for (int i = 0; i < numberOfThreads; i++) {
                final int startIndex = i * batchSize;
                final int endIndex = (i == numberOfThreads - 1) ? arr.length : (i +
                        1) * batchSize;
                tasks.add(() -> findMaxOne(Arrays.copyOfRange(arr, startIndex, endIndex)));
            }
            List<Future<Integer>> futures = executorService.invokeAll(tasks);
            for (Future<Integer> future : futures) {
                int threadRes = future.get();
                Thread.sleep(1);
                if (threadRes > threadMax) {
                    threadMax = threadRes;
                }
            }
            executorService.shutdown();
        } catch (Exception e) {
            return 0;
        }
        return threadMax;
    }

    public static int findMaxNumberFJ(int[] arr) {
        try {
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            MaxFinderTask task = new MaxFinderTask(arr, 0, arr.length);
            return forkJoinPool.invoke(task);
        } catch (Exception e) {
            return 0;
        }
    }

    public static void main(String[] args) {
        int[] arr = generateArray(10000);
        long startTime, endTime, durationInMilliseconds;
        int res;
        System.out.println("Однопоток");
        startTime = System.nanoTime();
        res = findMaxOne(arr);
        endTime = System.nanoTime();
        durationInMilliseconds = (endTime - startTime) / 1000000;
        System.out.println("Время выполнения последовательной функции: " + durationInMilliseconds + " миллисекунд. Результат - " + res);

        System.out.println("Многопоток");
        startTime = System.nanoTime();
        res = findMaxThreads(arr);
        endTime = System.nanoTime();
        durationInMilliseconds = (endTime - startTime) / 1000000;
        System.out.println("Время выполнения многопоточной функции: " + durationInMilliseconds + " миллисекунд. Результат - " + res);

        System.out.println("FJ");
        startTime = System.nanoTime();
        res = findMaxNumberFJ(arr);
        endTime = System.nanoTime();
        durationInMilliseconds = (endTime - startTime) / 1000000;
        System.out.println("Время выполнения fork join функции: " + durationInMilliseconds + " миллисекунд. Результат - " + res);
    }
}