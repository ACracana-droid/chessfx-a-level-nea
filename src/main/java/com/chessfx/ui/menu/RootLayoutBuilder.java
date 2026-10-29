package com.chessfx.ui.menu;

import com.chessfx.chessconfig.AppSettings;
import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.ui.util.LayoutManager;
import com.chessfx.ui.util.PopupManager;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Builder;

public class RootLayoutBuilder implements Builder<Pane> {


    // Although these are fields, I should never need to access them from outside.
    // All of their customisation should be handled by the respective classes.


    private final AppSettings appSettings;
    private final ChessConfigModel chessConfig;
    private final BorderPane appPane;
    private final Pane root;

    public RootLayoutBuilder(AppSettings appSettings, ChessConfigModel chessConfig) {
        this.appSettings = appSettings;
        this.chessConfig = chessConfig;

        root = new StackPane();
        appPane = makeAppLevelBorderPane(
                new PopupManager(root, root.heightProperty(), root.widthProperty()));
        root.getChildren().addAll(appPane);
    }


    @Override
    public Pane build() {
        return root;
    }

    public BorderPane getAppPane() {
        return appPane;
    }


    private BorderPane makeAppLevelBorderPane(PopupManager popupManager) {
        BorderPane boarderPane = new BorderPane();
        boarderPane.getStyleClass().add("root-wrapper-pane");

        // This object is responsible for managing layouts from now on.
        new LayoutManager(boarderPane, popupManager,
                chessConfig, appSettings);
        return boarderPane;
    }

}
