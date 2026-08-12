package com.chess.chessfx.engine.pieces;

public class King extends Piece {


    public King(Team team, int rank, int file, PieceData pieceData) {
        super("king", pieceData.fenId, team, pieceData.value, rank, file, pieceData.getAllMoveVectors(), false);
    }
}
