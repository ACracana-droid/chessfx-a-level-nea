package com.chessfx.ui.menu;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.util.Builder;


public class SettingsGUIBuilder implements Builder<Region> {

    BorderPane rootPane;

    AppSettings appSettings;

    StringProperty themeNameProperty = new SimpleStringProperty();

    public SettingsGUIBuilder(AppSettings appSettings, Runnable changeLayoutToMenu) {
        this.appSettings = appSettings;
        themeNameProperty.set(appSettings.getThemeName());
        rootPane = new BorderPane();
        rootPane.setTop(new ReturnButton(changeLayoutToMenu, "Return to main menu").getAsVBox());
        rootPane.setCenter(makeVBox());
    }

    private Node makeVBox() {
        VBox vBox = new VBox(makeThemeSelectHBox());
        vBox.getStyleClass().add("options-vbox");
        return vBox;
    }

    private HBox makeThemeSelectHBox() {
        HBox hBox = new HBox(makeTextInfo(), makeThemeDropDownSelector());
        hBox.getStyleClass().add("config-hbox");
        return hBox;
    }

    private Node makeTextInfo() {
        Label label = new Label("Theme:");
        return label;
    }

    private Node makeThemeDropDownSelector() {
        MenuButton dropDown = new MenuButton();
        // StyleClass: menu-button
        dropDown.textProperty().bind(themeNameProperty);
        for (String theme : appSettings.getThemes()) {
            dropDown.getItems().add(makeThemeItem(theme));
        }

        return dropDown;
    }


    private MenuItem makeThemeItem(String theme) {
        MenuItem menuItem = new MenuItem(theme);
        menuItem.getStyleClass().add("dropdown-option");
        menuItem.setOnAction(event -> handleThemeChange(theme));
        return menuItem;
    }

    private void handleThemeChange(String theme) {
        if (theme.equals(appSettings.getThemeName())) // do nothing
            return;

        appSettings.setTheme(theme);
        themeNameProperty.set(theme);

    }


    @Override
    public Region build() {
        return rootPane;
    }
}
