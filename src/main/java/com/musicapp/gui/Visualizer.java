package com.musicapp.gui;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javafx.scene.control.Alert;

public class Visualizer extends Application {

    private static final double DEFAULT_RADIUS = 200;
    private static final double DEFAULT_ROTATION_SPEED = 0.05;

    private double radius = DEFAULT_RADIUS;
    private double angle = 0;
    private double rotationSpeed = DEFAULT_ROTATION_SPEED;
    private int direction = 1;
    private boolean isRunning = false;
    private double centerX, centerY;
    private Circle outerCircle;
    private ShapeManager shapeManager;
    private int numSides = 5;
    private final int[] notePitches = {60, 67, 62, 69, 64, 71, 66, 61, 68, 63, 70, 65};
    private final String[] notes = {"C", "G", "D", "A", "E", "H", "Fis", "Cis", "Gis", "Es", "B", "F"};
    private final Text[] noteLabels = new Text[12];
    private Slider volumeSlider;
    private Slider speedSlider;
    private int currentVolume = 100;
    private Integer currentlyPlayingNote = null;
    private Button toggleButton;
    private Button leftButton;
    private Button rightButton;
    private Button changeShapeButton;
    private Button backButton;
    private boolean isPentagon = true;
    private Receiver receiver;
    private Stage mainStage;

    private Stage mainWindowStage;  // Reference to the main window stage

    public void setMainWindow(Stage mainWindowStage) {
        this.mainWindowStage = mainWindowStage;
    }

    @Override
    public void start(Stage primaryStage) {
        this.mainStage = primaryStage;
        Pane pane = new Pane();
        Scene scene = new Scene(pane, 800, 600);

        // Initialize MIDI
        initializeMidi();

        // Initialize outer circle
        outerCircle = new Circle(radius);
        outerCircle.setFill(null);
        outerCircle.setStroke(Color.BLACK);
        outerCircle.setStrokeWidth(3);
        pane.getChildren().add(outerCircle);

        // Initialize shape manager with default shape (pentagon)
        shapeManager = new ShapeManager(pane, numSides);

        // Initialize note labels
        for (int i = 0; i < 12; i++) {
            Text noteLabel = new Text(notes[i]);
            noteLabel.setFill(Color.RED);
            noteLabel.setTextAlignment(TextAlignment.CENTER);
            noteLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            pane.getChildren().add(noteLabel);
            noteLabels[i] = noteLabel;
        }

        // Speed control slider
        speedSlider = new Slider(0.01, 0.20, rotationSpeed);
        speedSlider.setId("Speed Slider");
        speedSlider.setShowTickMarks(true);
        speedSlider.setShowTickLabels(true);
        speedSlider.setMajorTickUnit(0.05);
        speedSlider.setMinorTickCount(4);
        speedSlider.setSnapToTicks(true);

        // Volume control slider
        volumeSlider = new Slider(0, 127, currentVolume);
        volumeSlider.setId("Volume Slider");
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setMajorTickUnit(20);
        volumeSlider.setMinorTickCount(4);
        volumeSlider.setSnapToTicks(true);

        // Control buttons
        toggleButton = createStyledButton("Start / Stop");
        leftButton = createStyledButton("← LEFT");
        rightButton = createStyledButton("RIGHT →");
        changeShapeButton = createStyledButton("Change Shape");
        backButton = createStyledButton("Back");

        toggleButton.setOnAction(e -> isRunning = !isRunning); // Toggle running state
        leftButton.setOnAction(e -> direction = -1);
        rightButton.setOnAction(e -> direction = 1);
        changeShapeButton.setOnAction(e -> toggleShape());
        backButton.setOnAction(e -> backToMain());

        // Add elements to pane
        pane.getChildren().addAll(speedSlider, volumeSlider, toggleButton, leftButton, rightButton, changeShapeButton, backButton);

        // Layout adjustments
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateLayout(pane));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateLayout(pane));

        // Initial layout update
        updateLayout(pane);

        // Animation loop for checking collisions
        new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isRunning) {
                    angle += direction * rotationSpeed;
                    shapeManager.updateShape(centerX, centerY, radius, angle);
                    rotationSpeed = speedSlider.getValue();

                    checkShapeNoteCollision();
                }
            }
        }.start();

        // Volume adjustment listener
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            currentVolume = newVal.intValue();
        });

        primaryStage.setTitle("JavaFX MIDI Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        try {
            if (receiver != null) {
                receiver.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeMidi() {
        try {
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            receiver = synthesizer.getReceiver();
        } catch (Exception e) {
            showErrorDialog("MIDI Initialization Error", "Failed to initialize MIDI.");
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        );
        return button;
    }

    private void updateLayout(Pane pane) {
        double width = pane.getWidth();
        double height = pane.getHeight();
        centerX = width / 2;
        centerY = height / 2;
        radius = Math.min(width, height) / 4;

        // Update outer circle
        outerCircle.setCenterX(centerX);
        outerCircle.setCenterY(centerY);
        outerCircle.setRadius(radius);

        // Adjust shape
        shapeManager.updateShape(centerX, centerY, radius, angle);

        // Adjust note positions
        updateNotePositions();

        // Layout buttons and sliders
        double sliderHeightOffset = radius + 60;

        // Speed slider
        speedSlider.setLayoutX(centerX - 100);
        speedSlider.setLayoutY(centerY + sliderHeightOffset);
        speedSlider.setPrefWidth(Math.min(width - 100, 200));

        // Volume slider
        volumeSlider.setLayoutX(centerX - 100);
        volumeSlider.setLayoutY(centerY - sliderHeightOffset - volumeSlider.getHeight() - 20);
        volumeSlider.setPrefWidth(Math.min(width - 100, 200));

        // Toggle (Start/Stop) button
        toggleButton.setLayoutX(centerX - toggleButton.getWidth() / 2);
        toggleButton.setLayoutY(centerY - toggleButton.getHeight() / 2);

        // Left button
        leftButton.setLayoutX(centerX - radius - leftButton.getWidth() - 40);
        leftButton.setLayoutY(centerY - leftButton.getHeight() / 2);

        // Right button
        rightButton.setLayoutX(centerX + radius + 40);
        rightButton.setLayoutY(centerY - rightButton.getHeight() / 2);

        // Change shape button
        changeShapeButton.setLayoutX(centerX - changeShapeButton.getWidth() / 2);
        changeShapeButton.setLayoutY(speedSlider.getLayoutY() + speedSlider.getHeight() + 20);

        // Back button
        backButton.setLayoutX(20);
        backButton.setLayoutY(20);
    }

    private void updateNotePositions() {
        for (int i = 0; i < 12; i++) {
            double theta = 2 * Math.PI / 12 * i;
            double x = centerX + Math.cos(theta) * radius;
            double y = centerY + Math.sin(theta) * radius;

            noteLabels[i].setX(x - noteLabels[i].getLayoutBounds().getWidth() / 2);
            noteLabels[i].setY(y + noteLabels[i].getLayoutBounds().getHeight() / 4);
        }
    }

    private void checkShapeNoteCollision() {
        boolean collisionDetected = false;
        int noteIndexToPlay = -1;

        double collisionThreshold = 20.0;

        for (Line line : shapeManager.getShapeLines()) {
            double vertexX = line.getEndX();
            double vertexY = line.getEndY();

            for (int j = 0; j < 12; j++) {
                Text noteLabel = noteLabels[j];
                double noteX = noteLabel.getX() + noteLabel.getLayoutBounds().getWidth() / 2;
                double noteY = noteLabel.getY() - noteLabel.getLayoutBounds().getHeight() / 2;
                double distance = Math.sqrt(Math.pow(vertexX - noteX, 2) + Math.pow(vertexY - noteY, 2));

                if (distance < collisionThreshold) {
                    noteIndexToPlay = j;
                    collisionDetected = true;
                    break;
                }
            }

            if (collisionDetected) {
                break;
            }
        }

        if (collisionDetected && noteIndexToPlay != -1) {
            if (currentlyPlayingNote == null || !currentlyPlayingNote.equals(noteIndexToPlay)) {
                playMidiTone(noteIndexToPlay, currentVolume);
                currentlyPlayingNote = noteIndexToPlay;

                // Add visual effect to noteLabel when it is hit
                Text noteLabel = noteLabels[noteIndexToPlay];
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), noteLabel);
                scaleTransition.setFromX(1.0);
                scaleTransition.setToX(1.5);
                scaleTransition.setFromY(1.0);
                scaleTransition.setToY(1.5);
                scaleTransition.setCycleCount(2);
                scaleTransition.setAutoReverse(true);
                scaleTransition.play();
            }
        } else {
            if (currentlyPlayingNote != null) {
                stopMidiTone(currentlyPlayingNote);
                currentlyPlayingNote = null;
            }
        }
    }

    private void playMidiTone(int noteIndex, int volume) {
        try {
            int pitch = notePitches[noteIndex];
            int velocity = volume;

            ShortMessage noteOn = new ShortMessage();
            noteOn.setMessage(ShortMessage.NOTE_ON, 0, pitch, velocity);
            receiver.send(noteOn, -1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopMidiTone(int noteIndex) {
        try {
            int pitch = notePitches[noteIndex];
            int velocity = 0;

            ShortMessage noteOff = new ShortMessage();
            noteOff.setMessage(ShortMessage.NOTE_OFF, 0, pitch, velocity);
            receiver.send(noteOff, -1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toggleShape() {
        if (isPentagon) {
            numSides = 4;
        } else {
            numSides = 5;
        }
        isPentagon = !isPentagon;
        shapeManager.removeShapeLines();
        shapeManager = new ShapeManager((Pane) toggleButton.getParent(), numSides);
    }

    private void toggleStartStop() {
        isRunning = !isRunning;
        toggleButton.setText(isRunning ? "Stop" : "Start");
    }

    private void backToMain() {
        // Close Visualizer window and show the MainWindow
        mainStage.close();
        if (mainWindowStage != null) {
            mainWindowStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

