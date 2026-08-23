package com.chessfx.ui.menu;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ReturnButton extends Button {

    public ReturnButton(Runnable returnToMainScreen, String text) {
        super(text);
        this.getStyleClass().add("return-button");
        this.setOnAction(event -> handleButtonAction(returnToMainScreen));
    }

    private void handleButtonAction(Runnable returnToMainScreen) {
        returnToMainScreen.run();
    }

    public VBox getAsVBox() {
        VBox vBox = new VBox(this);
        vBox.getStyleClass().add("return-vbox");
        return vBox;
    }
}

