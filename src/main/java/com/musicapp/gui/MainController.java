package com.musicapp.gui;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    public Label selectRole;

    @FXML
    private Button toMainBtn;

    @FXML
    public Label welcomeText;

    @FXML
    private ComboBox<String> masterOrClient;

    @FXML
    public void initialize() {
        // Add choices to the ComboBox
        masterOrClient.getItems().addAll("Master", "Client");

        // default value
        masterOrClient.setValue("Master");
    }

    @FXML
    public void toMain() {

        if (masterOrClient.getValue().equals("Master")) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/master/masterMain.fxml"));
            try {
                Parent root = loader.load();
                Stage stage = (Stage) toMainBtn.getScene().getWindow();
                stage.setScene(new Scene(root));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}