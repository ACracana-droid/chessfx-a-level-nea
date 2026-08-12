package com.chess.chessfx.ui.game;

import com.chess.chessfx.chessconfig.ChessConfigModel;
import com.chess.chessfx.engine.ChessGameModel;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.Builder;


public class GameLayoutBuilder implements Builder<Region> {

    Runnable changeLayoutToMenu;
    BorderPane rootPane;
    ChessGameModel boardRepresentation;
    ChessConfigModel chessConfig;

    public GameLayoutBuilder(Runnable changeLayoutToMenu, ChessConfigModel chessConfig) {
        this.changeLayoutToMenu = changeLayoutToMenu;
        this.chessConfig = chessConfig;
    }

    private HBox turnCounterHBox(IntegerProperty turnCountProperty) {
        HBox hBox = new HBox(makeTimer(), makeTurnCounter(turnCountProperty), makeReturnToMenuButton());
        hBox.getStyleClass().add("game-top-hbox");
        return hBox;
    }

    private Node makeReturnToMenuButton() {
        Button button = new Button("Exit");
        button.setOnAction(event -> changeLayoutToMenu.run());
        return button;
    }

    private Node makeTurnCounter(IntegerProperty integerProperty) {
        Label label = new Label();
        label.textProperty().bind(integerProperty.asString());
        label.getStyleClass().add("turn-counter");
        return label;
    }

    private Node makeTimer() {
        Label label = new Label("00:00");
        // Placeholder for now
        return label;
    }

    @Override
    public Region build() {
        rootPane = new BorderPane();
//        rootPane.setOnMouseClicked(event -> handleGlobalInGameMouseClick());
        return rootPane;
    }



    /**
     * <b>The entry point for the chess game.</b>
     * This is where a user is directed after pressing a play button.
     * <p>
     * Due to threading, this likely happens during the `manageLayoutAnimation()`
     * in RootWrapperBuilder.
     */
    public void startup() {
        // chess config is updated as soon as a play button is pressed
        // (which is also when this method occurs).
        boardRepresentation = new ChessGameModel(chessConfig);
        makeChessboardGUI(boardRepresentation);
    }

    private void makeChessboardGUI(ChessGameModel boardModel) {
        rootPane.setCenter(new ChessBoardBuilder(boardModel).build());
        rootPane.setTop(turnCounterHBox(boardModel.fullmoveCounterProperty()));
    }

}
