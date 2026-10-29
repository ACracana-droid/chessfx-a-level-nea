package com.chessfx.engine.pieces;

import com.chessfx.engine.moves.MoveVector;
import com.chessfx.engine.board.Team;
import com.chessfx.log.Log;

import java.util.*;

public class ArbitraryPieceFactory {

    Map<String, PieceData> piecesMap;

    public ArbitraryPieceFactory(Map<String, PieceData> pieceMap) {
        this.piecesMap = pieceMap;
    }

    public Piece create(String name, Team team, int rank, int file, boolean hasMoved) {
        PieceData pieceData = getPieceEngineData(name);
        Set<MoveVector> moveSet = pieceData.getAllMoveVectors(team);

        Piece piece = makePiece(name, team, rank, file, hasMoved, pieceData, moveSet);

        team.getPieceList().add(piece);
        return piece;
    }

    private Piece makePiece(String name, Team team, int rank, int file,
                            boolean hasMoved, PieceData pieceData, Collection<MoveVector> moveSet) {
        if (name.equals("king")) {
            King king = new King(team, rank, file, pieceData, moveSet.toArray(new MoveVector[0]), hasMoved);
            team.setKing(king);
            return king;
        } else {
            return new Piece(name, pieceData.fenId, team, pieceData.value, rank, file, moveSet.toArray(new MoveVector[0]), hasMoved);
        }
    }

    public Piece copy(Piece piece, Team team) {
        Piece copy = makePiece(piece.name, team, piece.coOrds.y(), piece.coOrds.x(), piece.hasMoved, getPieceEngineData(piece.getName()), List.of(piece.moveSet));
        team.getPieceList().add(copy);
        return copy;
    }


    private PieceData getPieceEngineData(String identifier) {
        if (piecesMap.containsKey(identifier)) {
            return piecesMap.get(identifier);
        } else {
            Log.warn("ArbitraryPieceFactory", "There either is not a file containing information about this piece" +
                    "\nor it could not be found.");
            return piecesMap.get("placeholder");
        }

    }

}
