package com.chess.chessfx.gameui;

import com.chess.chessfx.chessconfig.ChessConfigModel;
import com.chess.chessfx.engine.BoardRepresentation;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.Builder;

public class GameLayoutBuilder implements Builder<Region> {

    Runnable changeLayoutToMenu;
    BorderPane rootPane;
    BoardRepresentation boardRepresentation;
    ChessConfigModel chessConfig;

    public GameLayoutBuilder(Runnable changeLayoutToMenu, ChessConfigModel chessConfig) {
        this.changeLayoutToMenu = changeLayoutToMenu;
        this.chessConfig = chessConfig;
    }

    private void makeChessboardGUI(BoardRepresentation representation) {
        rootPane.setCenter(null);
        rootPane.setTop(turnCounterHBox(representation.getFullMoveCounter()));
    }

    private HBox turnCounterHBox(IntegerProperty turnCountProperty) {
        HBox hBox = new HBox(makeTimer(), makeTurnCounter(turnCountProperty));
        hBox.getStyleClass().add("game-top-hbox");
        return hBox;
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
        return rootPane;
    }

    // TIP: here is the entry point for the game.
    // This is where a user is directed after pressing play.
    // Due to threading, this likely happens during the `manageLayoutAnimation()`
    // in RootWrapperBuilder.

    public void startup() {
        // chess config is updated as soon as a play button is pressed
        // (which is also when this method occurs).
        boardRepresentation = new BoardRepresentation(chessConfig);
        makeChessboardGUI(boardRepresentation);
    }
}
