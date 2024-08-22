package com.musicapp.exceptions;

/**
 * Ausnahme, die bei Konfigurationsfehlern ausgelöst wird.
 */
public class ConfigurationException extends Exception {
    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
