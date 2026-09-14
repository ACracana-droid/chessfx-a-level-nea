package com.chessfx.ui.menu;

import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.ui.game.ChessBoardOrnamentBuilder;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.util.Builder;

public class MenuLayoutBuilder implements Builder<Region> {


    private Runnable changeLayoutToGame;
    private final Runnable changeLayoutToSettings;
    private final PopupManager popupManager;
    private final Runnable changeLayoutToMoreOptions;

    BorderPane rootPane;

    ChessConfigModel chessConfig;

    public MenuLayoutBuilder(Runnable changeLayoutToGame, Runnable changeLayoutToSettings,
                             Runnable changeLayoutToMoreOptions, ChessConfigModel chessConfig, PopupManager popupManager) {
        this.changeLayoutToMoreOptions = changeLayoutToMoreOptions;
        this.chessConfig = chessConfig;
        this.changeLayoutToGame = changeLayoutToGame;
        this.changeLayoutToSettings = changeLayoutToSettings;
        this.popupManager = popupManager;
        rootPane = new BorderPane(createOptionsVBox());
        rootPane.setTop(makeBoardOrnament());
    }

    private Node createOptionsVBox() {
        VBox vBox = new VBox(
                makeQuickGameButton(),
                makeWildCardButton(),
                makeSettingsButton(),
                makeMoreOptionsButton());
        vBox.getStyleClass().add("options-vbox");
        return vBox;
    }

    private Node makeBoardOrnament() {
        Pane chessBoard = new ChessBoardOrnamentBuilder().build();
        return chessBoard;
    }

    private Node makeSettingsButton() {
        Button button = new Button("Settings");
        button.setOnAction(event -> {
            changeLayoutToSettings.run();
        });
        return button;
    }

    private Node makeMoreOptionsButton() {
        Button button = new Button("See more (doesn't work yet)");
        button.setOnAction(event -> changeLayoutToMoreOptions.run());
        return button;
    }

    private Node makeWildCardButton() {
        PlayButton button = new PlayButton(changeLayoutToGame,
                () -> chessConfig.setChessConfigFromPropertiesResource("reallyBigBasicChess.properties"));
        button.setText("Wildcard 2 player");
        return button;
    }

    private Node makeQuickGameButton() {
        Button button = new PlayButtonWithPlayerTypeSelector(changeLayoutToGame,
                chessConfig, () -> {
            chessConfig.setChessConfigFromPropertiesResource("defaultPvB.properties");
        }, popupManager);

        button.setText("Quick Game");
        return button;
    }

    @Override
    public Region build() {
        return rootPane;
    }
}
