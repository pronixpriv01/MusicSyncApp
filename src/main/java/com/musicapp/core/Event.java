package com.musicapp.core;

/**
 * Repräsentiert ein generisches Event, das von EventListenern behandelt wird.
 */
public class Event {
    // Details und Eigenschaften des Events
    private String message;

    public Event(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
