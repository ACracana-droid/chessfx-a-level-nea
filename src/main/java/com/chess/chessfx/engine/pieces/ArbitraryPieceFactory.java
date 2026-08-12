package com.chess.chessfx.engine.pieces;

import com.chess.chessfx.engine.MoveVector;

import java.util.*;

public class ArbitraryPieceFactory {

    Map<String, PieceData> piecesMap;
    Set<String> adjustedPieces;
    final static int INF = 0xfffffff; // functionally infinite.

    public ArbitraryPieceFactory(Map<String, PieceData> pieceMap) {
        this.piecesMap = pieceMap;
        adjustedPieces = new HashSet<>();
    }


    public Piece create(String name, Team team, int rank, int file, boolean hasMoved) {

        PieceData pieceData = getPieceEngineData(name);
        MoveVector[] moveSet = pieceData.getAllMoveVectors();
        if (team == Team.WHITE) adjustMoveSet(name, moveSet);

        return new ArbitraryPiece(name, team, rank, file, pieceData, moveSet, hasMoved);
    }

    // TODO: MAKE MORE ROBUST
    private void adjustMoveSet(String name, MoveVector[] moveSet) {
        if (adjustedPieces.add(name)) {
            for (MoveVector moveVector : moveSet) {
                moveVector.flipYValues();
            }
        }

    }

    private PieceData getPieceEngineData(String identifier) {
        if (piecesMap.containsKey(identifier)) {
            return piecesMap.get(identifier);
        } else {
            System.out.println("ArbitraryPiece Factory: There either is not a file containing information about this piece" +
                    "\nor it could not be found.");
            return piecesMap.get("placeholder");
        }

    }

}
