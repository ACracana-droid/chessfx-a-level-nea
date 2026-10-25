package com.chessfx.engine.board;

import com.chessfx.engine.moves.Move;
import com.chessfx.engine.pieces.Piece;

public record PlyData(Move move, boolean hasPieceMovedBefore, Piece directCapture, Team team
) {

}
