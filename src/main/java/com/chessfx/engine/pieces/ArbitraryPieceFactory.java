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

        Piece piece = makePiece(name, team, rank, file, hasMoved, pieceData);

        team.getPieceList().add(piece);
        return piece;
    }

    private Piece makePiece(String name, Team team, int rank, int file,
                            boolean hasMoved, PieceData pieceData) {
        if (name.equals("king")) {
            King king = new King(team, rank, file, hasMoved, pieceData);
            team.setKing(king);
            return king;
        }
        if (name.equals("pawn")) {
            return new Pawn(team, rank, file, hasMoved, pieceData);
        } else {
            return new Piece(name, team, rank, file, hasMoved, pieceData);
        }
    }

    public Piece copy(Piece piece, Team team) {
        PieceData pieceData = getPieceEngineData(piece.getName());
        Piece copy = makePiece(piece.name, team, piece.pos.y(), piece.pos.x(), piece.hasMoved, pieceData);
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

    public Piece mutate(Piece piece, String newName) {
        Piece copy = makePiece(newName, piece.getTeam(), piece.pos.y(), piece.pos.x(), piece.hasMoved, getPieceEngineData(newName));
        piece.getTeam().getPieceList().add(copy);
        return copy;
    }
}
