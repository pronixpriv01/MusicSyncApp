package com.musicapp.network;

import com.musicapp.util.AppConfig;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

public class Client extends WebSocketClient {

    private static final Logger logger = Logger.getLogger(Client.class.getName());

    public Client(AppConfig config, String serverUri) throws URISyntaxException {
        super(new URI(serverUri));
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        logger.info("Mit Master verbunden.");
    }

    @Override
    public void onMessage(String message) {
        logger.info("Befehl vom Master erhalten: " + message);
        // Hier könntest du auf Befehle des Masters reagieren und z.B. die Musikwiedergabe starten
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        logger.info("Verbindung zum Master geschlossen.");
    }

    @Override
    public void onError(Exception ex) {
        logger.severe("Fehler im Client WebSocket: " + ex.getMessage());
    }

    public void connectToMaster() {
        this.connect();
        logger.info("Versuche, eine Verbindung zum Master herzustellen...");
    }

    public void disconnectFromMaster() {
        this.close();
        logger.info("Verbindung zum Master getrennt.");
    }
}
