package com.musicapp.core;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * Ein einfach implementierter Thread-Pool-Manager, der Threads manuell erstellt und verwaltet.
 */
public class ManualThreadPoolManager {

    private final List<WorkerThread> threads;
    private final Queue<Runnable> taskQueue;
    private volatile boolean isStopped = false;

    public ManualThreadPoolManager(int numberOfThreads) {
        taskQueue = new LinkedList<>();
        threads = new LinkedList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            WorkerThread worker = new WorkerThread(taskQueue);
            threads.add(worker);
            worker.start();  // Starte die Worker-Threads
        }
    }

    /**
     * Fügt eine Aufgabe der Warteschlange hinzu und weckt einen Thread auf, um die Aufgabe zu verarbeiten.
     *
     * @param task Die auszuführende Aufgabe.
     */
    public synchronized void submitTask(Runnable task) {
        if (isStopped) {
            throw new IllegalStateException("ThreadPool has been stopped");
        }
        taskQueue.add(task);
        notify();  // Weckt einen Thread auf, um die Aufgabe zu verarbeiten
    }

    /**
     * Plant eine wiederholte Aufgabe mit einem bestimmten Intervall.
     *
     * @param task Die auszuführende Aufgabe.
     * @param interval Das Intervall zwischen den Ausführungen in Millisekunden.
     */
    public synchronized void scheduleRepeatedTask(Runnable task, long interval) {
        submitTask(() -> {
            while (!isStopped) {
                long startTime = System.currentTimeMillis();
                task.run();
                long endTime = System.currentTimeMillis();
                long sleepTime = interval - (endTime -startTime);
                if (sleepTime > 0) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        });
    }

    /**
     * Beendet alle Threads sicher.
     */
    public synchronized void shutdown() {
        isStopped = true;
        for (WorkerThread thread : threads) {
            thread.doStop();
        }
    }

    /**
     * Wartet, bis alle Threads beendet sind.
     */
    public synchronized void awaitTermination() {
        for (WorkerThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private class WorkerThread extends Thread {

        private final Queue<Runnable> taskQueue;
        private volatile boolean isStopped = false;

        public WorkerThread(Queue<Runnable> taskQueue) {
            this.taskQueue = taskQueue;
        }

        @Override
        public void run() {
            while (!isStopped()) {
                Runnable task;
                synchronized (taskQueue) {
                    while (taskQueue.isEmpty()) {
                        try {
                            taskQueue.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    task = taskQueue.poll();
                }
                try {
                    task.run();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }

        public synchronized void doStop() {
            isStopped = true;
            this.interrupt();  // Weckt den Thread auf, falls er wartet
        }

        public synchronized boolean isStopped() {
            return isStopped;
        }
    }
}
