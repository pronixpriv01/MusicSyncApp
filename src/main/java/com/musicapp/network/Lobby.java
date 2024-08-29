package com.musicapp.network;

import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Die Lobby-Klasse verwaltet die verbundenen Clients und stellt sicher, dass der Master die Kontrolle behält.
 */
public class Lobby {

    private static final Logger logger = LoggerFactory.getLogger(Lobby.class);
    private final Set<WebSocket> clients = Collections.synchronizedSet(new HashSet<>());
    private final Map<WebSocket, String> clientStatuses = Collections.synchronizedMap(new HashMap<>());
    private WebSocket master;

    /**
     * Fügt einen neuen Client der Lobby hinzu.
     *
     * @param client Der WebSocket des neuen Clients.
     */
    public void addClient(WebSocket client) {
        if (master == null) {
            master = client;
            logger.info("Neuer Master festgelegt: {}", client.getRemoteSocketAddress());
        } else {
            clients.add(client);
            clientStatuses.put(client, "CONNECTED");
            logger.info("Neuer Client hinzugefügt: {}", client.getRemoteSocketAddress());
        }
    }

    /**
     * Entfernt einen Client aus der Lobby.
     *
     * @param client Der zu entfernende WebSocket des Clients.
     */
    public void removeClient(WebSocket client) {
        if (client.equals(master)) {
            master = null;
            logger.info("Master entfernt. Neuer Master wird benötigt.");
        } else {
            clients.remove(client);
            clientStatuses.remove(client);
            logger.info("Client entfernt: {}", client.getRemoteSocketAddress());
        }
    }

    /**
     * Gibt den aktuellen Master zurück.
     *
     * @return Der WebSocket des aktuellen Masters.
     */
    public WebSocket getMaster() {
        return master;
    }

    /**
     * Gibt alle verbundenen Clients zurück.
     *
     * @return Eine Menge von WebSockets aller Clients.
     */
    public Set<WebSocket> getClients() {
        return new HashSet<>(clients);
    }

    /**
     * Sendet eine Nachricht an alle Clients.
     *
     * @param message Die Nachricht, die gesendet werden soll.
     */
    public void broadcast(String message) {
        synchronized (clients) {
            for (WebSocket client : clients) {
                client.send(message);
            }
        }
        logger.info("Nachricht an alle Clients gesendet: {}", message);
    }

    /**
     * Überprüft, ob ein Master vorhanden ist.
     *
     * @return true, wenn ein Master vorhanden ist; false, sonst.
     */
    public boolean hasMaster() {
        return master != null;
    }

    /**
     * Aktualisiert den Status eines bestimmten Clients.
     *
     * @param client Der WebSocket des Clients.
     * @param status Der neue Status des Clients.
     */
    public void updateClientStatus(WebSocket client, String status) {
        clientStatuses.put(client, status);
        logger.info("Status von Client {} aktualisiert: {}", client.getRemoteSocketAddress(), status);
    }

    /**
     * Gibt den Status aller Clients zurück.
     *
     * @return Eine Map, die WebSocket-Clients und ihren Status enthält.
     */
    public Map<WebSocket, String> getClientStatuses() {
        return new HashMap<>(clientStatuses);
    }
}
