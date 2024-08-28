package com.musicapp.gui;

import com.musicapp.network.NetworkFactory;
import com.musicapp.network.Master;
import com.musicapp.util.AppConfig;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(MasterMainController.class);
    private AppConfig appconfig;
    private Master master;

    /**
     * Initialisiert die Controller-Klasse.
     * Setzt die anfänglichen Konfigurationen und Einstellungen.
     */
    @FXML
    public void initialize() {
        // Erstellen einer neuen AppConfig-Instanz oder laden einer vorhandenen Konfiguration
        appconfig = new AppConfig();

        // Setzen Sie das Textfeld auf den aktuellen maxConnections-Wert
        setMaxConnections.setText(String.valueOf(appconfig.getMaxConnections()));
    }

    /**
     * Handelt die Aktion zum Zurückkehren zum vorherigen Fenster.
     */
    @FXML
    public void back() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/start/start.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) back.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    /**
     * Handelt die Aktion, um den Master-Server zu starten und Verbindungen von Clients zu akzeptieren.
     */
    @FXML
    public void connectToClients() {
        try {
            // Lesen des Wertes aus dem Textfeld und in AppConfig speichern
            int maxConnections = Integer.parseInt(setMaxConnections.getText());
            appconfig.setMaxConnections(maxConnections);

            // Logik zur Initialisierung und Start des Master-Servers über NetworkFactory
            if (master == null) {
                master = NetworkFactory.createMaster(appconfig);
                master.start();
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
