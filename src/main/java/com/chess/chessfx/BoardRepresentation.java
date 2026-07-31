package com.chess.chessfx;

import com.chess.chessfx.chessconfig.ChessConfigModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

public class BoardRepresentation {

    IntegerProperty turnCountProperty;
    ChessConfigModel chessConfig;

    public BoardRepresentation(ChessConfigModel chessConfig) {
        this.chessConfig = chessConfig;
        turnCountProperty = new SimpleIntegerProperty(1);
    }

    public List<List<Tile>> getBoardData() {
        List<List<Tile>> boardTiles = new ArrayList<>();
        for (int i = 0; i < chessConfig.getHeight(); i++) {
            for (int j = 0; j <chessConfig.getWidth(); j++) {

            }
        }
        return boardTiles;
    }

    public IntegerProperty getTurnCountProperty() {
        return turnCountProperty;
    }
}
