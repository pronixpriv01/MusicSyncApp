package com.musicapp.core;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Verwaltet Hintergrundaufgaben, die nach einer bestimmten Verzögerung ausgeführt werden sollen.
 */
public class BackgroundTaskScheduler {

    private final List<WorkerThread> threads;
    private final Queue<ScheduledTask> taskQueue;
    private volatile boolean isStopped = false;

    /**
     * Konstruktor zur Erstellung eines Task-Schedulers mit einer bestimmten Anzahl von Threads.
     *
     * @param numberOfThreads Die Anzahl der Threads im Scheduler.
     */
    public BackgroundTaskScheduler(int numberOfThreads) {
        taskQueue = new LinkedList<>();
        threads = new LinkedList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            WorkerThread worker = new WorkerThread(taskQueue);
            threads.add(worker);
            worker.start();  // Starte die Worker-Threads
        }
    }

    /**
     * Plant eine Aufgabe zur Ausführung nach einer bestimmten Verzögerung.
     *
     * @param task  Die auszuführende Aufgabe.
     * @param delay Die Verzögerung in Millisekunden.
     */
    public synchronized void scheduleTask(Runnable task, long delay) {
        if (isStopped) {
            throw new IllegalStateException("TaskScheduler wurde gestoppt.");
        }
        taskQueue.add(new ScheduledTask(task, System.currentTimeMillis() + delay));
        notify();  // Weckt einen Thread auf, um die Aufgabe zu verarbeiten
    }

    /**
     * Beendet den Scheduler und alle geplanten Aufgaben.
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

        private final Queue<ScheduledTask> taskQueue;
        private volatile boolean isStopped = false;

        public WorkerThread(Queue<ScheduledTask> taskQueue) {
            this.taskQueue = taskQueue;
        }

        @Override
        public void run() {
            while (!isStopped()) {
                ScheduledTask scheduledTask;
                synchronized (taskQueue) {
                    while (taskQueue.isEmpty() || (scheduledTask = taskQueue.peek()).scheduledTime > System.currentTimeMillis()) {
                        try {
                            taskQueue.wait(100);  // Zeitlich begrenzt warten
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    scheduledTask = taskQueue.poll();  // Aufgabe zur Ausführung nehmen
                }
                try {
                    scheduledTask.task.run();
                } catch (RuntimeException e) {
                    // Fehlerbehandlung hier
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

    private static class ScheduledTask {
        private final Runnable task;
        private final long scheduledTime;

        public ScheduledTask(Runnable task, long scheduledTime) {
            this.task = task;
            this.scheduledTime = scheduledTime;
        }
    }
}
