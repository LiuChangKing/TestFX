package com.example.testfx;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HelloApplication extends Application {
    private final List<MetricCard> metricCards = new ArrayList<>();
    private final Random random = new Random();
    private Timeline liveTimeline;
    private int timeStep = 0;
    private DoubleProperty glowLevel = new SimpleDoubleProperty(0.4);
    private XYChart.Series<Number, Number> liveSeries;

    @Override
    public void start(Stage stage) {
        StackPane gradientLayer = new StackPane();
        gradientLayer.getStyleClass().add("gradient-layer");

        VBox content = new VBox(18);
        content.setPadding(new Insets(26, 32, 40, 32));
        content.getStyleClass().add("content-wrapper");

        content.getChildren().addAll(
                createHeroSection(),
                createMetricRow(),
                createLiveExperienceSection()
        );

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.getStyleClass().add("glass-scroll");
        scrollPane.setFitToWidth(true);

        gradientLayer.getChildren().add(scrollPane);

        Scene scene = new Scene(gradientLayer, 1280, 840);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setTitle("Neon Playground | JavaFX Studio");
        stage.setScene(scene);
        stage.show();

        playBackdropGlow(gradientLayer);
        startLiveNumbers();
    }

    private VBox createHeroSection() {
        Label title = new Label("Neon Playground");
        title.getStyleClass().add("hero-title");

        Label subtitle = new Label("即时预览主题、动画和实时数据，打造属于你的酷炫体验。");
        subtitle.getStyleClass().add("hero-subtitle");

        Button shimmerButton = new Button("激活流光");
        shimmerButton.getStyleClass().add("pill-button");
        shimmerButton.setOnAction(e -> triggerHeroPulse(shimmerButton));

        Button microInteractionButton = new Button("小交互合集");
        microInteractionButton.getStyleClass().add("ghost-button");
        microInteractionButton.setOnAction(e -> showMicroInteractions());

        ToggleButton themeToggle = new ToggleButton("夜幕模式");
        themeToggle.getStyleClass().add("toggle-button");
        themeToggle.selectedProperty().addListener((obs, oldV, isOn) -> {
            String theme = isOn ? "night" : "day";
            themeToggle.getScene().getRoot().setStyle("-app-theme:" + theme + ";");
        });

        ColorPicker accentPicker = new ColorPicker(Color.web("#7cf6ff"));
        accentPicker.getStyleClass().add("accent-picker");
        accentPicker.setOnAction(e -> {
            Color c = accentPicker.getValue();
            String color = String.format("rgba(%d,%d,%d,0.85)",
                    (int) (c.getRed() * 255), (int) (c.getGreen() * 255), (int) (c.getBlue() * 255));
            accentPicker.getScene().getRoot().setStyle("-accent-color: " + color + ";");
        });

        HBox actions = new HBox(12, shimmerButton, microInteractionButton, themeToggle, accentPicker);
        actions.setAlignment(Pos.CENTER_LEFT);

        GlassCard heroCard = new GlassCard(createHeroBody(title, subtitle, actions));
        heroCard.getStyleClass().add("hero-card");
        return new VBox(heroCard);
    }

    private VBox createHeroBody(Label title, Label subtitle, HBox actions) {
        VBox heroBody = new VBox(12);
        heroBody.getStyleClass().add("hero-body");

        HBox titleRow = new HBox(title, createBadge());
        titleRow.setAlignment(Pos.CENTER_LEFT);
        titleRow.setSpacing(12);

        heroBody.getChildren().addAll(titleRow, subtitle, actions);
        return heroBody;
    }

    private Node createBadge() {
        Label badge = new Label("Live");
        badge.getStyleClass().add("badge");

        Circle pulse = new Circle(6, Color.web("#ff6b6b"));
        ScaleTransition pulseAnim = new ScaleTransition(Duration.seconds(1.6), pulse);
        pulseAnim.setFromX(1);
        pulseAnim.setFromY(1);
        pulseAnim.setToX(1.6);
        pulseAnim.setToY(1.6);
        pulseAnim.setAutoReverse(true);
        pulseAnim.setCycleCount(Animation.INDEFINITE);
        pulseAnim.play();

        HBox badgeWrapper = new HBox(pulse, badge);
        badgeWrapper.setSpacing(6);
        badgeWrapper.setAlignment(Pos.CENTER);
        badgeWrapper.getStyleClass().add("live-badge");
        return badgeWrapper;
    }

    private GridPane createMetricRow() {
        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(18);

        MetricCard engagement = new MetricCard("互动率", "⚡", Color.web("#7cf6ff"), 74.2);
        MetricCard satisfaction = new MetricCard("满意度", "✨", Color.web("#ffe27a"), 89.6);
        MetricCard conversion = new MetricCard("转化率", "🚀", Color.web("#b08eff"), 61.4);

        metricCards.addAll(List.of(engagement, satisfaction, conversion));

        grid.add(engagement, 0, 0);
        grid.add(satisfaction, 1, 0);
        grid.add(conversion, 2, 0);

        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(33);
        grid.getColumnConstraints().addAll(col, col, col);

        return grid;
    }

    private HBox createLiveExperienceSection() {
        HBox liveRow = new HBox(18);
        liveRow.setAlignment(Pos.TOP_CENTER);

        GlassCard liveChart = new GlassCard(buildLiveChart());
        liveChart.getStyleClass().add("live-card");
        HBox.setHgrow(liveChart, Priority.ALWAYS);

        VBox interactionColumn = new VBox(14);
        interactionColumn.getChildren().addAll(
                createSparkCard(),
                createControlCenter()
        );
        interactionColumn.setPrefWidth(320);

        liveRow.getChildren().addAll(liveChart, interactionColumn);
        HBox.setHgrow(interactionColumn, Priority.NEVER);
        return liveRow;
    }

    private VBox buildLiveChart() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("时间");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("活跃度");

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.getStyleClass().add("neon-chart");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        liveSeries = series;
        chart.getData().add(liveSeries);

        Button pause = new Button("暂停");
        pause.getStyleClass().add("pill-button");
        pause.setOnAction(e -> toggleLiveFeed(pause));

        Slider speedSlider = new Slider(0.4, 2.0, 1.0);
        speedSlider.valueProperty().addListener((obs, oldV, newV) -> {
            if (liveTimeline != null) {
                liveTimeline.setRate(newV.doubleValue());
            }
        });
        speedSlider.getStyleClass().add("speed-slider");

        HBox controls = new HBox(12, pause, new Label("速度"), speedSlider);
        controls.setAlignment(Pos.CENTER_LEFT);

        VBox container = new VBox(14,
                buildChartHeader(),
                chart,
                controls
        );
        container.getStyleClass().add("live-wrapper");
        container.setPadding(new Insets(12));

        chart.setUserData(series);
        return container;
    }

    private Node buildChartHeader() {
        Label title = new Label("实时流线");
        title.getStyleClass().add("section-title");
        Text desc = new Text("观察随时间变化的活跃度波形，尝试暂停或加速动画。");
        desc.getStyleClass().add("muted");

        VBox vbox = new VBox(title, desc);
        vbox.setSpacing(6);
        return vbox;
    }

    private GlassCard createSparkCard() {
        Label title = new Label("互动清单");
        title.getStyleClass().add("section-title");

        ListView<String> checklist = new ListView<>();
        checklist.getItems().addAll(
                "点击卡片查看数值闪烁", "拖动速度滑块调整曲线频率", "切换夜幕模式获得暗色霓虹", "使用取色器自定义高光"
        );
        checklist.setCellFactory(list -> new ChecklistCell());
        checklist.setPrefHeight(210);

        VBox container = new VBox(10, title, checklist);
        container.getStyleClass().add("interaction-panel");
        return new GlassCard(container);
    }

    private GlassCard createControlCenter() {
        Label title = new Label("氛围控制");
        title.getStyleClass().add("section-title");

        Slider glowSlider = new Slider(0.2, 0.8, glowLevel.get());
        glowSlider.valueProperty().bindBidirectional(glowLevel);

        glowLevel.addListener((obs, oldV, newV) ->
                glowSlider.getScene().getRoot().setStyle("-glow-level: " + newV.doubleValue() + ";"));

        Label hint = new Label("调节背景雾化 & 光晕强度，营造专属氛围。");
        hint.getStyleClass().add("muted");

        VBox container = new VBox(10, title, glowSlider, hint);
        container.getStyleClass().add("control-panel");
        return new GlassCard(container);
    }

    private void triggerHeroPulse(Node node) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(480), node);
        scale.setToX(1.08);
        scale.setToY(1.08);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);

        FadeTransition fade = new FadeTransition(Duration.millis(480), node);
        fade.setFromValue(1);
        fade.setToValue(0.6);
        fade.setAutoReverse(true);
        fade.setCycleCount(2);

        ParallelTransition pulse = new ParallelTransition(scale, fade);
        pulse.play();
    }

    private void showMicroInteractions() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("交互提示");
        alert.setContentText("尝试：\n- 将鼠标悬停在卡片上感受浮动\n- 拖动速度滑块切换节奏\n- 点击激活按钮触发光效动画");
        alert.show();
    }

    private void playBackdropGlow(StackPane layer) {
        Circle glow = new Circle(320, Color.web("#5ef3ff", 0.35));
        glow.setTranslateX(-260);
        glow.setTranslateY(-140);

        Circle glow2 = new Circle(260, Color.web("#b388ff", 0.25));
        glow2.setTranslateX(320);
        glow2.setTranslateY(120);

        layer.getChildren().add(0, new StackPane(glow, glow2));

        Timeline shimmer = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(glow.scaleXProperty(), 1),
                        new KeyValue(glow.scaleYProperty(), 1),
                        new KeyValue(glow2.scaleXProperty(), 1.05),
                        new KeyValue(glow2.scaleYProperty(), 1.05)
                ),
                new KeyFrame(Duration.seconds(3.8),
                        new KeyValue(glow.scaleXProperty(), 1.2, Interpolator.EASE_BOTH),
                        new KeyValue(glow.scaleYProperty(), 1.2, Interpolator.EASE_BOTH),
                        new KeyValue(glow2.scaleXProperty(), 1.15, Interpolator.EASE_BOTH),
                        new KeyValue(glow2.scaleYProperty(), 1.15, Interpolator.EASE_BOTH)
                )
        );
        shimmer.setAutoReverse(true);
        shimmer.setCycleCount(Animation.INDEFINITE);
        shimmer.play();
    }

    private void startLiveNumbers() {
        liveTimeline = new Timeline(new KeyFrame(Duration.seconds(1.2), e -> updateMetrics()));
        liveTimeline.setCycleCount(Animation.INDEFINITE);
        liveTimeline.play();
    }

    private void toggleLiveFeed(Button pauseButton) {
        if (liveTimeline == null) {
            return;
        }
        if (liveTimeline.getStatus() == Animation.Status.RUNNING) {
            liveTimeline.pause();
            pauseButton.setText("继续");
        } else {
            liveTimeline.play();
            pauseButton.setText("暂停");
        }
    }

    private void updateMetrics() {
        for (MetricCard card : metricCards) {
            double noise = (random.nextDouble() - 0.5) * 10;
            double updated = clamp(card.getTargetValue() + noise, 40, 98);
            card.updateValue(updated);
        }

        timeStep++;
        addLivePoint();
    }

    private void addLivePoint() {
        if (liveSeries == null) {
            return;
        }

        double value = 50 + Math.sin(timeStep / 3.0) * 20 + random.nextDouble() * 8;
        liveSeries.getData().add(new XYChart.Data<>(timeStep, value));
        if (liveSeries.getData().size() > 30) {
            liveSeries.getData().remove(0);
        }
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static class ChecklistCell extends ListCell<String> {
        private final CheckBox checkBox = new CheckBox();

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                checkBox.setText(item);
                setGraphic(checkBox);
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
