package com.musicapp.gui;

import com.musicapp.core.MusicApp;
import com.musicapp.util.AppConfig;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Klasse für das Onboarding-Fenster, um die Benutzerrolle auszuwählen.
 */
public class OnboardingWindow extends Application {

    private MusicApp musicApp;

    @Override
    public void start(Stage primaryStage) {
        musicApp = new MusicApp(); // Erstellt eine Instanz von MusicApp
        showOnboardingUI(primaryStage);
    }

    private void showOnboardingUI(Stage primaryStage) {
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-padding: 20;");

        Label roleLabel = new Label("Select Role:");
        Button masterButton = new Button("Master");
        masterButton.setOnAction(e -> {
            musicApp.getConfig().setMaster(true);  // Setzt die Konfiguration auf Master
            primaryStage.close(); // Schließt das Onboarding-Fenster
        });

        Button clientButton = new Button("Client");
        clientButton.setOnAction(e -> {
            musicApp.getConfig().setMaster(false); // Setzt die Konfiguration auf Client
            primaryStage.close(); // Schließt das Onboarding-Fenster
        });

        vbox.getChildren().addAll(roleLabel, masterButton, clientButton);

        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Onboarding - Select Role");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
