package com.musicapp.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.Stage;


public class VisualizerClient extends Visualizer {


    // Konstruktor für VisualizerClient
    public VisualizerClient() {
        super(); // Aufruf des Konstruktors der Superklasse (Visualizer)
         // Initialisierung der neuen Eigenschaft
    }

    // Überschreiben einer Methode aus der Visualizer-Klasse
    @Override
    public void start(Stage primaryStage) {
        // Zuerst den ursprünglichen Code der Superklasse ausführen
        super.start(primaryStage);

        // Zusätzliche Logik, die spezifisch für VisualizerClient ist
        System.out.println("VisualizerClient wurde gestartet.");
    }

    public static void main(String[] args) {
        launch(args); // Startet die JavaFX-Anwendung
    }
}