package com.example.testfx;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class GlassCard extends StackPane {
    private final ScaleTransition hoverAnimation;

    public GlassCard(Node content) {
        getStyleClass().add("glass-card");
        setMaxWidth(Double.MAX_VALUE);
        setCache(true);
        setCacheHint(CacheHint.SPEED);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(14);
        shadow.setSpread(0.12);
        shadow.setOffsetY(6);
        shadow.setColor(Color.rgb(0, 0, 0, 0.45));
        setEffect(shadow);

        hoverAnimation = new ScaleTransition(Duration.millis(220), this);
        hoverAnimation.setFromX(1);
        hoverAnimation.setFromY(1);
        hoverAnimation.setToX(1.02);
        hoverAnimation.setToY(1.02);
        hoverAnimation.setInterpolator(Interpolator.EASE_BOTH);

        setOnMouseEntered(e -> playHover(true));
        setOnMouseExited(e -> playHover(false));

        getChildren().add(content);
    }

    private void playHover(boolean hover) {
        hoverAnimation.stop();
        hoverAnimation.setRate(hover ? 1 : -1);
        hoverAnimation.play();
    }
}
