package com.musicapp.network;

import com.musicapp.core.ManualThreadPoolManager;
import com.musicapp.gui.Visualizer;
import com.musicapp.util.AppConfig;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Die Client-Klasse stellt eine Verbindung zum Master her und empfängt Kommandos zur synchronisierten Wiedergabe.
 */
public class Client extends WebSocketClient {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private long latency = 0;
    private long jitter = 0;
    private final ManualThreadPoolManager threadPool;
    private Stage mainWindowStage;  // Referenz zur Hauptfenster-Stage
    private Visualizer visualizer;  // Referenz zum Visualizer

    /**
     * Konstruktor zur Erstellung eines neuen Clients mit Serververbindung.
     *
     * @param config    Die AppConfig-Instanz für die Clientkonfiguration.
     * @param serverUri Die URI des Masters, mit dem der Client verbunden wird.
     * @throws URISyntaxException Wenn die Server-URI ungültig ist.
     */
    public Client(AppConfig config, String serverUri) throws URISyntaxException {
        super(new URI(serverUri));
        this.threadPool = new ManualThreadPoolManager(1);  // Erstellen Sie einen Thread-Pool mit 1 Thread
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        logger.info("Mit Master verbunden. Warte auf Startbefehl...");
        startLatencyMeasurement();
        sendStatusUpdate("CONNECTED");
    }

    @Override
    public void onMessage(String message) {
        logger.info("Befehl vom Master erhalten: {}", message);
        if (message.startsWith("start_playback")) {
            long startTime = Long.parseLong(message.split(" ")[1]);
            long currentTime = Instant.now().toEpochMilli();
            long delay = startTime - currentTime - latency;
            logger.info("Starte Wiedergabe in {} Millisekunden", delay);
            schedulePlayback(delay);
        } else if (message.equals("heartbeat")) {
            long now = Instant.now().toEpochMilli();
            send("pong " + now);
        } else if (message.startsWith("pong")) {
            long sendTime = Long.parseLong(message.split(" ")[1]);
            calculateLatency(sendTime);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        logger.info("Verbindung zum Master geschlossen. Code: {}, Grund: {}, remote: {}", code, reason, remote);
        sendStatusUpdate("DISCONNECTED");
        stopLatencyMeasurement();
    }

    @Override
    public void onError(Exception ex) {
        logger.error("Fehler im Client WebSocket: {}", ex.getMessage(), ex);
    }

    /**
     * Setzt die Hauptfenster-Stage für die Interaktion mit der GUI.
     *
     * @param mainWindowStage Die Hauptfenster-Stage.
     */
    public void setMainWindowStage(Stage mainWindowStage) {
        this.mainWindowStage = mainWindowStage;
    }

    /**
     * Startet den Visualizer und zeigt die GUI an.
     */
    private void startVisualizer() {
        Platform.runLater(() -> {
            try {
                Stage visualizerStage = new Stage();
                visualizer = new Visualizer();
                visualizer.setMainWindow(mainWindowStage);
                visualizer.start(visualizerStage);
            } catch (Exception e) {
                logger.error("Fehler beim Starten des Visualizers: ", e);
            }
        });
    }

    /**
     * Sendet eine Statusaktualisierung an den Master.
     *
     * @param status Der aktuelle Status des Clients (z.B. "CONNECTED", "DISCONNECTED").
     */
    private void sendStatusUpdate(String status) {
        send("status_update " + status);
        logger.info("Statusaktualisierung gesendet: {}", status);
    }

    /**
     * Plant die Wiedergabe basierend auf einer Verzögerung in Millisekunden.
     *
     * @param delay Die Verzögerung bis zum Wiedergabestart.
     */
    private void schedulePlayback(long delay) {
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Fehler beim Warten auf Wiedergabestart: ", e);
            }
        }
        startPlayback();
    }

    /**
     * Startet die synchronisierte Wiedergabe und den Visualizer.
     */
    private void startPlayback() {
        logger.info("Synchronisierte Wiedergabe gestartet.");
        startVisualizer(); // Starte den Visualizer
    }

    /**
     * Startet die regelmäßige Messung der Netzwerklatenz.
     */
    private void startLatencyMeasurement() {
        logger.info("Beginne mit der Messung der Netzwerklatenz...");
        threadPool.scheduleRepeatedTask(() -> {
            long sendTime = Instant.now().toEpochMilli();
            send("ping " + sendTime);
        }, 1000);  // Alle 1 Sekunde eine Latenzmessung
    }

    /**
     * Stoppt die Messung der Netzwerklatenz.
     */
    private void stopLatencyMeasurement() {
        threadPool.shutdown();
        logger.info("Latenzmessung gestoppt");
    }

    /**
     * Berechnet die Netzwerklatenz basierend auf der gesendeten Zeit.
     *
     * @param sendTime Die gesendete Zeit in Millisekunden.
     */
    private void calculateLatency(long sendTime) {
        long currentTime = Instant.now().toEpochMilli();
        long newLatency = currentTime - sendTime;
        long newJitter = Math.abs(latency - newLatency);

        latency = (latency + newLatency) / 2;  // Glättung der Latenz
        jitter = (jitter + newJitter) / 2;    // Glättung des Jitters

        logger.info("Gemessene Netzwerklatenz: {} ms, Jitter: {} ms", latency, jitter);
    }
}
