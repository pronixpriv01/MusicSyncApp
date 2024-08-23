package com.musicapp.core;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Verwaltet Hintergrundaufgaben, die nach einer bestimmten Verzögerung ausgeführt werden sollen.
 */
public class BackgroundTaskScheduler {

    private final Timer timer = new Timer();

    /**
     * Plant eine Aufgabe zur Ausführung nach einer bestimmten Verzögerung.
     *
     * @param task Die auszuführende Aufgabe.
     * @param delay Die verzögerung in Millisekunden.
     */
    public void scheduleTask(Runnable task, long delay) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        }, delay);
    }

    /**
     * Beendet den Scheduler und alle geplanten Aufgaben.
     */
    public void shutdown() {
        timer.cancel();
    }
}
