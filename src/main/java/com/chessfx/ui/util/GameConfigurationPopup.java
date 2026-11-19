package com.chessfx.ui.util;

import javafx.scene.layout.Region;

public class GameConfigurationPopup extends Region {

    private final boolean isVsBot;

    public GameConfigurationPopup(boolean isVsBot) {
        this.isVsBot = isVsBot;
    }

}
