package com.chessfx.chessconfig;

import com.chessfx.engine.pieces.ArbitraryPieceFactory;
import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.pieces.Team;
import com.chessfx.engine.tiles.Tile;
import com.chessfx.engine.tiles.TileParent;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FenManager {

    char[] fen;
    String fenStyle;
    private final TeamConf teamConf;
    //    int fullmoveNumber;
//    int halfmoveClock;
    int fullmoveNumber = 1;
    int halfmoveClock = 0;
    int startingTeamIdx;
    int height, width; // simple utility

    ArbitraryPieceFactory pieceFactory;
    Set<String> pieceNameSet;

    Map<String, String> fenIdToNameMap;

    TileParent[][] board;


    // FEN stands for Forsyth-Edwards notation.
    // There are several standard variants that support different games of chess
    // like 'X-Fen' or 'Shredder-FEN'.
    // I will try to use my own standard. I don't intend for this to be used outside my program.

    public FenManager(String fenStyle, String fenString, Map<String, String> fenIdToNameMap,
                      ArbitraryPieceFactory pieceFactory, int height, int width, TeamConf teamConf) {

        this.fen = fenString.trim().toCharArray();
        this.fenStyle = fenStyle;
        this.teamConf = teamConf;
        this.pieceNameSet = new HashSet<>();
        this.fenIdToNameMap = fenIdToNameMap;
        this.pieceFactory = pieceFactory;
        this.height = height;
        this.width = width;
        makeBoard();
    }

    private void makeBoard() {
        if (fenStyle.equals("case")) {
            System.out.println("Fen Manager: Using cased fen.");
            makeBoardFromCasedTeamsFen();
        } else {
            System.out.println("Fen Manager: Using complex FEN: UNFINISHED!!!!");
            makeBoardFromComplexFen();
        }
    }

    public int fullmoveNumberProperty() {
        return fullmoveNumber;
    }

    public int halfmoveClockProperty() {
        return halfmoveClock;
    }

    public TileParent[][] getGameBoard() {
        if (board == null)
            System.out.println("Fen Manager: Board is null!");
        return board;
    }


    /**
     * Parses the board state from this program's Forsyth Edwards inspired notation and converts
     * this into a 2D array. Supports variable board size, more players and special tiles,
     * but is not compatible with real world FEN.
     *
     * @return 2D array which represents a full board state.
     */
    private void makeBoardFromComplexFen() {
        //TODO: Make new FEN-inspired notation supporting different tile types.
    }


    int i = 0;

    /**
     * Parses the board state from FEN and converts this into a 2D array.
     * Supports 2 player matches with variable board size; backwards compatible with real world FEN.
     *
     */
    private void makeBoardFromCasedTeamsFen() {
        board = new TileParent[height][width];

        // 0 indexed internally. (rank == row and file == column)
        i = 0;

        parseBoard();

        temporaryBoardChecks();
        i++;
        parseStartingTeam();
        // castle rules:
        while (fen[i] != ' ') {
            // parse castling rules.
            i++;
        }
        i++;

        while (fen[i] != ' ') {
            // parse enPassant square
            i++;
        }
        i++;
        halfmoveClock = getHalfmoveNumber();
        i++;
        fullmoveNumber = getFullmoveNumber();
        System.out.println("Fen Manager: Parsed whole FEN String.");
    }

    private void parseStartingTeam() {
        startingTeamIdx = (fen[i] == 'w') ? teamConf.whiteIdx : teamConf.blackIdx;
        System.out.println("Team: " + startingTeamIdx);
        i += 2;
    }

    private void parseBoard() {
        int rank = 0;
        int file = 0;
        char cur;
        while ((cur = fen[i]) != ' ') {
            if (Character.isAlphabetic(cur)) {

                board[rank][file] = new Tile(makePieceFromAscii(cur, rank, file));
                file++;
            } else if (Character.isDigit(cur)) {
                // must look ahead for 2-digit numbers
                int num = Character.getNumericValue(cur);
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
            i++;
        }
    }

    private int getHalfmoveNumber() {
        int halfmove = 0;
        while (fen[i] != ' ') {
            // parse halfmove number (increments every time a piece is moved)
            halfmove = halfmove * 10 + Character.getNumericValue(fen[i]);
            i++;
        }
        System.out.println("halfmove clock: " + halfmove);
        return halfmove;
    }

    private int getFullmoveNumber() {
        int fullmove = 0;
        while (i < fen.length) {
            // parse fullmove number (increments after black moves)
            fullmove = fullmove * 10 + Character.getNumericValue(fen[i]);
            i++;
        }
        System.out.println("fullmove number: " + fullmoveNumber);
        return fullmove;
    }

    private void temporaryBoardChecks() {
        System.out.println("Fen Manager: doing temporary board checks ");
        int count = 0;
        for (int idx = 0; idx < height; idx++) {
            for (int j = 0; j < width; j++) {
                if (board[idx][j] == null) {
                    count++;
                    System.out.println("" + idx + " " + j);
                }
            }
        }
        System.out.println("Fen Manager: Null count: " + count);
    }

    public Piece makePieceFromAscii(char c, int rank, int file) {
        Team team = Character.isUpperCase(c) ? teamConf.white : teamConf.black;
        String name = fenIdToNameMap.get(String.valueOf(Character.toLowerCase(c)));
        pieceNameSet.add(name);
        return pieceFactory.create(name, team, rank, file, false);
    }

    public Set<String> getInstantiatedPieceNameSet() {
        return pieceNameSet;
    }

    public int getStartingTeamIdx() {
        return startingTeamIdx;
    }
}
