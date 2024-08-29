package com.musicapp.gui;

import javafx.animation.AnimationTimer;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

public class VisualizerController {

    private static final Logger logger = LoggerFactory.getLogger(VisualizerController.class);

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
    private int currentVolume = 100;
    private Integer currentlyPlayingNote = null;
    private boolean isPentagon = true;
    private Receiver receiver;

    @FXML
    private Pane visualizerPane;
    @FXML
    private Slider speedSlider;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Button toggleButton;
    @FXML
    private Button leftButton;
    @FXML
    private Button rightButton;
    @FXML
    private Button changeShapeButton;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        initializeVisualizer();
        initializeMidi();

        // Set the slider values
        speedSlider.setValue(rotationSpeed);
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            rotationSpeed = newVal.doubleValue();
        });

        volumeSlider.setValue(currentVolume);
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            currentVolume = newVal.intValue();
        });

        // Set action handlers for buttons
        toggleButton.setOnAction(e -> toggleStartStop());
        leftButton.setOnAction(e -> direction = -1);
        rightButton.setOnAction(e -> direction = 1);
        changeShapeButton.setOnAction(e -> toggleShape());
        backButton.setOnAction(this::backToMain);
    }

    private void initializeVisualizer() {
        outerCircle = new Circle();
        outerCircle.setFill(null);
        outerCircle.setStroke(Color.BLACK);
        outerCircle.setStrokeWidth(3);
        visualizerPane.getChildren().add(outerCircle);

        shapeManager = new ShapeManager(visualizerPane, numSides);

        for (int i = 0; i < 12; i++) {
            noteLabels[i] = new Text();
            noteLabels[i].setFill(Color.RED);
            noteLabels[i].setTextAlignment(TextAlignment.CENTER);
            noteLabels[i].setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            visualizerPane.getChildren().add(noteLabels[i]);
        }

        RotateTransition rotateTransition = new RotateTransition(Duration.millis(1000), outerCircle);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.setInterpolator(javafx.animation.Interpolator.LINEAR);
        rotateTransition.setRate(rotationSpeed);
        rotateTransition.play();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isRunning) {
                    angle += direction * rotationSpeed;
                    shapeManager.updateShape(centerX, centerY, radius, angle);
                    updateNoteLabels();
                    checkShapeNoteCollision();
                }
            }
        }.start();
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

    @FXML
    private void toggleStartStop() {
        isRunning = !isRunning;
        toggleButton.setText(isRunning ? "Stop" : "Start");
    }

    @FXML
    private void toggleShape() {
        if (isPentagon) {
            numSides = 4;
        } else {
            numSides = 5;
        }
        isPentagon = !isPentagon;
        shapeManager.removeShapeLines();
        shapeManager = new ShapeManager(visualizerPane, numSides);
    }

    private void updateNoteLabels() {
        for (int i = 0; i < 12; i++) {
            double theta = 2 * Math.PI / 12 * i;
            double x = centerX + Math.cos(theta) * radius;
            double y = centerY + Math.sin(theta) * radius;

            noteLabels[i].setText(notes[i]);
            noteLabels[i].setX(x - 10);
            noteLabels[i].setY(y + 5);
        }
    }

    private void checkShapeNoteCollision() {
        boolean collisionDetected = false;
        int noteIndexToPlay = -1;

        double collisionThreshold = 15.0;

        for (Line line : shapeManager.getShapeLines()) {
            double[] point1 = {line.getStartX(), line.getStartY()};
            double[] point2 = {line.getEndX(), line.getEndY()};

            double[][] pointsToCheck = {point1, point2};

            for (double[] point : pointsToCheck) {
                double pointX = point[0];
                double pointY = point[1];

                for (int i = 0; i < 12; i++) {
                    double noteCenterX = noteLabels[i].getX() + noteLabels[i].getLayoutBounds().getWidth() / 2;
                    double noteCenterY = noteLabels[i].getY() + noteLabels[i].getLayoutBounds().getHeight() / 2;

                    double distance = Math.sqrt(Math.pow(pointX - noteCenterX, 2) + Math.pow(pointY - noteCenterY, 2));

                    if (distance <= collisionThreshold) {
                        noteIndexToPlay = i;
                        collisionDetected = true;

                        noteLabels[i].setFill(Color.GREEN);
                        break;
                    } else {
                        noteLabels[i].setFill(Color.RED);
                    }
                }

                if (collisionDetected) {
                    break;
                }
            }

            if (collisionDetected) {
                break;
            }
        }

        if (collisionDetected) {
            if (currentlyPlayingNote != null && currentlyPlayingNote != notePitches[noteIndexToPlay]) {
                sendMidiNoteOff(currentlyPlayingNote);
            }

            currentlyPlayingNote = notePitches[noteIndexToPlay];
            sendMidiNoteOn(currentlyPlayingNote);

        } else if (currentlyPlayingNote != null) {
            sendMidiNoteOff(currentlyPlayingNote);
            currentlyPlayingNote = null;
        }
    }

    private void sendMidiNoteOn(int pitch) {
        if (receiver != null) {
            try {
                ShortMessage message = new ShortMessage();
                message.setMessage(ShortMessage.NOTE_ON, 0, pitch, currentVolume);
                receiver.send(message, -1);
            } catch (Exception e) {
                logger.error("Error sending MIDI note on", e);
            }
        }
    }

    private void sendMidiNoteOff(int pitch) {
        if (receiver != null) {
            try {
                ShortMessage message = new ShortMessage();
                message.setMessage(ShortMessage.NOTE_OFF, 0, pitch, 0);
                receiver.send(message, -1);
            } catch (Exception e) {
                logger.error("Error sending MIDI note off", e);
            }
        }
    }

    /**
     * Aktiviert oder deaktiviert die Steuerungselemente basierend auf der Benutzerrolle.
     *
     * @param enabled true, wenn die Steuerungselemente aktiviert werden sollen, false sonst.
     */
    public void setControlsEnabled(boolean enabled) {
        if (toggleButton != null) {
            toggleButton.setDisable(!enabled);
        }
        if (leftButton != null) {
            leftButton.setDisable(!enabled);
        }
        if (rightButton != null) {
            rightButton.setDisable(!enabled);
        }
        if (changeShapeButton != null) {
            changeShapeButton.setDisable(!enabled);
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void backToMain(ActionEvent actionEvent) {
        // Implement logic to return to the main window
        logger.info("Returning to the main window.");
    }


}
