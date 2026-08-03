package com.chess.chessfx.engine;

import com.chess.chessfx.chessconfig.ChangedFenManager;
import com.chess.chessfx.chessconfig.ChessConfigModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

public class BoardRepresentation {

    IntegerProperty fullMoveCounter;
    IntegerProperty halfMoveCounter;

    ChessConfigModel chessConfig;

    final int BOARD_HEIGHT;
    final int BOARD_WIDTH;

    public BoardRepresentation(ChessConfigModel chessConfig) {
        this.chessConfig = chessConfig;

        BOARD_HEIGHT = chessConfig.getHeight();
        BOARD_WIDTH = chessConfig.getWidth();
        ChangedFenManager fen = chessConfig.getFen();
        fullMoveCounter = new SimpleIntegerProperty(fen.getFullMoveNumber());
        halfMoveCounter = new SimpleIntegerProperty(fen.getHalfMoveNumber());
    }

    public List<List<Tile>> getBoardData() {
        List<List<Tile>> boardTiles = new ArrayList<>();
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {

            }
        }
        return boardTiles;
    }

    public IntegerProperty getFullMoveCounter() {
        return fullMoveCounter;
    }
}
