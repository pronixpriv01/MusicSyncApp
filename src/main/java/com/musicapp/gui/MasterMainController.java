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
    private Button disconnectButton;
    @FXML
    private Button startVisualizerButton;
    @FXML
    private Button stopVisualizerButton;
    @FXML
    private ListView<String> clientListView;

    private static final Logger logger = LoggerFactory.getLogger(MasterMainController.class);
    private AppConfig appconfig;
    private Master master;
    private Visualizer visualizer;
    private Stage visualizerStage;
    private Main mainApp; // Reference to the Main class

    @FXML
    public void initialize() {
        appconfig = new AppConfig();
        setMaxConnections.setText(String.valueOf(appconfig.getMaxConnections()));

        disconnectButton.setOnAction(e -> stopMasterServer());
        startVisualizerButton.setOnAction(e -> startVisualizer());
        stopVisualizerButton.setOnAction(e -> stopVisualizer());
    }

    public void setAppConfig(AppConfig config) {
        this.appconfig = config;
        setMaxConnections.setText(String.valueOf(appconfig.getMaxConnections()));
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void back(ActionEvent event) {
        if (mainApp != null) {
            mainApp.showOnboardingUI();
        } else {
            logger.error("MainApp reference is null. Cannot return to the role selection.");
        }
    }

    @FXML
    public void connectToClients() {
        try {
            int maxConnections = Integer.parseInt(setMaxConnections.getText());
            appconfig.setMaxConnections(maxConnections);

            if (master == null) {
                master = new Master(appconfig, appconfig.getPort());
                master.start();
                master.setController(this);
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

    @FXML
    private void startVisualizer() {
        if (visualizerStage == null || !visualizerStage.isShowing()) {
            visualizer = new Visualizer();
            visualizer.setControlsEnabled(true);
            visualizerStage = new Stage();
            visualizer.setMainWindow(visualizerStage);
            visualizer.start(visualizerStage);
            logger.info("Visualizer gestartet.");
        } else {
            showAlert("Warnung", "Visualizer läuft bereits.", AlertType.WARNING);
        }
    }

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

    @FXML
    private void stopMasterServer() {
        if (master != null) {
            try {
                stopVisualizer();
                master.stop();
                master = null;
                logger.info("Master-Server gestoppt.");
                showAlert("Info", "Master-Server wurde gestoppt.", AlertType.INFORMATION);
                backToRoleSelection();
            } catch (Exception e) {
                logger.error("Fehler beim Stoppen des Master-Servers: ", e);
                showAlert("Fehler", "Fehler beim Stoppen des Master-Servers. Details im Log.", AlertType.ERROR);
            }
        } else {
            showAlert("Warnung", "Kein laufender Master-Server zum Stoppen.", AlertType.WARNING);
        }
    }

    @FXML
    private void backToRoleSelection() {
        if (mainApp != null) {
            mainApp.showOnboardingUI();
        } else {
            logger.error("MainApp reference is null. Cannot return to the role selection.");
        }
    }

    public void updateClientList(String clientStatus) {
        Platform.runLater(() -> clientListView.getItems().add(clientStatus));
    }

    public void updateLobbyList(List<String> clientStatuses) {
        Platform.runLater(() -> {
            clientListView.getItems().clear();
            clientListView.getItems().addAll(clientStatuses);
        });
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
