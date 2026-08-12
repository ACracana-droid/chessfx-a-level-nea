package com.chess.chessfx.ui.menu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ChessApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(new RootWrapperBuilder().build());
        PropertiesManager propertiesManager = new PropertiesManager();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(
                "/com/chess/chessfx/css/themes/" + propertiesManager.getThemeFileName()
        )).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(
                "/com/chess/chessfx/css/style.css")).toExternalForm());
        stage.setTitle("Chess App");
        stage.setMinHeight(400);
        stage.setMinWidth(400);
        stage.setScene(scene);
        stage.show();
    }
}
