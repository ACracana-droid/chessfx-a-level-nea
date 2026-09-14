package com.chessfx.ui.menu;

import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.ui.game.GameLayoutBuilder;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Builder;
import javafx.util.Duration;

public class RootWrapperBuilder implements Builder<Pane> {


    private BorderPane appLevelBorderPane;

    // Although these are fields, I should never need to access them from outside.
    // All of their customisation should be handled by the respective classes.
    private Region menuLayout;
    private Region gameLayout;
    final Duration FADE_DURATION = Duration.seconds(0.4);

    AppSettings appSettings;

    public RootWrapperBuilder(AppSettings appSettings) {
        this.appSettings = appSettings;
    }


    @Override
    public Pane build() {
        Pane root = new StackPane();
        root.getChildren().add(makeAppLevelBorderPane(new PopupManager(root.getChildren(), root.heightProperty(), root.widthProperty())));
        return root;
    }

    private BorderPane makeAppLevelBorderPane(PopupManager popupManager) {
        appLevelBorderPane = new BorderPane();
        appLevelBorderPane.getStyleClass().add("root-wrapper-pane");

        ChessConfigModel chessConfig = new ChessConfigModel();

        // Layouts contain their layout changes and the accompanying animation.
        GameLayoutBuilder gameLayoutBuilder = new GameLayoutBuilder(
                () -> {
                    fadingLayoutChange(gameLayout, menuLayout);
                }, chessConfig);

        gameLayout = gameLayoutBuilder.build();
        Runnable changeLayoutToGame = () -> {
            gameLayoutBuilder.startup(() -> fadingLayoutChange(gameLayout)); // necessary to configure
            // and build board when switching to game
        };


        menuLayout = new MenuLayoutBuilder(
                changeLayoutToGame,
                makeSettingsLayoutSwitchRunnable(),
                () -> {
                    Region seeAllGamesLayout = new AllGameOptionsLayoutBuilder(
                            () -> fadingLayoutChange(menuLayout),
                            changeLayoutToGame,
                            chessConfig).build();
                    fadingLayoutChange(menuLayout, seeAllGamesLayout);
                },
                chessConfig, popupManager).build();

        appLevelBorderPane.setCenter(menuLayout);
        return appLevelBorderPane;
    }

    private Runnable makeSettingsLayoutSwitchRunnable() {
        return () -> {
            Region settingsLayout = new SettingsLayoutBuilder(appSettings, () -> fadingLayoutChange(menuLayout)).build();
            fadingLayoutChange(menuLayout, settingsLayout);
        };
    }


    private void fadeOutLayout() {
        Node layoutFrom = appLevelBorderPane.getCenter();
        FadeTransition fadeOut = new FadeTransition(FADE_DURATION, layoutFrom);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> System.out.println("Finished fade out."));
        fadeOut.play();
        System.out.println("Fading out.");
    }

    private void fadeInLayout(Region layoutTo) {
        layoutTo.setOpacity(0.0);
        appLevelBorderPane.setCenter(layoutTo);
        FadeTransition fadeIn = new FadeTransition(FADE_DURATION, layoutTo);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    private void fadingLayoutChange(Region layoutTo) {
        Node layoutFrom = appLevelBorderPane.getCenter();
        FadeTransition fadeOut = new FadeTransition(FADE_DURATION, layoutFrom);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> fadeInLayout(layoutTo));
        fadeOut.play();
    }

    private void fadingLayoutChange(Region layout1, Region layout2) {
        FadeTransition fadeOut = new FadeTransition(FADE_DURATION, layout1);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            layout2.setOpacity(0.0);
            appLevelBorderPane.setCenter(layout2);
            FadeTransition fadeIn = new FadeTransition(FADE_DURATION, layout2);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }
}
