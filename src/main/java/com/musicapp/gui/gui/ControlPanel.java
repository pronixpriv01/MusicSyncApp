package com.musicapp.gui.gui;

import javafx.scene.control.Slider;

public class ControlPanel {

    private Slider volumeSlider;
    private Slider speedSlider;

    public ControlPanel(Slider volumeSlider, Slider speedSlider) {
        this.volumeSlider = volumeSlider;
        this.speedSlider = speedSlider;
    }

    public Slider getVolumeSlider() {
        return volumeSlider;
    }

    public Slider getSpeedSlider() {
        return speedSlider;
    }

    public void setVolumeSlider(Slider volumeSlider) {
        this.volumeSlider = volumeSlider;
    }

    public void setSpeedSlider(Slider speedSlider) {
        this.speedSlider = speedSlider;
    }
}
