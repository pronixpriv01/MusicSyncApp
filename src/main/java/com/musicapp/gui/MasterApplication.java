package com.musicapp.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Die Klasse MasterApplication stellt die GUI für den Master-Teil der Anwendung bereit.
 */
public class MasterApplication extends Application {

    private static final Logger logger = LoggerFactory.getLogger(MasterApplication.class);

    @Override
    public void start(Stage stage) throws Exception {
        try {
            logger.info("Master GUI wird gestartet.");
            // Lade die FXML-Datei für die Master-GUI
            Parent root = FXMLLoader.load(getClass().getResource("/master/masterMain.fxml"));
            Scene scene = new Scene(root, 600, 400);
            stage.setTitle("Master Control Panel");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            logger.error("Fehler beim Laden der Master GUI", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
