package com.chessfx.ui.menu;

import com.chessfx.chessconfig.ChessConfigModel;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class GamePopup extends DraggableRegion {


    private final Runnable changeLayoutToGame;
    private final Runnable removePopup;
    private final ChessConfigModel chessConfig;

    public GamePopup(Runnable changeLayoutToGame, Runnable removePopup, ChessConfigModel chessConfig) {
        super();
        this.changeLayoutToGame = changeLayoutToGame;
        this.removePopup = removePopup;
        this.chessConfig = chessConfig;
        this.getStyleClass().add("game-popup");
        this.getChildren().add(makeDecisionVBox());
    }

    private Node makeDecisionVBox() {
        VBox vBox = new VBox(makeDescriptionLabel(),
                makeOptionsHBox());
        return vBox;
    }

    private Node makeOptionsHBox() {
        HBox hBox = new HBox(makeBotButton(),
                makeGamePlayersDecision("Vs Player", "pp"));
        return hBox;
    }

    private Node makeGamePlayersDecision(String buttonText, String gameType) {
        Button button = new Button(buttonText);
        button.setOnAction(event -> handlePopupButtonPressed(gameType));
        return button;
    }

    private Node makeBotButton() {
        Button button = new Button("Vs Bot");
        button.setOnAction(event -> handlePopupButtonPressed("pb"));
        return button;
    }

    private void handlePopupButtonPressed(String gameType) {
        chessConfig.setGameType(gameType);
        removePopup.run();
        changeLayoutToGame.run();
    }

    private Label makeDescriptionLabel() {
        Label label = new Label("Play against a player or bot?");
        return label;
    }

}
