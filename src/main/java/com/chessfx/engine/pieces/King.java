package com.chessfx.engine.pieces;

import com.chessfx.engine.board.Team;

public class King extends Piece {


    public King(Team team, int rank, int file, boolean hasMoved, PieceData pieceData) {
        super("king", team, rank, file, hasMoved, pieceData);
    }
}
