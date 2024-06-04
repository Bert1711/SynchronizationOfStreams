package com.zaroyan;

import java.util.concurrent.*;

/**
 * @author Zaroyan
 */
public class ComplexTaskExecutor {
    private final int numberOfThreads;

    public ComplexTaskExecutor(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    // Метод для выполнения задач
    public void executeTasks(int numberOfTasks) {
        // Создаем CyclicBarrier, который будет ждать завершения всех потоков
        // и выполнять действие, указанное в Runnable, после преодоления барьера
        CyclicBarrier barrier = new CyclicBarrier(numberOfThreads, () -> {
            System.out.println("Все задания выполнены. Барьер преодолен.");
        });

        // Создаем ExecutorService с фиксированным количеством потоков, равным numberOfThreads
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        // Запускаем цикл для выполнения указанного числа задач
        for (int i = 0; i < numberOfTasks; i++) {
            executor.execute(() -> {
                // Создаем и выполняем сложную задачу
                ComplexTask task = new ComplexTask();
                task.execute();
                try {
                    // Ждем, пока все потоки выполнят свои задачи и достигнут барьера
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }

        // Завершаем работу ExecutorService после того, как все задачи будут отправлены на выполнение
        executor.shutdown();

        try {
            // Ждем завершения выполнения всех задач в течение 1 минуты
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

