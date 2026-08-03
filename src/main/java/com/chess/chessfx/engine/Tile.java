package com.chess.chessfx.engine;

import com.chess.chessfx.engine.pieces.Piece;

public class Tile extends TileParent {
    Piece piece;

    public Tile(Piece piece) {
        super();

    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
