package com.chessfx.engine.board;

import com.chessfx.engine.moves.Move;
import com.chessfx.engine.pieces.Piece;

public record PlyData(Move move, Piece origPiece,
                      boolean hasPieceMovedBefore, Piece directCapture,
                      java.util.List<Piece> indirectCaptures, Team team,
                      com.chessfx.engine.moves.Coordinate enPassantTargetSquare) {

}
