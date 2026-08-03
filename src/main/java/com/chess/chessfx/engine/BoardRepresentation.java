package com.chess.chessfx.engine;

import com.chess.chessfx.chessconfig.ChessConfigModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class BoardRepresentation {

    IntegerProperty fullmoveCounter;
    IntegerProperty halfmoveClock; // used for 50 move draw rule (since last capture or pawn advance)
    int halfmoveClockRuleNumber; // used for changing '50' halfmove draw rule

    private final ChessConfigModel chessConfig;

    final int BOARD_HEIGHT;
    final int BOARD_WIDTH;

    Tile[][] board;

    public BoardRepresentation(ChessConfigModel chessConfig) {
        this.chessConfig = chessConfig;

        BOARD_HEIGHT = chessConfig.getHeight();
        BOARD_WIDTH = chessConfig.getWidth();
        board = chessConfig.getGameBoard();
        fullmoveCounter = new SimpleIntegerProperty(chessConfig.getFullmoveNumber());
        halfmoveClock = new SimpleIntegerProperty(chessConfig.getHalfmoveClock());
        halfmoveClockRuleNumber = chessConfig.getHalfmoveClockRuleNumber();
    }

    public IntegerProperty fullmoveCounterProperty() {
        return fullmoveCounter;
    }

    public Tile[][] getBoardAsTileArray2D() {
        return board;
    }
    public ChessConfigModel getChessConfig() {
        return chessConfig;
    }
}
