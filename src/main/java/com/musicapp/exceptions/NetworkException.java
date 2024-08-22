package com.musicapp.exceptions;

/**
 * Ausnahme, die bei Netzwerkfehlern ausgelöst wird.
 */
public class NetworkException extends Exception {
    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
