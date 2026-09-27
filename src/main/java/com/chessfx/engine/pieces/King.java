package com.chessfx.engine.pieces;

import com.chessfx.engine.moves.MoveVector;
import com.chessfx.engine.board.Team;

public class King extends Piece {
    public King(Team team, int rank, int file, PieceData pieceData, MoveVector[] moveSet, boolean hasMoved) {
        super("king", pieceData.fenId, team, pieceData.value, rank, file, moveSet, hasMoved);
        team.setKing(this);
    }

}
