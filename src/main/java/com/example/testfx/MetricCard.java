package com.example.testfx;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.text.DecimalFormat;

public class MetricCard extends GlassCard {
    private final Label valueLabel;
    private final ProgressBar progressBar;
    private final DecimalFormat decimalFormat = new DecimalFormat("0.0");
    private double targetValue;

    public MetricCard(String title, String icon, Color accent, double initialValue) {
        super(new VBox());
        VBox content = (VBox) getChildren().get(0);
        content.getStyleClass().add("metric-card");
        content.setSpacing(14);

        Label titleLabel = new Label(icon + "  " + title);
        titleLabel.getStyleClass().add("metric-title");

        valueLabel = new Label();
        valueLabel.setFont(Font.font("Inter", 32));
        valueLabel.getStyleClass().add("metric-value");

        progressBar = new ProgressBar(initialValue / 100.0);
        progressBar.getStyleClass().add("metric-progress");
        progressBar.setPrefWidth(260);
        progressBar.setStyle(String.format("-fx-accent: rgba(%d,%d,%d,0.9);",
                (int) (accent.getRed() * 255), (int) (accent.getGreen() * 255), (int) (accent.getBlue() * 255)));

        HBox header = new HBox(titleLabel);
        header.setAlignment(Pos.CENTER_LEFT);

        content.getChildren().addAll(header, valueLabel, progressBar);
        VBox.setVgrow(progressBar, Priority.NEVER);

        updateValue(initialValue);
    }

    public void updateValue(double newValue) {
        this.targetValue = newValue;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), progressBar.getProgress())),
                new KeyFrame(Duration.millis(380), new KeyValue(progressBar.progressProperty(), Math.min(1, newValue / 100.0)))
        );
        timeline.play();
        valueLabel.setText(decimalFormat.format(targetValue) + "%");
    }

    public double getTargetValue() {
        return targetValue;
    }
}
