package com.chess.chessfx.chessconfig;

import com.chess.chessfx.engine.pieces.ArbitraryPieceFactory;
import com.chess.chessfx.engine.pieces.Piece;
import com.chess.chessfx.engine.pieces.Team;
import com.chess.chessfx.engine.tiles.Tile;
import com.chess.chessfx.engine.tiles.TileParent;

import java.util.HashSet;
import java.util.Set;

public class FenManager {

    char[] fen;
    String fenStyle;
    int fullmoveNumber;
    int halfmoveNumber;

    ArbitraryPieceFactory pieceFactory;
    Set<String> pieceIdSet;
    Tile[][] board;

    String piecePlacement;

    // FEN stands for Forsyth-Edwards notation.
    // There are several standard variants that support different games of chess
    // like 'X-Fen' or 'Shredder-FEN'.
    // I will try to use my own standard. I don't intend for this to be used outside my program.

    public FenManager(String fenStyle, String fenString) {
        this.fen = fenString.toCharArray();
        this.fenStyle = fenStyle;
        this.pieceIdSet = new HashSet<>();
        pieceFactory = new ArbitraryPieceFactory();
    }

    public int getFullmoveNumber() {
        return fullmoveNumber;
    }

    public int getHalfmoveClock() {
        return halfmoveNumber;
    }

    public TileParent[][] getGameBoard(int height, int width) {
        if (fenStyle.equals("case")) {
            System.out.println("Fen Manager: Using cased fen.");
            return getBoardFromCasedTeamsFen(height, width);
        } else {
            System.out.println("Fen Manager: Using complex FEN: UNFINISHED!!!!");
            return getBoardFromComplexFen(height, width);
        }

    }


    /**
     * Parses the board state from this program's Forsyth Edwards inspired notation and converts
     * this into a 2D array. Supports variable board size, more players and special tiles,
     * but is not compatible with real world FEN.
     *
     * @param height chess board height
     * @param width  chess board width
     * @return 2D array which represents a full board state.
     */
    private TileParent[][] getBoardFromComplexFen(int height, int width) {
        //TODO: Make new FEN-inspired notation supporting different tile types.
        return null;
    }

    /**
     * Parses the board state from FEN and converts this into a 2D array.
     * Supports 2 player matches with variable board size; backwards compatible with real world FEN.
     *
     * @param height chess board height
     * @param width  chess board width
     * @return 2D array which represents a full board state.
     */
    private TileParent[][] getBoardFromCasedTeamsFen(int height, int width) {
        TileParent[][] board = new TileParent[height][width];
        int rank = 0;
        int file = 0;
        // 0 indexed internally. (rank == row and file == column)
        char cur;
        for (int i = 0; (cur = fen[i]) != ' '; i++) {
            if (Character.isAlphabetic(cur)) {

                board[rank][file] = new Tile(makePieceFromAscii(cur, rank, file));

                file++;
            } else if (Character.isDigit(cur)) {
                // must look ahead for 2-digit numbers
                int num = Character.getNumericValue(cur);
                // To reproduce error, comment out this if-statement
                if (Character.isDigit(fen[i + 1])) {
                    i++;
                    num = (num * 10) + Character.getNumericValue(fen[i]);
                }
                for (int j = 0; j < num; j++, file++) {
                    board[rank][file] = new Tile(null);
                }
            } else {
                // for '/'
                rank++;
                file = 0;
            }
        }

        System.out.println("Fen Manager: doing temporary board checks ");
        int count = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (board[i][j] == null) {
                    count++;
                    System.out.println("" + i + " " + j);
                }
            }
        }
        System.out.println("Fen Manager: Null count: " + count);
        return board;
    }

    public Piece makePieceFromAscii(char c, int rank, int file) {
        Team team = Character.isUpperCase(c) ? Team.WHITE : Team.BLACK;
        String id = null;
        switch (Character.toLowerCase(c)) {
            case 'p' -> id = "pawn";
            case 'r' -> id = "rook";
            case 'n' -> id = "knight";
            case 'b' -> id = "bishop";
            case 'q' -> id = "queen";
            case 'k' -> id = "king";
        }
        pieceIdSet.add(id);
        return pieceFactory.create(id, team, rank, file);
    }

    public Set<String> getInstantiatedPieceIdSet() {
        return pieceIdSet;
    }
}
