package com.musicapp.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MasterApplication extends Application  {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(com.musicapp.gui.MasterApplication.class.getResource("/master/masterMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 540, 380);
        stage.setTitle("Midi Orchestra Synchronizer - Konfiguration");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try {
            launch();
        } catch (Exception e) {
            e.getCause();
        }
    }
}
