package com.musicapp.network;

import com.musicapp.util.AppConfig;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.logging.Logger;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Master extends WebSocketServer {

    private static final Logger logger = Logger.getLogger(Master.class.getName());
    private Set<WebSocket> connectedClients = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public Master(AppConfig config, int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        connectedClients.add(conn);
        logger.info("Neuer Client verbunden: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        connectedClients.remove(conn);
        logger.info("Client getrennt: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        logger.info("Nachricht von Client erhalten: " + message);
        // Hier könntest du auf Nachrichten der Clients reagieren, falls nötig
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        logger.severe("Fehler im Master WebSocket: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        logger.info("Master WebSocket-Server gestartet.");
    }

    public void startServer() {
        this.start();
        logger.info("Master wartet auf Verbindungen...");
    }

    public void stopServer() throws InterruptedException {
        this.stop();
        logger.info("Master-Server gestoppt.");
    }

    public void sendCommandToClients(String command) {
        for (WebSocket client : connectedClients) {
            client.send(command);
        }
        logger.info("Befehl an alle Clients gesendet: " + command);
    }
}
