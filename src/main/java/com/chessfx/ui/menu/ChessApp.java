package com.chessfx.ui.menu;

import com.chessfx.chessconfig.AppSettings;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ChessApp extends Application {

    public final static String THEME_DIR = "/css/themes/";

    @Override
    public void start(Stage stage) throws IOException {
        AppSettings appSettings = new AppSettings();

        Scene scene = new Scene(new RootWrapperBuilder(appSettings).build(), 628, 746);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(
                THEME_DIR + appSettings.getThemeName() + ".css"
        )).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(
                "/css/style.css")).toExternalForm());

        appSettings.setSceneStyleSheets(scene.getStylesheets());
//...
        stage.setTitle("Chess App");
        stage.setMinHeight(400);
        stage.setMinWidth(400);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        // This happens before the start() method
//        Parameters param = getParameters();
//
//        Log.warn(getParameters().getRaw().toString());
//        Log.debug("In init method...");
//        Log.debug(System.getProperty("log.level", "INFO"));
//        String key = "log-threshold";
//        if (param.getRaw().contains(key)) {
//            int t = Integer.parseInt(param.getNamed().get("log-threshold"));
//            Log.setThreshold(t);
//        }

    }
}
