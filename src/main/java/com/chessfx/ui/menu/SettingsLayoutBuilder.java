package com.chessfx.ui.menu;

import com.chessfx.chessconfig.AppSettings;
import com.chessfx.ui.util.LayoutManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
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
        rootPane.setCenter(makeSettingsVbox());

    }


    private Node makeSettingsVbox() {
        VBox vBox = new VBox(makeThemeSelectHBox(), hSeparator());
        vBox.getStyleClass().add("options-vbox");
        return vBox;
    }

    /**
     * Makes a horizontal separator node to help structure forms.
     *
     * @return horizontal separator node
     */
    private Separator hSeparator() {
        // Credit https://jenkov.com/tutorials/javafx/separator.html
        Separator separator = new Separator(Orientation.HORIZONTAL);
        // has 'separator' style class.
        return separator;
    }

    private HBox makeThemeSelectHBox() {
        HBox hBox = new HBox(makeTextInfo(), new ThemeDropDown(appSettings, themeNameProperty).build());
        hBox.getStyleClass().add("config-hbox");
        return hBox;
    }

    private Node makeTextInfo() {
        Label label = new Label("Theme:");
        return label;
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

