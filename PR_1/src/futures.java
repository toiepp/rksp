import java.util.Scanner;
import java.util.concurrent.*;

public class futures {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        while (true) {
            System.out.print("Введите число (введите 'exit' для выхода): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                Integer number = Integer.parseInt(input);
                Callable<Integer> squareTask = () -> square(number);
                Future<Integer> futureResult = executorService.submit(squareTask);
                System.out.println(number + "\nРезультат: " + futureResult.get());
            } catch (Exception e) {
                System.out.println("Неправильный ввод");
            }
        }
        executorService.shutdown();
    }

    private static Integer square(Integer number) throws InterruptedException {
        int delayInSeconds = ThreadLocalRandom.current().nextInt(1, 6);
        Thread.sleep(delayInSeconds * 1000L);
        return number * number;
    }
}