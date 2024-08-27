package com.musicapp.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MasterMainController {
    @FXML
    public Label headline;
    @FXML
    public TextField setMaxConnections;
    @FXML
    public Label maxCon;
    @FXML
    private Button back;


    public void back () throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/start/start.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) back.getScene().getWindow();
        stage.setScene(new Scene(root));

    }
}
