package com.musicapp.gui.gui;

import javafx.animation.ScaleTransition;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class NotificationManager {

    public void showNoteHitEffect(Text noteLabel) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), noteLabel);
        scaleTransition.setFromX(1.0);
        scaleTransition.setToX(1.5);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToY(1.5);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }
}
