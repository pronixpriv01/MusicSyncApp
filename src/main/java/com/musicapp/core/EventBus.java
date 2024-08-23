package com.musicapp.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Ein einfaches Event-Bus-System, das Listener für verschiedene Ereignisse registriert und diese benachrichtigt.
 */
public class EventBus {

    private final List<EventListener> listeners = new ArrayList<>();

    /**
     * Registriert einen Listener für ein Ereignis.
     *
     * @param listener Der zu registrierende Listener.
     */
    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    /**
     * Entfernt einen Listener.
     *
     * @param listener Der zu entfernende Listener.
     */
    public void unregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Löst ein Ereignis aus und benachrichtigt alle registrierten Listener.
     *
     * @param event Das auszulösende Ereignis.
     */
    public void dispatchEvent(Event event) {
        for (EventListener listener : listeners) {
            listener.handleEvent(event);
        }
    }
}
