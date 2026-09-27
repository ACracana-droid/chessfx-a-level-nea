package com.chessfx.ui.menu;

import com.chessfx.chessconfig.AppSettings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.util.Builder;

public class ThemeDropDown implements Builder<MenuButton> {
    AppSettings appSettings;
    StringProperty themeNameProperty;

    public ThemeDropDown(AppSettings appSettings, StringProperty themeNameProperty) {
        this.appSettings = appSettings;
        this.themeNameProperty = themeNameProperty;
    }

    public ThemeDropDown(AppSettings appSettings) {
        this.appSettings = appSettings;
        this.themeNameProperty = new SimpleStringProperty(appSettings.getThemeName());
    }


    @Override
    public MenuButton build() {
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
}
