package com.chess.chessfx.engine.pieces;

public class ArbitraryPiece extends Piece {
    String identifier;

    public ArbitraryPiece(String identifier, Team team, int rank, int file) {
        super(team, rank, file);
        this.identifier = identifier;
    }



}
