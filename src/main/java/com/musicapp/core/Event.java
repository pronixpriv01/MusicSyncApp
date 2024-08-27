package com.musicapp.core;

/**
 * Repräsentiert ein generisches Ereignis, das von Event-Listenern behandelt wird.
 */
public class Event {

    private String message;

    /**
     * Konstruktor zum Erstellen eines neuen Events mit einer Nachricht.
     *
     * @param message Die Nachricht des Ereignisses.
     */
    public Event(String message) {
        this.message = message;
    }

    /**
     * Gibt die Nachricht des Ereignisses zurück.
     *
     * @return Die Nachricht des Ereignisses.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setzt die Nachricht des Ereignisses.
     *
     * @param message Die neue Nachricht des Ereignisses.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
