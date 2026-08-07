package com.chess.chessfx.menu;

import com.chess.chessfx.chessconfig.ChessConfigModel;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.util.Builder;

public class MenuLayoutBuilder implements Builder<Region> {

    Runnable changeLayoutToGame;
    Pane rootPane;

    ChessConfigModel chessConfig;

    public MenuLayoutBuilder(Runnable changeLayoutToGame, ChessConfigModel chessConfig) {
        this.chessConfig = chessConfig;
        this.changeLayoutToGame = changeLayoutToGame;
        rootPane = new StackPane(createOptionsHBox());

    }

    private Node createOptionsHBox() {
        VBox vBox = new VBox(makeQuickPlayVsBotButton(),
                makeQuickPlayerVsPlayerButton(),
                makeWildCardButton(),
                makeMoreOptionsButton()
                );
        vBox.getStyleClass().add("options-vbox");
        return vBox;
    }

    private Node makeMoreOptionsButton() {
        return new Button("See more (doesn't work yet)");
    }

    private Node makeWildCardButton() {
        PlayButton button = new PlayButton(changeLayoutToGame,
                () -> chessConfig.setChessConfigFromPropertiesResource("reallyBigBasicChess.properties"));
        button.setText("Wildcard 2 player");
        return button;
    }

    private Node makeQuickPlayerVsPlayerButton() {
        PlayButton button = new PlayButton(changeLayoutToGame,
                () -> chessConfig.setChessConfigFromPropertiesResource("defaultPlayerVsPlayer.properties"));
        button.setText("Quick Player Vs Player");
        return button;
    }

    private Node makeQuickPlayVsBotButton() {
        PlayButton button = new PlayButton(changeLayoutToGame,
                () -> chessConfig.setChessConfigFromPropertiesResource("defaultPlayerVsBot.properties"));
        button.setText("Quick Player Vs Bot");
        return button;
    }

    @Override
    public Region build() {
        return rootPane;
    }
}
