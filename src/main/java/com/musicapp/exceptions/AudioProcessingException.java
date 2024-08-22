package com.musicapp.exceptions;

/**
 * Ausnahme, die bei Fehlern in der Audiobearbeitung ausgelöst wird.
 */
public class AudioProcessingException extends Exception {
    public AudioProcessingException(String message) {
        super(message);
    }

    public AudioProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
