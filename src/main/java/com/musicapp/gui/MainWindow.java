package com.musicapp.gui;

import com.musicapp.util.AppConfig;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindow {

    public static void showMasterWindow(Stage stage, AppConfig config) {
        try {
            FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource("/master/masterMain.fxml"));
            Parent root = loader.load();
            stage.setTitle("Master Control");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showClientWindow(Stage stage, AppConfig config) {
        try {
            FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource("/client/clientMain.fxml"));
            Parent root = loader.load();
            stage.setTitle("Client Control");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
