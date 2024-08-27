package com.musicapp.gui.gui;

import com.musicapp.network.Client;  // استيراد كلاس Client من الحزمة network
import com.musicapp.network.Master;  // استيراد كلاس Master من الحزمة network
import com.musicapp.util.AppConfig;  // افتراض أن AppConfig موجود في مشروعك
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URISyntaxException;

public class Main extends Application {

    private boolean isMaster;
    private Button startVisualizerButton;
    private Stage primaryStage;  // Save reference to primary stage
    private AppConfig appConfig; // Configuration object (you might need to implement this class or adjust accordingly)
    private Master master;
    private Client client;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initialize(primaryStage);
        renderUI(primaryStage);
        primaryStage.show();
    }

    private void initialize(Stage primaryStage) {
        // Initialization code if needed
        appConfig = new AppConfig();  // Assuming you have an AppConfig class for configuration
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
            startMaster();
        });

        Button clientButton = new Button("Client");
        clientButton.setOnAction(e -> {
            isMaster = false;
            startClient();
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

    private void startMaster() {
        if (master == null) {
            master = new Master(appConfig, 8080); // Specify port or use config
            master.startServer();
        }
        System.out.println("Master started.");
    }

    private void startClient() {
        if (client == null) {
            try {
                client = new Client(appConfig, "ws://localhost:8080"); // Specify server URI or use config
                client.connectToMaster();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Client started.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
