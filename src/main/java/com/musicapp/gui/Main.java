package com.musicapp.gui;

import com.musicapp.network.Client;
import com.musicapp.network.Master;
import com.musicapp.util.AppConfig;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hauptklasse für die GUI-Logik. Zeigt die Auswahloberfläche für Master- und Client-Modi an.
 */
public class Main extends Application {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private AppConfig config;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        config = new AppConfig(); // Initialisiere AppConfig

        showOnboardingUI(); // Zeigt die Onboarding-GUI
        primaryStage.show();
    }

    /**
     * Zeigt die Onboarding-GUI an, um die Benutzerrolle auszuwählen.
     */
    private void showOnboardingUI() {
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-padding: 20;");

        // Label und Buttons für Rollenauswahl
        Label roleLabel = new Label("Select Role:");

        Button masterButton = new Button("Master");
        masterButton.setOnAction(e -> {
            config.setMaster(true);
            startMainWindow();  // Startet die Haupt-GUI im Master-Modus
        });

        Button clientButton = new Button("Client");
        clientButton.setOnAction(e -> {
            config.setMaster(false);
            startMainWindow();  // Startet die Haupt-GUI im Client-Modus
        });

        vbox.getChildren().addAll(roleLabel, masterButton, clientButton);

        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Onboarding - Select Role");
    }

    /**
     * Startet das Hauptfenster basierend auf der gewählten Rolle.
     */
    private void startMainWindow() {
        try {
            if (config.isMaster()) {
                // Master-Fenster anzeigen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/master/masterMain.fxml"));
                Parent root = loader.load();
                MasterMainController masterController = loader.getController();
                masterController.setAppConfig(config); // Setzt die App-Konfiguration

                // Visualizer für den Master mit Steuerungselementen aktivieren
                Visualizer visualizer = new Visualizer();
                visualizer.setControlsEnabled(true);

                Scene scene = new Scene(root, 600, 400);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Master Control");
                primaryStage.show();
            } else {
                // Client-Fenster anzeigen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/clientMain.fxml"));
                Parent root = loader.load();
                ClientMainController clientController = loader.getController();
                clientController.setAppConfig(config); // Setzt die App-Konfiguration

                // Visualizer für den Client ohne Steuerungselemente aktivieren
                Visualizer visualizer = new Visualizer();
                visualizer.setControlsEnabled(false);

                Scene scene = new Scene(root, 600, 400);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Client Control");
                primaryStage.show();
            }
        } catch (Exception e) {
            logger.error("Fehler beim Starten des Hauptfensters", e);
            showAlert("Fehler", "Fehler beim Starten des Hauptfensters. Details im Log.", AlertType.ERROR);
        }
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

    public static void main(String[] args) {
        launch(args);  // Startet die JavaFX-Anwendung
    }
}
