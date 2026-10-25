package com.chessfx.ui.menu;

import com.chessfx.chessconfig.AppSettings;

import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.log.Log;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ChessApp extends Application {

    public final static String THEME_DIR = "/css/themes/";

    AppSettings appSettings;
    ChessConfigModel chessConfig;


    @Override
    public void start(Stage stage) throws IOException {

        RootLayoutBuilder rootBuilder = new RootLayoutBuilder(appSettings, chessConfig);
        Pane root = rootBuilder.build();
        Scene scene = new Scene(root, 628, 746);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(
                THEME_DIR + appSettings.getThemeName() + ".css"
        )).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(
                "/css/style.css")).toExternalForm());

        appSettings.setSceneStyleSheets(scene.getStylesheets());
//...
        scene.setOnKeyPressed(event -> handleKeyPressed(event, rootBuilder.getAppPane()));

        stage.setTitle("Chess App");
        stage.setMinHeight(400);
        stage.setMinWidth(400);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        Log.debug("Running init() function...");
        appSettings = new AppSettings();
        chessConfig = new ChessConfigModel();
        Log.debug("Finished init() function.");


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

    private void handleKeyPressed(KeyEvent event, Pane root) {
        final double SCALE_DIFF = 0.1;
        if (event.isControlDown()) {

            if (event.getCode().equals(KeyCode.MINUS)) {
                if (root.getScaleX() < 0.5) return;
                scaleDown(root, SCALE_DIFF);
            } else if (event.getCode().equals(KeyCode.EQUALS)) {
                if (root.getScaleX() > 1.5) return;

                scaleUp(root, SCALE_DIFF);
            }
        }

    }

    private void scaleUp(Pane root, double scaleDiff) {
        root.setScaleX(root.getScaleX() + scaleDiff);
        root.setScaleY(root.getScaleY() + scaleDiff);
    }

    private void scaleDown(Pane root, double scaleDiff) {
        root.setScaleX(root.getScaleX() - scaleDiff);
        root.setScaleY(root.getScaleY() - scaleDiff);
    }
}
