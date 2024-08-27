package com.musicapp.gui.gui;

import javafx.scene.layout.Pane;

public class ClientManagerPanel {

    private Pane pane;

    public ClientManagerPanel(Pane pane) {
        this.pane = pane;
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }
}
