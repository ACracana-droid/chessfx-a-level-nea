package com.chess.chessfx.engine;

import com.chess.chessfx.chessconfig.ChessConfigModel;
import com.chess.chessfx.engine.tiles.TileParent;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class BoardRepresentation {

    IntegerProperty fullmoveCounter;
    IntegerProperty halfmoveClock; // used for 50 move draw rule (since last capture or pawn advance)
    int halfmoveClockRuleNumber; // used for changing '50' halfmove draw rule

    private final ChessConfigModel chessConfig;


    TileParent[][] board;

    public BoardRepresentation(ChessConfigModel chessConfig) {
        this.chessConfig = chessConfig;

        board = chessConfig.getGameBoard();

        fullmoveCounter = new SimpleIntegerProperty(chessConfig.getFullmoveNumber());
        halfmoveClock = new SimpleIntegerProperty(chessConfig.getHalfmoveClock());
        halfmoveClockRuleNumber = chessConfig.getHalfmoveClockRuleNumber();
    }

    public IntegerProperty fullmoveCounterProperty() {
        return fullmoveCounter;
    }

    public TileParent[][] getBoardAsTileArray2D() {
        if (board == null) {
            System.out.println("Board was null. bad");
        }
        return board;
    }

    public ChessConfigModel getChessConfig() {
        return chessConfig;
    }

}
