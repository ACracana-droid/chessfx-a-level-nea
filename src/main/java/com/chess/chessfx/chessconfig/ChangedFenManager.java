package com.chess.chessfx.chessconfig;

public class ChangedFenManager {

    String fen;
    int fullMoveNumber;
    int halfMoveNumber;

    // FEN stands for Forsyth-Edwards notation.
    // There are several standard variants that support different games of chess
    // like 'X-Fen' or 'Shredder-FEN'.
    // I will try to use my own standard. I don't intend for this to be used outside my program.

    /// I have realised that because of how FEN works, having metadata
    /// like height and width in my .properties files is unecessary.
    /// I can infer these from the fen.

    public ChangedFenManager(String fen) {
        this.fen = fen;
    }

    public void decodeFen(String fen) {
        while (true) {

        }
    }


    public int getFullMoveNumber() {
        return fullMoveNumber;
    }

    public int getHalfMoveNumber() {
        return halfMoveNumber;
    }
}
