package com.musicapp.gui.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private boolean isMaster;
    private Button startVisualizerButton;
    private Stage primaryStage;  // Save reference to primary stage

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initialize(primaryStage);
        renderUI(primaryStage);
        primaryStage.show();
    }

    private void initialize(Stage primaryStage) {
        // Initialization code if needed
    }

    private void renderUI(Stage primaryStage) {
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-padding: 20;");

        // Label to show role information
        Label roleLabel = new Label("Select Role:");

        // Buttons to select role
        Button masterButton = new Button("Master");
        masterButton.setOnAction(e -> {
            isMaster = true;
            updateLayout(primaryStage);
        });

        Button clientButton = new Button("Client");
        clientButton.setOnAction(e -> {
            isMaster = false;
            updateLayout(primaryStage);
        });

        // Button to start the Visualizer
        startVisualizerButton = new Button("Start Visualizer");
        startVisualizerButton.setOnAction(e -> startVisualizer());

        vbox.getChildren().addAll(roleLabel, masterButton, clientButton, startVisualizerButton);

        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Main Window");

        // Listen for window resize events
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateLayout(primaryStage));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateLayout(primaryStage));
    }

    private void updateLayout(Stage primaryStage) {
        if (isMaster) {
            System.out.println("Master role selected");
        } else {
            System.out.println("Client role selected");
        }

        // Recalculate layout for the visualizer button
        double width = primaryStage.getWidth();
        double height = primaryStage.getHeight();
        startVisualizerButton.setLayoutX(width / 2 - startVisualizerButton.getWidth() / 2);
        startVisualizerButton.setLayoutY(height / 2 + 100); // Position the button
    }

    private void startVisualizer() {
        // Hide the main application stage
        primaryStage.hide();

        // Create and start the Visualizer stage
        Stage visualizerStage = new Stage();
        Visualizer visualizer = new Visualizer();
        visualizer.setMainWindow(primaryStage);  // Pass reference to the main window
        visualizer.start(visualizerStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
