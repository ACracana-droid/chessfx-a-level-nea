package com.chessfx.ui.menu;

import com.chessfx.chessconfig.ChessConfigModel;
import javafx.concurrent.Task;
import javafx.scene.control.Button;


public class PlayButtonWithPlayerTypeSelector extends Button {

    public PlayButtonWithPlayerTypeSelector(Runnable changeLayoutToGame, ChessConfigModel chessConfig,
                                            Runnable setChessConfig, PopupManager popupManager) {
        super();
        this.getStyleClass().add("menu-play-button");
        this.setOnAction(event -> changeLayoutToGame(changeLayoutToGame, chessConfig, setChessConfig, popupManager));
    }

    private void changeLayoutToGame(Runnable changeLayoutToGame, ChessConfigModel chessConfig, Runnable setChessConfig, PopupManager popupManager) {

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                setChessConfig.run();
                return null;
            }
        };
        // on fxat
        task.setOnSucceeded(event -> handleSuccessfulConfigLoad(changeLayoutToGame, chessConfig, popupManager));

        Thread thread = new Thread(task);
        thread.start();
    }

    private void handleSuccessfulConfigLoad(Runnable changeLayoutToGame, ChessConfigModel chessConfig, PopupManager popupManager) {
        popupManager.askGameTypeAndWait(changeLayoutToGame, chessConfig);
    }
}
