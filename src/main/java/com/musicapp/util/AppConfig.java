package com.musicapp.util;

/**
 * Die AppConfig-Klasse dient der Verwaltung der Konfigurationsparameter
 * für die MusicApp-Anwendung. Sie ermöglicht das Setzen und Abrufen von
 * Konfigurationswerten, die das Verhalten der Anwendung beeinflussen.
 */
public class AppConfig {

    private String startParameter;
    private int maxConnections;
    private boolean enableLogging;
    private boolean isMaster;
    private String serverUri = "ws://localhost:8887";

    /**
     * Konstruktor der AppConfig-Klasse.
     * Setzt die Standartwerte für die Konfiguration.
     */
    public AppConfig() {
        this.startParameter = "default";
        this.maxConnections = 10;
        this.enableLogging = true;
        this.isMaster = false;
    }

    /**
     * Gibt den aktuellen Startparameter zurück.
     *
     * @return Der aktuelle Startparameter als String.
     */
    public String getStartParameter() {
        return startParameter;
    }

    /**
     * Setzt den Startparameter.
     *
     * @param startParameter Der neue Startparameter.
     */
    public void setStartParameter(String startParameter) {
        this.startParameter = startParameter;
    }

    /**
     * Gibt die maximale Anzahl der Verbindungen zurück.
     *
     * @return Die maximale Anzahl der Verbindungen als int.
     */
    public int getMaxConnections() {
        return maxConnections;
    }

    /**
     * Setzt die maximale Anzahl der Verbindungen.
     *
     * @param maxConnections Die neue maximale Anzahl der Verbindungen.
     */
    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    /**
     * Gibt zurück, ob das Logging aktiviert ist.
     *
     * @return true, wenn das Logging aktiviert ist; false, sonst.
     */
    public boolean isEnableLogging() {
        return enableLogging;
    }

    /**
     * Aktiviert oder deaktiviert das Logging.
     *
     * @param enableLogging true, um das Logging zu aktivieren; false, um es zu deaktivieren.
     */
    public void setEnableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public String getServerUri() {
        return serverUri;
    }

    public void setServerUri(String serverUri) {
        this.serverUri = serverUri;
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "startParameter='" + startParameter + '\'' +
                ", maxConnections=" + maxConnections +
                ", enableLogging=" + enableLogging +
                ", isMaster=" + isMaster +
                ", serverUri='" + serverUri + '\'' +
                '}';
    }
}
