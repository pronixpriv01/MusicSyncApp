package com.musicapp.gui;

import javafx.scene.shape.Line;

public class Pentagon {

    private Line[] pentagonLines;
    private double centerX, centerY, radius;
    private double angle;

    public Pentagon(Line[] pentagonLines) {
        this.pentagonLines = pentagonLines;
    }

    public Line[] getPentagonLines() {
        return pentagonLines;
    }

    public void setPentagonLines(Line[] pentagonLines) {
        this.pentagonLines = pentagonLines;
    }

    public void update(double centerX, double centerY, double radius, double angle) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.angle = angle;
        drawPentagon();
    }

    private void drawPentagon() {
        for (int i = 0; i < 5; i++) {
            double theta = angle + 2 * Math.PI / 5 * i;
            double x1 = centerX + Math.cos(theta) * radius;
            double y1 = centerY + Math.sin(theta) * radius;
            double x2 = centerX + Math.cos(theta + 2 * Math.PI / 5) * radius;
            double y2 = centerY + Math.sin(theta + 2 * Math.PI / 5) * radius;

            pentagonLines[i].setStartX(x1);
            pentagonLines[i].setStartY(y1);
            pentagonLines[i].setEndX(x2);
            pentagonLines[i].setEndY(y2);
        }
    }
}
