package com.chessfx.ui.util;

import com.chessfx.chessconfig.AppSettings;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.girod.javafx.svgimage.SVGContent;
import org.girod.javafx.svgimage.SVGImage;
import org.girod.javafx.svgimage.SVGLoader;

import java.util.Objects;


abstract public class SettingsLayoutUtil {

    public static Button makeIconButton(AppSettings appSettings, PopupManager popupManager) {
        Button button = new Button();
        SVGImage image = SVGLoader.load(Objects.requireNonNull
                (popupManager.getClass().getResource("settings2.svg")));
        button.setGraphic(image);
        button.getStyleClass().add("settings-button");
        button.setOnAction(event -> {
            popupManager.makeSettingsPopup(appSettings);
        });
        return button;
    }


    public static Node makeSettingsVbox(AppSettings appSettings) {
        VBox vBox = new VBox(makeThemeSelectHBox(appSettings), hSeparator());
        vBox.getStyleClass().add("options-vbox");
        return vBox;
    }

    /**
     * Makes a horizontal separator node to help structure forms.
     *
     * @return horizontal separator node
     */
    private static Separator hSeparator() {
        // Credit https://jenkov.com/tutorials/javafx/separator.html
        Separator separator = new Separator(Orientation.HORIZONTAL);
        // has 'separator' style class.
        return separator;
    }

    private static HBox makeThemeSelectHBox(AppSettings appSettings) {
        HBox hBox = new HBox(makeTextInfo(), new ThemeDropDown(appSettings).build());
        hBox.getStyleClass().add("config-hbox");
        return hBox;
    }

    private static Node makeTextInfo() {
        Label label = new Label("Theme:");
        return label;
    }

}
