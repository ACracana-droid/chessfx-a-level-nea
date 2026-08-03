package com.chess.chessfx.chessconfig;

import com.chess.chessfx.engine.Tile;

import static com.chess.chessfx.engine.pieces.Piece.makePieceFromAscii;

public class FenManager {

    char[] fen;
    String fenStyle;
    int fullmoveNumber;
    int halfmoveNumber;

    String piecePlacement;

    // FEN stands for Forsyth-Edwards notation.
    // There are several standard variants that support different games of chess
    // like 'X-Fen' or 'Shredder-FEN'.
    // I will try to use my own standard. I don't intend for this to be used outside my program.

    public FenManager(String fenStyle, String fenString) {
        this.fen = fenString.toCharArray();
        this.fenStyle = fenStyle;

    }

    public int getFullmoveNumber() {
        return fullmoveNumber;
    }

    public int getHalfmoveClock() {
        return halfmoveNumber;
    }

    public Tile[][] getGameBoard(int height, int width) {
        if (fenStyle.equals("case")) {
            return getBoardFromCasedTeamsFen(height, width);
        } else {
            return getBoardFromComplexFen(height, width);
        }

    }

    // not backwards compatible with fen.
    // uses new scheme, defining pieces and tiles with characters
    private Tile[][] getBoardFromComplexFen(int height, int width) {
        //TODO: Make new FEN-inspired notation supporting different tile types.
        return null;
    }

    private Tile[][] getBoardFromCasedTeamsFen(int height, int width) {
        Tile[][] board = new Tile[height][width];
        int rank = 0;
        int file = 0;
        // 0 indexed internally. (rank == row and file == column)
        char cur;
        for (int i = 0; (cur = fen[i]) != ' '; i++) {
            if (Character.isAlphabetic(cur)) {
                board[rank][file] = new Tile(makePieceFromAscii(cur));
                file++;
            } else if (Character.isDigit(cur)) {
                for (int j = 0; j < Character.getNumericValue(cur); j++) {
                    board[rank][file] = new Tile(makePieceFromAscii(cur));
                }
            } else {
                // for '/'
                rank++;
                file = 0;
            }
        }
        return board;
    }


}
