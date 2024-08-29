package com.musicapp.gui;

import com.musicapp.util.AppConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
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
    private Button backButton;

    private static final Logger logger = LoggerFactory.getLogger(ClientMainController.class);
    private AppConfig appconfig;
    private Main mainApp; // Reference to the Main class

    /**
     * Initialisiert die Controller-Klasse. Setzt die anfänglichen Konfigurationen und Einstellungen.
     */
    @FXML
    public void initialize() {
        if (statusLabel != null) {
            statusLabel.setText("Client verbunden. Warten auf Anweisungen vom Master.");
        } else {
            logger.error("StatusLabel ist null. Überprüfen Sie das FXML-Layout.");
        }
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
     * Setzt die Referenz auf die Main-Klasse.
     *
     * @param mainApp Die Instanz der Main-Klasse.
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Methode für die Rückkehraktion bei Klick auf den "Back"-Button.
     */
    @FXML
    private void back() {
        logger.info("Back button clicked!");
        // Wechseln Sie zur Auswahl der Rollenansicht
        if (mainApp != null) {
            mainApp.showOnboardingUI();
        } else {
            logger.error("MainApp reference is null. Cannot return to the role selection.");
        }
    }
}
