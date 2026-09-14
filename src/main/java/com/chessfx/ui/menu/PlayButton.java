package com.chessfx.ui.menu;

import javafx.concurrent.Task;
import javafx.scene.control.Button;

public class PlayButton extends Button {

    public PlayButton(Runnable changeLayoutToGame, Runnable setChessConfig) {
        super();
        this.setOnAction(event -> changeLayoutToGame(changeLayoutToGame, setChessConfig));
        this.getStyleClass().add("menu-play-button");
    }

    public PlayButton() {
        super();
    }

    private void changeLayoutToGame(Runnable changeLayoutToGame, Runnable setChessConfig) {

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
