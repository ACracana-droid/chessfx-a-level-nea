package com.chess.chessfx.engine.pieces;

abstract public class Piece {

    Team team;

    public Piece(Team team) {
        this.team = team;
    }


    // This is called a factory method
    public static Piece makePieceFromAscii(char c) {
        Team team = Character.isUpperCase(c) ? Team.WHITE : Team.BLACK;
        return switch (Character.toLowerCase(c)) {
            case 'p' -> new Pawn(team);
            case 'r' -> new Rook(team);
            case 'n' -> new Knight(team);
            case 'b' -> new Bishop(team);
            case 'q' -> new Queen(team);
            case 'k' -> new King(team);
            default -> new Pawn(team); // could make error piece.
        };
    }
}
