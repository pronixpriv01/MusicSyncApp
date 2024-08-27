package com.musicapp.gui.gui;

import javafx.scene.Scene;

public class SettingsWindow {

    private Scene scene;

    public SettingsWindow(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
