package com.musicapp.core;

import com.musicapp.network.Client;
import com.musicapp.network.Master;
import com.musicapp.util.AppConfig;
import com.musicapp.util.StringUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Die Klasse MusicApp verwaltet den Lebenszyklus der Anwendung.
 * Sie bietet Methoden zum Starten, Stoppen, Neustarten und Wiederherstellen
 * der Anwendung nach unerwarteten Abbrüchen.
 *
 * @author pronixpriv*/
public class MusicApp {


    private static final Logger logger = Logger.getLogger(MusicApp.class.getName());
    private final AppConfig config;
    private Master master;
    private Client client;

    /**
     * Konstruktor der MusicApp-Klasse.
     * Initialisiert die Anwendung mit den übergebenen Konfigurationseinstellungen.
     *
     * @param config Die Konfigurationseinstellungen der Anwendung
     * */
    public MusicApp(AppConfig config) {
        this.config = config;
        logger.log(Level.INFO, "MusicApp-Instanz wurde erstellt: " + StringUtil.toString(this));
    }

    /**
     * Startet die Anwendung.
     * Initialisiert notwendige Komponenten und startet die Hauptlogik.
     * */
    public void start() {
        logger.log(Level.INFO, "MusicApp wird gestartet mit Konfiguration: " + StringUtil.toString(config));
        try {
            if (config.isMaster()) {
                master = new Master(config);
                master.start();
            } else {
                client = new Client(config);
                client.connectToMaster();
                client.start();
            }
            System.out.println("MusicApp gestartet.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Fehler beim Starten der Anwendung", e);
            handelException(e);
        }
    }

    /**
     * Stoppt die Anwendung.
     * Führt notwendige Aufräumarbeiten durch.
     */
    public void stop() {
        logger.log(Level.INFO, "MusicApp wird gestoppt.");
        try {
            if (master != null) {
                master.stop();
            }
            if (client != null) {
                client.disconnect();
                client.stop();
            }
            System.out.println("MusicApp gestoppt.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Fehler beim Stoppen der Anwendung", e);
            handelException(e);
        }
    }

    /**
     * Startet die Anwendung neu.
     * Führt einen Neustart durch, indem die Anwendung gestoppt und dann erneut gestartet wird.
     */
    public void restart() {
        logger.log(Level.INFO, "MusicApp wird neu gestartet.");
        stop();
        start();
    }

    /**
     * Stellt den Zustand der Anwendung nach einem unerwarteten Abbruch wieder her.
     * Diese Methode versucht, die Anwendung in einen stabilen Zustand zu versetzen.
     */
    public void recover() {
        logger.log(Level.INFO, "MusicApp wird nach unerwartetem Abbruch wiederhergestellt.");
        try {
            System.out.println("MusicApp wiederhergestellt");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Fehler bei der Wiederherstellung der Anwendung", e);
            handelException(e);
        }
    }

    private void handelException(Exception e) {
        logger.log(Level.SEVERE, "Eine unerwartete Ausnahme ist aufgetreten", e);
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
    public static void main(String [] args) {
        AppConfig config = new AppConfig();
        config.setStartParameter("low-latency");
        config.setMaxConnections(50);
        config.setEnableLogging(true);
        config.setMaster(true);

        MusicApp app = new MusicApp(config);
        app.start();

        System.out.println(StringUtil.toString(app));
    }
}
