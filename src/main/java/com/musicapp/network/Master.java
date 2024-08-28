package com.musicapp.network;

import com.musicapp.core.ManualThreadPoolManager;
import com.musicapp.util.AppConfig;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Die Master-Klasse steuert die Synchronisation und Verwaltung der verbundenen Clients.
 */
public class Master extends WebSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(Master.class);
    private final Lobby lobby = new Lobby();
    private final ManualThreadPoolManager threadPool;

    /**
     * Konstruktor zur Initialisierung des Master-Servers.
     *
     * @param config Die AppConfig-Instanz für die Serverkonfiguration.
     * @param port   Der Port, auf dem der Server läuft.
     */
    public Master(AppConfig config, int port, ManualThreadPoolManager threadPoolManager) {
        super(new InetSocketAddress(port));
        this.threadPool = new ManualThreadPoolManager(3);
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
            logger.info("Master sendet Befehl: {}", message);
            if (message.equalsIgnoreCase("start")) {
                startSynchronizedPlayback();
            }
            lobby.broadcast(message);
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
     * Aktualisiert den Status eines Clients in der Lobby.
     *
     * @param conn   Der WebSocket des Clients.
     * @param status Der neue Status des Clients.
     */
    private void updateClientStatus(WebSocket conn, String status) {
        String clientIdentifier = conn.getRemoteSocketAddress().toString();
        logger.info("Status für {} aktualisiert: {}", clientIdentifier, status);
        // Logik zur Aktualisierung des Status in der GUI
    }

    /**
     * Startet die synchronisierte Wiedergabe durch Senden eines Timestamps an alle Clients.
     */
    private void startSynchronizedPlayback() {
        long startTime = Instant.now().plusMillis(5000).toEpochMilli(); // Startzeit in 5 Sekunden
        logger.info("Synchronisierte Wiedergabe startet um Timestamp: {}", startTime);
        lobby.broadcast("start_playback " + startTime);
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
