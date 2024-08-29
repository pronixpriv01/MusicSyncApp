package com.musicapp.gui;

import com.musicapp.network.Master;
import com.musicapp.util.AppConfig;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controller-Klasse für die Master-Hauptsteuerung. Diese Klasse steuert die
 * GUI-Interaktionen und zeigt den Status der verbundenen Clients an.
 */
public class MasterMainController {

    @FXML
    private Label headline;
    @FXML
    private TextField setMaxConnections;
    @FXML
    private Button connect;
    @FXML
    private Button disconnectButton; // Button zum Beenden des Master-Servers und Zurückkehren zur Rollenauswahl
    @FXML
    private Button startVisualizerButton; // Button zum Starten des Visualizers
    @FXML
    private Button stopVisualizerButton; // Button zum Stoppen des Visualizers
    @FXML
    private ListView<String> clientListView; // ListView für verbundene Clients

    private static final Logger logger = LoggerFactory.getLogger(MasterMainController.class);
    private AppConfig appconfig;
    private Master master;
    private Visualizer visualizer;
    private Stage visualizerStage;

    /**
     * Initialisiert die Controller-Klasse. Setzt die anfänglichen Konfigurationen und Einstellungen.
     */
    @FXML
    public void initialize() {
        appconfig = new AppConfig(); // Falls die AppConfig noch nicht gesetzt ist, eine Standardinstanz verwenden
        setMaxConnections.setText(String.valueOf(appconfig.getMaxConnections()));

        // Setze die Action für den disconnectButton
        disconnectButton.setOnAction(e -> stopMasterServer());

        // Setze die Action für den startVisualizerButton
        startVisualizerButton.setOnAction(e -> startVisualizer());

        // Setze die Action für den stopVisualizerButton
        stopVisualizerButton.setOnAction(e -> stopVisualizer());
    }

    /**
     * Setzt die App-Konfiguration.
     *
     * @param config Die AppConfig-Instanz.
     */
    public void setAppConfig(AppConfig config) {
        this.appconfig = config;
        setMaxConnections.setText(String.valueOf(appconfig.getMaxConnections())); // Aktualisiert das Textfeld mit der neuen Konfiguration
    }

    /**
     * Startet den Master-Server und akzeptiert Verbindungen von Clients.
     */
    @FXML
    public void connectToClients() {
        try {
            int maxConnections = Integer.parseInt(setMaxConnections.getText());
            appconfig.setMaxConnections(maxConnections);

            if (master == null) {
                master = new Master(appconfig, appconfig.getPort());
                master.start();
                master.setController(this);  // Setzt diesen Controller als Empfänger von Statusaktualisierungen
                logger.info("Master-Server gestartet und wartet auf Verbindungen...");
                showAlert("Erfolg", "Master-Server erfolgreich gestartet.", AlertType.INFORMATION);
            } else {
                showAlert("Warnung", "Master-Server läuft bereits.", AlertType.WARNING);
            }
        } catch (NumberFormatException e) {
            logger.error("Ungültiger Wert für maximale Verbindungen: ", e);
            showAlert("Fehler", "Bitte geben Sie eine gültige Zahl für maximale Verbindungen ein.", AlertType.ERROR);
        } catch (Exception e) {
            logger.error("Fehler beim Starten des Master-Servers: ", e);
            showAlert("Fehler", "Fehler beim Starten des Master-Servers. Details im Log.", AlertType.ERROR);
        }
    }

    /**
     * Startet den Visualizer.
     */
    @FXML
    private void startVisualizer() {
        if (visualizerStage == null || !visualizerStage.isShowing()) {
            visualizer = new Visualizer();
            visualizer.setControlsEnabled(true); // Master hat volle Kontrolle
            visualizerStage = new Stage();
            visualizer.setMainWindow(visualizerStage);
            visualizer.start(visualizerStage);
            logger.info("Visualizer gestartet.");
        } else {
            showAlert("Warnung", "Visualizer läuft bereits.", AlertType.WARNING);
        }
    }

    /**
     * Stoppt den Visualizer.
     */
    @FXML
    private void stopVisualizer() {
        if (visualizer != null && visualizerStage != null) {
            visualizerStage.close();
            visualizer.stop();
            visualizer = null;
            visualizerStage = null;
            logger.info("Visualizer gestoppt.");
            showAlert("Info", "Visualizer wurde gestoppt.", AlertType.INFORMATION);
        } else {
            showAlert("Warnung", "Kein laufender Visualizer zum Stoppen.", AlertType.WARNING);
        }
    }

    /**
     * Beendet den Master-Server und kehrt zur Rollenauswahl zurück.
     */
    @FXML
    private void stopMasterServer() {
        if (master != null) {
            try {
                stopVisualizer(); // Stoppe den Visualizer, bevor der Master gestoppt wird
                master.stop();
                master = null;
                logger.info("Master-Server gestoppt.");
                showAlert("Info", "Master-Server wurde gestoppt.", AlertType.INFORMATION);
                // Hier könnte die Logik zum Zurückkehren zur Rollenauswahl hinzugefügt werden
            } catch (Exception e) {
                logger.error("Fehler beim Stoppen des Master-Servers: ", e);
                showAlert("Fehler", "Fehler beim Stoppen des Master-Servers. Details im Log.", AlertType.ERROR);
            }
        } else {
            showAlert("Warnung", "Kein laufender Master-Server zum Stoppen.", AlertType.WARNING);
        }
    }

    /**
     * Aktualisiert die Liste der verbundenen Clients in der GUI.
     */
    public void updateClientList(String clientStatus) {
        Platform.runLater(() -> clientListView.getItems().add(clientStatus)); // Aktualisiert die ListView
    }

    /**
     * Aktualisiert die komplette Lobby-Information in der GUI.
     *
     * @param clientStatuses Eine Liste der Client-Status-Informationen.
     */
    public void updateLobbyList(List<String> clientStatuses) {
        Platform.runLater(() -> {
            clientListView.getItems().clear();
            clientListView.getItems().addAll(clientStatuses);
        });
    }

    /**
     * Zeigt eine Alert-Meldung an.
     *
     * @param title   Titel des Alerts.
     * @param message Nachricht des Alerts.
     * @param type    Typ des Alerts (Information, Warnung, Fehler).
     */
    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void back(ActionEvent actionEvent) {
    }
}
