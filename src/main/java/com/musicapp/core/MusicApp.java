package com.musicapp.core;

import com.musicapp.gui.Main;
import com.musicapp.network.Client;
import com.musicapp.network.Lobby;
import com.musicapp.network.Master;
import com.musicapp.network.NetworkFactory;
import com.musicapp.util.AppConfig;
import com.musicapp.util.ConfigLoader;
import com.musicapp.util.StringUtil;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

/**
 * Die Klasse MusicApp verwaltet den Lebenszyklus der Anwendung.
 * Sie bietet Methoden zum Starten, Stoppen, Neustarten und Wiederherstellen
 * der Anwendung nach unerwarteten Abbrüchen.
 */
public class MusicApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(MusicApp.class);
    private static AppConfig config;
    private static Master master;
    private static Client client;
    private static Lobby lobby;

    /**
     * Konstruktor der MusicApp-Klasse.
     * Initialisiert die Anwendung mit den übergebenen Konfigurationseinstellungen.
     *
     * @param config Die Konfigurationseinstellungen der Anwendung
     */
    public MusicApp(AppConfig config) {
        MusicApp.config = config;
        logger.info("MusicApp-Instanz wurde erstellt: {}", StringUtil.toString(config));
    }

    public MusicApp() {
        // Leerer Standardkonstruktor, wenn die Konfiguration später geladen wird
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Konfiguration laden, wenn sie nicht bereits gesetzt ist
            if (config == null) {
                config = ConfigLoader.loadFromProperties("src/main/resources/AppConfig.properties");
            }

            // Lobby initialisieren
            lobby = new Lobby();

            // Starten der Haupt-GUI
            Main mainGUI = new Main();
            mainGUI.start(primaryStage);

            logger.info("MusicApp gestartet.");
        } catch (Exception e) {
            logger.error("Fehler beim Starten der Anwendung", e);
            handleException(e);
        }
    }

    /**
     * Methode zum Starten der Anwendung basierend auf der aktuellen Konfiguration.
     */
    public void launchAppBasedOnConfig() throws URISyntaxException {
        if (config.isMaster()) {
            master = NetworkFactory.createMaster(config);
            master.setLobby(lobby);  // Setzt die Lobby im Master
            master.start();
            logger.info("Master gestartet auf Port: " + config.getPort());
        } else {
            client = NetworkFactory.createClient(config);
            client.connect();
            logger.info("Client verbunden mit Server: " + config.getServerUri());
        }
    }

    /**
     * Setzt die Konfiguration für die MusicApp.
     *
     * @param config Die neue AppConfig-Instanz.
     */
    public void setConfig(AppConfig config) {
        MusicApp.config = config;
    }

    /**
     * Gibt die aktuelle Konfiguration der Anwendung zurück.
     *
     * @return Die aktuelle AppConfig-Instanz.
     */
    public static AppConfig getConfig() {
        return config;
    }

    /**
     * Gibt die aktuelle Client-Instanz zurück.
     *
     * @return Die aktuelle Client-Instanz.
     */
    public static Client getClient() {
        return client;
    }

    /**
     * Gibt die aktuelle Master-Instanz zurück.
     *
     * @return Die aktuelle Master-Instanz.
     */
    public static Master getMaster() {
        return master;
    }

    /**
     * Setzt die Master-Instanz.
     *
     * @param newMaster Die neue Master-Instanz.
     */
    public static void setMaster(Master newMaster) {
        master = newMaster;
    }

    /**
     * Gibt die aktuelle Lobby-Instanz zurück.
     *
     * @return Die aktuelle Lobby-Instanz.
     */
    public static Lobby getLobby() {
        return lobby;
    }

    /**
     * Setzt die Lobby-Instanz.
     *
     * @param lobby Die Lobby-Instanz.
     */
    public void setLobby(Lobby lobby) {
        MusicApp.lobby = lobby;
    }

    /**
     * Stoppt die Anwendung.
     * Führt notwendige Aufräumarbeiten durch.
     */
    public static void stopApp() {
        logger.info("MusicApp wird gestoppt.");
        try {
            if (master != null) {
                master.stop();
                master = null;
            }
            if (client != null) {
                client.close();
                client = null;
            }
            logger.info("MusicApp gestoppt.");
        } catch (Exception e) {
            logger.error("Fehler beim Stoppen der Anwendung", e);
            e.printStackTrace();
        }
    }

    private void handleException(Exception e) {
        logger.error("Eine unerwartete Ausnahme ist aufgetreten", e);
    }

    public static void main(String[] args) {
        // Starte die JavaFX-Anwendung
        launch(args);
    }
}
