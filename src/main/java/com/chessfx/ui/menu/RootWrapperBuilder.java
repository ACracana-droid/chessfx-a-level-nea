package com.chessfx.ui.menu;

import com.chessfx.chessconfig.AppSettings;
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

import static com.chessfx.ui.menu.LayoutManager.Layout.MENU;

public class RootWrapperBuilder implements Builder<Pane> {


    private BorderPane appLevelBorderPane;

    // Although these are fields, I should never need to access them from outside.
    // All of their customisation should be handled by the respective classes.



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

        // This object is responsible for managing layouts from now on.
        new LayoutManager(appLevelBorderPane, popupManager,
                chessConfig, appSettings);
        return appLevelBorderPane;
    }

//    private Runnable makeSettingsLayoutSwitchRunnable() {
//        return () -> {
//            Region settingsLayout = new SettingsLayoutBuilder(appSettings, () -> layoutManager.fadingLayoutChange(menuLayout)).build();
//            fadingLayoutChange(menuLayout, settingsLayout);
//        };
//    }



}
