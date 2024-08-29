package com.musicapp.network;

import com.musicapp.core.ManualThreadPoolManager;
import com.musicapp.gui.MasterMainController;
import com.musicapp.util.AppConfig;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.time.Instant;

/**
 * Die Master-Klasse steuert die Synchronisation und Verwaltung der verbundenen Clients.
 */
public class Master extends WebSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(Master.class);
    private Lobby lobby;  // Lobby als Instanzvariable
    private final ManualThreadPoolManager threadPool;
    private boolean isPlaybackRunning = false;
    private MasterMainController controller;

    // Methode zum Setzen des Controllers
    public void setController(MasterMainController controller) {
        this.controller = controller;
    }

    /**
     * Konstruktor zur Initialisierung des Master-Servers.
     *
     * @param config Die AppConfig-Instanz für die Serverkonfiguration.
     * @param port   Der Port, auf dem der Server läuft.
     */
    public Master(AppConfig config, int port) {
        super(new InetSocketAddress(port));
        this.threadPool = new ManualThreadPoolManager(3);
    }

    /**
     * Setzt die Lobby für diesen Master.
     *
     * @param lobby Die Lobby-Instanz, die vom Master verwaltet werden soll.
     */
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        lobby.addClient(conn);
        logger.info("Neuer Client oder Master verbunden: {}", conn.getRemoteSocketAddress());

        if (lobby.getMaster().equals(conn)) {
            logger.info("Der Master hat sich verbunden und hat die volle Kontrolle.");
            startHeartbeat();  // Starte das Heartbeat-System
        } else {
            logger.info("Client verbunden und wartet auf Master-Kommandos.");
        }
        updateClientStatus(conn, "CONNECTED");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        lobby.removeClient(conn);
        logger.info("Client getrennt: {}", conn.getRemoteSocketAddress());
        updateClientStatus(conn, "DISCONNECTED");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        logger.info("Nachricht von Client erhalten: {}", message);
        if (message.startsWith("status_update")) {
            String status = message.split(" ")[1];
            updateClientStatus(conn, status);
        } else if (conn.equals(lobby.getMaster())) {
            handleMasterCommand(message);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        logger.error("Fehler im Master WebSocket: {}", ex.getMessage(), ex);
    }

    @Override
    public void onStart() {
        logger.info("Master WebSocket-Server gestartet und wartet auf Verbindungen.");
    }

    /**
     * Stoppt die Steuerungslogik und schließt die Netzwerkanbindung.
     */
    @Override
    public void stop() throws InterruptedException {
        logger.info("Master wird gestoppt");
        stopHeartbeat();
        super.stop();
    }

    /**
     * Aktualisiert den Status eines Clients in der Lobby.
     *
     * @param conn   Der WebSocket des Clients.
     * @param status Der neue Status des Clients.
     */
    private void updateClientStatus(WebSocket conn, String status) {
        String clientIdentifier = conn.getRemoteSocketAddress().toString();
        lobby.updateClientStatus(conn, status);
        logger.info("Status für {} aktualisiert: {}", clientIdentifier, status);

        if (controller != null) {
            controller.updateClientList(clientIdentifier + " - " + status);
        }
    }

    /**
     * Handhabt die Befehle des Masters und führt die entsprechende Aktion aus.
     *
     * @param command Der Befehl, der vom Master empfangen wurde.
     */
    private void handleMasterCommand(String command) {
        logger.info("Master sendet Befehl: {}", command);
        switch (command.toLowerCase()) {
            case "start":
                startSynchronizedPlayback();
                break;
            case "stop":
                stopPlayback();
                break;
            case "sync":
                syncPlayback();
                break;
            default:
                logger.warn("Unbekannter Befehl vom Master empfangen: {}", command);
                break;
        }
    }

    /**
     * Startet die synchronisierte Wiedergabe durch Senden eines Timestamps an alle Clients.
     */
    private void startSynchronizedPlayback() {
        if (!isPlaybackRunning) {
            long startTime = Instant.now().plusMillis(5000).toEpochMilli(); // Startzeit in 5 Sekunden
            logger.info("Synchronisierte Wiedergabe startet um Timestamp: {}", startTime);
            lobby.broadcast("start_playback " + startTime);
            isPlaybackRunning = true;
        } else {
            logger.info("Wiedergabe läuft bereits.");
        }
    }

    /**
     * Stoppt die synchronisierte Wiedergabe.
     */
    private void stopPlayback() {
        if (isPlaybackRunning) {
            logger.info("Synchronisierte Wiedergabe wird gestoppt.");
            lobby.broadcast("stop_playback");
            isPlaybackRunning = false;
        } else {
            logger.info("Keine laufende Wiedergabe zum Stoppen.");
        }
    }

    /**
     * Synchronisiert die Wiedergabe mit allen Clients.
     */
    private void syncPlayback() {
        if (isPlaybackRunning) {
            logger.info("Synchronisiere laufende Wiedergabe mit allen Clients.");
            long syncTime = Instant.now().plusMillis(2000).toEpochMilli(); // Synchronisationszeit in 2 Sekunden
            lobby.broadcast("sync_playback " + syncTime);
        } else {
            logger.info("Keine laufende Wiedergabe zum Synchronisieren.");
        }
    }

    /**
     * Startet das Senden von Heartbeat-Nachrichten an alle Clients.
     */
    private void startHeartbeat() {
        threadPool.scheduleRepeatedTask(() -> {
            lobby.broadcast("heartbeat");
            logger.info("Heartbeat-Nachricht gesendet");
        }, 1000);  // Sende alle 1 Sekunde ein Heartbeat
    }

    /**
     * Stoppt das Heartbeat-System.
     */
    public void stopHeartbeat() {
        threadPool.shutdown();
        logger.info("Heartbeat-System gestoppt");
    }
}
