package com.chess.chessfx.engine.pieces;

import com.chess.chessfx.engine.Move;
import com.chess.chessfx.engine.MoveVector;

public class ArbitraryPiece extends Piece {
    MoveVector[] moveVectors;

    public ArbitraryPiece(String identifier, Team team, int rank, int file, MoveVector[] moveVectors) {
        super(identifier, team, rank, file);
        this.moveVectors = moveVectors;
    }


}
