package com.musicapp.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Die Klasse ClientApplication stellt die GUI für den Client-Teil der Anwendung bereit.
 */
public class ClientApplication extends Application {

    private static final Logger logger = LoggerFactory.getLogger(ClientApplication.class);

    @Override
    public void start(Stage stage) throws Exception {
        try {
            logger.info("Client GUI wird gestartet.");
            // Lade die FXML-Datei für die Client-GUI
            Parent root = FXMLLoader.load(getClass().getResource("/client/clientMain.fxml"));
            Scene scene = new Scene(root, 600, 400);
            stage.setTitle("Client Control Panel");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            logger.error("Fehler beim Laden der Client GUI", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
