package com.chessfx.engine.board;

import com.chessfx.engine.moves.Move;
import com.chessfx.engine.pieces.Piece;

public class PlyData {
    private final Move move;
    private final boolean hasPieceMovedBefore;
    private final Team team;

    public PlyData(Move move, boolean hasMoved, Team team) {
        this.move = move;
        this.hasPieceMovedBefore = hasMoved;
        this.team = team;

    }

    public Move getMove() {
        return move;
    }

    public Team getTeam() {
        return team;
    }

    public Piece getPieceWhichMoved() {
        return move.getPiece();
    }

    public boolean wasFirstMove() {
        return false;
    }

    public boolean hasPieceMovedBefore() {
        return hasPieceMovedBefore;
    }
}
