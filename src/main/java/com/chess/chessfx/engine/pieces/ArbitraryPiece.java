package com.chess.chessfx.engine.pieces;

import com.chess.chessfx.engine.ChessGameModel;
import com.chess.chessfx.engine.Move;
import com.chess.chessfx.engine.MoveVector;

import java.util.ArrayList;
import java.util.List;

public class ArbitraryPiece extends Piece {

    public ArbitraryPiece(String name, Team team, int rank, int file, PieceData pieceData, MoveVector[] moveSet, boolean hasMoved) {
        super(name, pieceData.fenId, team, pieceData.value, rank, file, moveSet, hasMoved);
    }

}
