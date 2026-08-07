package com.chess.chessfx.engine.pieces;

import java.util.ArrayList;
import java.util.List;

public enum Team {
    WHITE(),
    BLACK(),
    RED(),
    BLUE(),
    YELLOW();

    final private List<Piece> pieceList;

    Team() {
        pieceList = new ArrayList<>();
    }

    public List<Piece> getPieceList() {
        return pieceList;
    }
}
