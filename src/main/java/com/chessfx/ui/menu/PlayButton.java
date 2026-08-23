package com.chessfx.ui.menu;

import com.chessfx.chessconfig.ChessConfigModel;

import javafx.concurrent.Task;
import javafx.scene.control.Button;

public class PlayButton extends Button {

    public PlayButton(Runnable changeLayoutToGame, ChessConfigModel chessConfig, Runnable setChessConfig) {
        super();
        this.setOnAction(event -> changeLayoutToGame(changeLayoutToGame, chessConfig, setChessConfig));
        this.getStyleClass().add("menu-play-button");
    }

    private void changeLayoutToGame(Runnable changeLayoutToGame, ChessConfigModel chessConfig, Runnable setChessConfig) {

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                setChessConfig.run();
                return null;
            }
        };
        // on fxat
        task.setOnSucceeded(event -> changeLayoutToGame.run());

        Thread thread = new Thread(task);
        thread.start();
    }
}
