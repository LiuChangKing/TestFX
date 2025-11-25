package com.example.testfx;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        root.getStyleClass().add("app-root");

        addBackdropGlow(root);

        HBox layout = new HBox(22);
        layout.getStyleClass().add("layout");
        layout.setPadding(new Insets(26, 32, 26, 32));

        VBox sidebar = buildSidebar();
        VBox mainArea = buildMainArea();

        layout.getChildren().addAll(sidebar, mainArea);
        HBox.setHgrow(mainArea, Priority.ALWAYS);

        root.getChildren().add(layout);

        Scene scene = new Scene(root, 1280, 820);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setTitle("Dream Minecraft Launcher | JavaFX Demo");
        stage.setScene(scene);
        stage.show();
    }

    private VBox buildSidebar() {
        VBox sidebar = new VBox(24);
        sidebar.getStyleClass().add("sidebar");

        VBox brand = new VBox(6);
        Label title = new Label("DREAM");
        title.getStyleClass().add("brand-title");
        Label subtitle = new Label("MINECRAFT LAUNCHER");
        subtitle.getStyleClass().add("brand-subtitle");
        brand.getChildren().addAll(title, subtitle);

        VBox navList = new VBox(10);
        ToggleGroup navGroup = new ToggleGroup();
        navList.getChildren().addAll(
                createNavItem("Home", "⌂", navGroup, true),
                createNavItem("News", "📰", navGroup, false),
                createNavItem("Mods", "🔧", navGroup, false),
                createNavItem("Settings", "⚙", navGroup, false)
        );

        VBox filler = new VBox();
        VBox.setVgrow(filler, Priority.ALWAYS);

        sidebar.getChildren().addAll(brand, navList, filler);
        return sidebar;
    }

    private ToggleButton createNavItem(String text, String icon, ToggleGroup group, boolean selected) {
        ToggleButton button = new ToggleButton(icon + "   " + text);
        button.setToggleGroup(group);
        button.getStyleClass().add("nav-button");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setSelected(selected);
        return button;
    }

    private VBox buildMainArea() {
        VBox mainArea = new VBox(18);
        mainArea.getStyleClass().add("main-area");

        GlassCard heroCard = new GlassCard(buildHero());
        heroCard.getStyleClass().add("hero-panel");

        GlassCard launcherCard = new GlassCard(buildLauncherCard());
        launcherCard.getStyleClass().add("launcher-panel");

        mainArea.getChildren().addAll(heroCard, launcherCard);
        return mainArea;
    }

    private VBox buildHero() {
        VBox container = new VBox(12);
        container.getStyleClass().add("hero-section");

        HBox titleRow = new HBox(14);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        StackPane icon = new StackPane();
        icon.setPrefSize(54, 54);
        icon.getStyleClass().add("cube-icon");

        VBox textCol = new VBox(6);
        Label title = new Label("Welcome back, Explorer");
        title.getStyleClass().add("hero-title");
        Text sub = new Text("Launch the latest Minecraft release, manage mods, and stay in sync with your adventures.");
        sub.getStyleClass().add("hero-subtitle");
        textCol.getChildren().addAll(title, sub);

        titleRow.getChildren().addAll(icon, textCol);

        HBox quickActions = new HBox(10);
        quickActions.getStyleClass().add("quick-actions");

        Button resumeButton = new Button("Resume Last World");
        resumeButton.getStyleClass().add("pill-button");

        Button cloudButton = new Button("Sync Cloud Saves");
        cloudButton.getStyleClass().add("ghost-button");

        quickActions.getChildren().addAll(resumeButton, cloudButton);

        container.getChildren().addAll(titleRow, quickActions);
        return container;
    }

    private BorderPane buildLauncherCard() {
        BorderPane card = new BorderPane();
        card.getStyleClass().add("launcher-card");
        card.setPadding(new Insets(18));

        VBox left = new VBox(14);
        left.getStyleClass().add("version-column");

        Label sectionTitle = new Label("Latest Release");
        sectionTitle.getStyleClass().add("section-title");
        Text sectionDesc = new Text("Choose your version and jump right in. Your settings are automatically applied.");
        sectionDesc.getStyleClass().add("muted");

        ToggleGroup versionGroup = new ToggleGroup();
        VBox versions = new VBox(8);
        List<VersionItem> versionItems = List.of(
                new VersionItem("1.20.4", "Latest Release", true),
                new VersionItem("1.20.3", "", false),
                new VersionItem("1.20.2", "", false),
                new VersionItem("1.20", "", false)
        );

        for (VersionItem item : versionItems) {
            versions.getChildren().add(createVersionOption(item, versionGroup));
        }

        left.getChildren().addAll(sectionTitle, sectionDesc, versions);

        VBox right = new VBox(16);
        right.getStyleClass().add("play-column");
        right.setAlignment(Pos.CENTER);

        StackPane avatar = new StackPane();
        avatar.getStyleClass().add("avatar");
        avatar.setPrefSize(110, 110);

        Label playerName = new Label("Steve");
        playerName.getStyleClass().add("player-name");

        Button playButton = new Button("PLAY");
        playButton.getStyleClass().add("play-button");
        playButton.setMaxWidth(Double.MAX_VALUE);

        right.getChildren().addAll(avatar, playerName, playButton);

        card.setCenter(left);
        card.setRight(right);
        BorderPane.setMargin(right, new Insets(0, 6, 0, 24));
        return card;
    }

    private ToggleButton createVersionOption(VersionItem item, ToggleGroup group) {
        ToggleButton toggle = new ToggleButton();
        toggle.setToggleGroup(group);
        toggle.setSelected(item.selected);
        toggle.getStyleClass().add("version-item");
        toggle.setMaxWidth(Double.MAX_VALUE);

        Label versionLabel = new Label(item.version);
        versionLabel.getStyleClass().add("version-title");

        Text note = new Text(item.note.isEmpty() ? "" : item.note);
        note.getStyleClass().add("version-note");

        VBox textBox = new VBox(2, versionLabel, note);

        HBox content = new HBox(12, textBox);
        content.setAlignment(Pos.CENTER_LEFT);

        toggle.setGraphic(content);
        return toggle;
    }

    private void addBackdropGlow(StackPane root) {
        Circle cyan = new Circle(360, Color.web("#4cf0ff", 0.18));
        cyan.setTranslateX(-320);
        cyan.setTranslateY(-140);

        Circle violet = new Circle(320, Color.web("#6f7cff", 0.18));
        violet.setTranslateX(320);
        violet.setTranslateY(180);

        StackPane layer = new StackPane(cyan, violet);
        root.getChildren().add(layer);
        layer.toBack();

        Timeline shimmer = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(cyan.radiusProperty(), 360, Interpolator.EASE_BOTH),
                        new KeyValue(violet.radiusProperty(), 320, Interpolator.EASE_BOTH)
                ),
                new KeyFrame(Duration.seconds(4.5),
                        new KeyValue(cyan.radiusProperty(), 400, Interpolator.EASE_BOTH),
                        new KeyValue(violet.radiusProperty(), 360, Interpolator.EASE_BOTH)
                )
        );
        shimmer.setAutoReverse(true);
        shimmer.setCycleCount(Animation.INDEFINITE);
        shimmer.play();
    }

    private static class VersionItem {
        private final String version;
        private final String note;
        private final boolean selected;

        private VersionItem(String version, String note, boolean selected) {
            this.version = version;
            this.note = note;
            this.selected = selected;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
