package com.musicapp.core;

import com.musicapp.network.Client;
import com.musicapp.network.Master;
import com.musicapp.util.AppConfig;
import com.musicapp.util.ConfigLoader;
import com.musicapp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Die Klasse MusicApp verwaltet den Lebenszyklus der Anwendung.
 * Sie bietet Methoden zum Starten, Stoppen, Neustarten und Wiederherstellen
 * der Anwendung nach unerwarteten Abbrüchen.
 */
public class MusicApp {

    private static final Logger logger = LoggerFactory.getLogger(MusicApp.class);
    private final AppConfig config;
    private Master master;
    private Client client;

    /**
     * Konstruktor der MusicApp-Klasse.
     * Initialisiert die Anwendung mit den übergebenen Konfigurationseinstellungen.
     *
     * @param config Die Konfigurationseinstellungen der Anwendung
     */
    public MusicApp(AppConfig config) {
        this.config = config;
        logger.info("MusicApp-Instanz wurde erstellt: {}", StringUtil.toString(this));
    }

    /**
     * Startet die Anwendung.
     * Initialisiert notwendige Komponenten und startet die Hauptlogik.
     */
    public void start() {
        logger.info("MusicApp wird gestartet mit Konfiguration: {}", StringUtil.toString(config));
        try {
            if (config.isMaster()) {
                master = new Master(config, config.getPort());
                master.start();
            } else {
                client = new Client(config, config.getServerUri());
                client.connect();
            }
            logger.info("MusicApp gestartet.");
        } catch (Exception e) {
            logger.error("Fehler beim Starten der Anwendung", e);
            handleException(e);
        }
    }

    /**
     * Stoppt die Anwendung.
     * Führt notwendige Aufräumarbeiten durch.
     */
    public void stop() {
        logger.info("MusicApp wird gestoppt.");
        try {
            if (master != null) {
                master.stop();
            }
            if (client != null) {
                client.close();
            }
            logger.info("MusicApp gestoppt.");
        } catch (Exception e) {
            logger.error("Fehler beim Stoppen der Anwendung", e);
            handleException(e);
        }
    }

    /**
     * Startet die Anwendung neu.
     * Führt einen Neustart durch, indem die Anwendung gestoppt und dann erneut gestartet wird.
     */
    public void restart() {
        logger.info("MusicApp wird neu gestartet.");
        stop();
        start();
    }

    /**
     * Stellt den Zustand der Anwendung nach einem unerwarteten Abbruch wieder her.
     */
    public void recover() {
        logger.info("MusicApp wird nach unerwartetem Abbruch wiederhergestellt.");
        try {
            // Logik zur Wiederherstellung der Anwendung
            start();
        } catch (Exception e) {
            logger.error("Fehler bei der Wiederherstellung der Anwendung", e);
            handleException(e);
        }
    }

    private void handleException(Exception e) {
        logger.error("Eine unerwartete Ausnahme ist aufgetreten", e);
    }

    /**
     * Gibt die aktuelle Konfiguration der Anwendung zurück.
     *
     * @return Die aktuelle AppConfig-Instanz.
     */
    public AppConfig getConfig() {
        return config;
    }

    @Override
    public String toString() {
        return StringUtil.toString(this);
    }

    /**
     * Hauptmethode zum Ausführen der Anwendung.
     * Diese Methode dient als Einstiegspunkt für die Ausführung der MusicApp.
     *
     * @param args Die Startparameter der Anwendung.
     */
    public static void main(String[] args) {
        try {
            AppConfig config = ConfigLoader.loadFromProperties("src/main/resources/AppConfig.properties");
            MusicApp app = new MusicApp(config);
            app.start();
        } catch (IOException e) {
            logger.error("Fehler beim Laden der Konfiguration", e);
        }
    }
}
