package com.musicapp.gui;

import com.musicapp.util.AppConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller-Klasse für die Client-Hauptsteuerung. Diese Klasse steuert die
 * GUI-Interaktionen für den Client.
 */
public class ClientMainController {

    @FXML
    private Label statusLabel;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider speedSlider;
    @FXML
    private Button backButton; // Button zum Zurückkehren zur Rollenauswahl

    private static final Logger logger = LoggerFactory.getLogger(ClientMainController.class);
    private AppConfig appconfig;

    /**
     * Initialisiert die Controller-Klasse. Setzt die anfänglichen Konfigurationen und Einstellungen.
     */
    @FXML
    public void initialize() {
        // Hier Initialisierungslogik hinzufügen, wenn erforderlich
        statusLabel.setText("Client verbunden. Warten auf Anweisungen vom Master.");

        // Zurück zur Rollenauswahl
        backButton.setOnAction(e -> returnToRoleSelection());
    }

    /**
     * Setzt die App-Konfiguration.
     *
     * @param config Die AppConfig-Instanz.
     */
    public void setAppConfig(AppConfig config) {
        this.appconfig = config;
    }

    /**
     * Methode zum Zurückkehren zur Rollenauswahl.
     */
    private void returnToRoleSelection() {
        // Implementiere die Logik für das Zurückkehren zur Rollenauswahl hier
        logger.info("Zurück zur Rollenauswahl.");
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
}
