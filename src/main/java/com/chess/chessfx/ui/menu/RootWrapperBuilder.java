package com.chess.chessfx.ui.menu;

import com.chess.chessfx.chessconfig.ChessConfigModel;
import com.chess.chessfx.ui.game.GameLayoutBuilder;
import javafx.animation.FadeTransition;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import javafx.util.Duration;

public class RootWrapperBuilder implements Builder<BorderPane> {

    private BorderPane rootBorderPane;

    // Although these are fields, I should never need to access them from outside.
    // All of their customisation should be handled by the respective classes.
    private Region menuLayout;
    private Region gameLayout;


    @Override
    public BorderPane build() {
        rootBorderPane = new BorderPane();
        rootBorderPane.getStyleClass().add("root-wrapper-pane");

        ChessConfigModel chessConfig = new ChessConfigModel();

        // Layouts contain their layout changes and the accompanying animation.
        GameLayoutBuilder gameLayoutBuilder = new GameLayoutBuilder(() ->
                fadingLayoutChange(gameLayout, menuLayout), chessConfig);

        gameLayout = gameLayoutBuilder.build();

        menuLayout = new MenuLayoutBuilder(() -> {
            fadingLayoutChange(menuLayout, gameLayout);
            gameLayoutBuilder.startup(); // necessary to configure
            // and build board when switching to game
        }, chessConfig).build();

        rootBorderPane.setCenter(menuLayout);
        return rootBorderPane;
    }



    private void fadingLayoutChange(Region layout1, Region layout2) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.7), layout1);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            layout2.setOpacity(0.0);
            rootBorderPane.setCenter(layout2);
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.7), layout2);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }
}
