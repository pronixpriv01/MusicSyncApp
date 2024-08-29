package com.musicapp.gui;

import com.musicapp.network.Master;
import com.musicapp.util.AppConfig;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private Label maxCon;
    @FXML
    private Button connect;
    @FXML
    private Button back;
    @FXML
    private ListView<String> clientListView; // ListView für verbundene Clients

    private static final Logger logger = LoggerFactory.getLogger(MasterMainController.class);
    private AppConfig appconfig;
    private Master master;

    /**
     * Initialisiert die Controller-Klasse. Setzt die anfänglichen Konfigurationen und Einstellungen.
     */
    @FXML
    public void initialize() {
        // Erstellen einer neuen AppConfig-Instanz oder Laden einer vorhandenen Konfiguration
        appconfig = new AppConfig();
        setMaxConnections.setText(String.valueOf(appconfig.getMaxConnections()));
    }

    /**
     * Handelt die Aktion zum Zurückkehren zum vorherigen Fenster.
     */
    @FXML
    public void back() {
        // Logik für das Zurückkehren zur Start-GUI (nicht gezeigt)
    }

    /**
     * Handelt die Aktion, um den Master-Server zu starten und Verbindungen von Clients zu akzeptieren.
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
     * Aktualisiert die Liste der verbundenen Clients.
     *
     * @param clientStatus Der Status des Clients (verbunden oder getrennt).
     */
    public void updateClientList(String clientStatus) {
        Platform.runLater(() -> {
            clientListView.getItems().add(clientStatus);  // Aktualisiert die ListView in der GUI
        });
    }

    /**
     * Zeigt eine Alert-Meldung auf der Benutzeroberfläche an.
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
}
