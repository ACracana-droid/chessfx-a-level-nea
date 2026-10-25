package com.chessfx.ui.menu;

import com.chessfx.chessconfig.AppSettings;
import com.chessfx.ui.util.LayoutManager;
import com.chessfx.ui.util.SettingsLayoutUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.*;

import static com.chessfx.ui.util.LayoutManager.Layout.MENU;


public class SettingsLayoutBuilder extends LayoutBuilder {

    BorderPane rootPane;
    AppSettings appSettings;
    StringProperty themeNameProperty = new SimpleStringProperty();

    public SettingsLayoutBuilder(LayoutManager layoutManager,
                                 AppSettings appSettings) {
        super(null, layoutManager, LayoutManager.Layout.SETTINGS);
        this.appSettings = appSettings;
        themeNameProperty.set(appSettings.getThemeName());
        rootPane = new BorderPane();
        rootPane.setTop(returnButtonVbox("Return to main menu", () -> layoutManager.startup(MENU)));
        rootPane.setCenter(SettingsLayoutUtil.makeSettingsVbox(appSettings));
    }



    @Override
    public Region build() {
        return rootPane;
    }

    @Override
    public void startup() {
        super.startup();
    }
}

