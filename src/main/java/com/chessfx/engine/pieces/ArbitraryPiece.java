package com.chessfx.engine.pieces;

import com.chessfx.engine.MoveVector;

public class ArbitraryPiece extends Piece {

    public ArbitraryPiece(String name, Team team, int rank, int file, PieceData pieceData, MoveVector[] moveSet, boolean hasMoved) {
        super(name, pieceData.fenId, team, pieceData.value, rank, file, moveSet, hasMoved);
    }

}
