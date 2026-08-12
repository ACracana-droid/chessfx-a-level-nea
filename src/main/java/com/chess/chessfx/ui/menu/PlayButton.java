package com.chess.chessfx.ui.menu;

import javafx.scene.control.Button;

public class PlayButton extends Button {
    // should be a method at this point!
    public PlayButton(Runnable changeLayoutToGame, Runnable setChessConfig) {
        super();
        this.setOnAction(event -> changeLayoutToGame(changeLayoutToGame, setChessConfig));
        this.getStyleClass().add("menu-play-button");
    }

    private void changeLayoutToGame(Runnable changeLayoutToGame, Runnable setChessConfig) {
        setChessConfig.run();
        changeLayoutToGame.run();
    }
}
