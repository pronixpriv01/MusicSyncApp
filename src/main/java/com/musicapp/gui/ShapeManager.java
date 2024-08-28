package com.musicapp.gui;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class ShapeManager {

    private Pane pane;
    private int sides; // Number of sides (4 for Quadrilateral, 5 for Pentagon)
    private Line[] shapeLines;

    public ShapeManager(Pane pane, int sides) {
        this.pane = pane;
        this.sides = sides;
        drawShape();
    }

    private void drawShape() {
        removeShapeLines();

        shapeLines = new Line[sides];

        double angleStep = 2 * Math.PI / sides;
        double radius = 200; // Default radius

        for (int i = 0; i < sides; i++) {
            double startAngle = i * angleStep;
            double endAngle = (i + 1) * angleStep;

            double startX = Math.cos(startAngle) * radius + 400; // Center X
            double startY = Math.sin(startAngle) * radius + 300; // Center Y
            double endX = Math.cos(endAngle) * radius + 400; // Center X
            double endY = Math.sin(endAngle) * radius + 300; // Center Y

            Line line = new Line(startX, startY, endX, endY);
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(2);

            shapeLines[i] = line;
            pane.getChildren().add(line);
        }
    }

    public void updateShape(double centerX, double centerY, double radius, double angle) {
        double angleStep = 2 * Math.PI / sides;

        for (int i = 0; i < sides; i++) {
            double startAngle = i * angleStep + angle;
            double endAngle = (i + 1) * angleStep + angle;

            double startX = Math.cos(startAngle) * radius + centerX;
            double startY = Math.sin(startAngle) * radius + centerY;
            double endX = Math.cos(endAngle) * radius + centerX;
            double endY = Math.sin(endAngle) * radius + centerY;

            Line line = shapeLines[i];
            line.setStartX(startX);
            line.setStartY(startY);
            line.setEndX(endX);
            line.setEndY(endY);
        }
    }

    public void removeShapeLines() {
        if (shapeLines != null) {
            for (Line line : shapeLines) {
                if (line != null) {
                    pane.getChildren().remove(line);
                }
            }
        }
    }

    public Line[] getShapeLines() {
        return shapeLines;
    }
}
