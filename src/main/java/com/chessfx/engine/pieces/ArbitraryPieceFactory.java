package com.chessfx.engine.pieces;

import com.chessfx.engine.moves.MoveVector;
import com.chessfx.engine.board.Team;

import java.util.*;

public class ArbitraryPieceFactory {

    Map<String, PieceData> piecesMap;

    public ArbitraryPieceFactory(Map<String, PieceData> pieceMap) {
        this.piecesMap = pieceMap;
    }

    public Piece create(String name, Team team, int rank, int file, boolean hasMoved) {

        PieceData pieceData = getPieceEngineData(name);
        Set<MoveVector> moveSet = pieceData.getAllMoveVectors(team);

        if (name.equals("king"))
            return new King(team, rank, file, pieceData, moveSet.toArray(new MoveVector[0]), hasMoved);

        return new ArbitraryPiece(name, team, rank, file, pieceData, moveSet.toArray(new MoveVector[0]), hasMoved);
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
