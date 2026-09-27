package com.chessfx.engine.moves;

import com.chessfx.engine.pieces.Piece;
import com.chessfx.log.Log;

import java.util.List;

public class Move extends MoveBase {

    /*
    captureList:
    The plan is to make a capture list that I can use to revive pieces at will.
     */
    private final List<Piece> captures; // could be null
    private final Piece piece;

//    private final boolean capturesKing;

    public Move(Piece piece, int y, int x, List<Piece> captures) {
        super(piece.getCoOrds()[0], piece.getCoOrds()[1], y, x);
        this.piece = piece;
        this.captures = captures;
//        capturesKing = calculateIsKingCapture();
//        Log.warn("Calculated isCheck. Value: " + capturesKing);
    }

    public Move(Piece piece, int y, int x) {
        super(piece.getCoOrds()[0], piece.getCoOrds()[1], y, x);
        this.piece = piece;
        captures = null;
//        capturesKing = false;
    }

    public boolean isCapture() {
        return captures != null && !captures.isEmpty();
    }

    public int getEndY() {
        return y2;
    }

    public int getEndX() {
        return x2;
    }


    public String internalNotation() {
        StringBuffer sb = new StringBuffer();
        if (isCapture())
            sb.append("x");
        sb.append("(").append(y1).append(", ").append(x1).append(")");
        sb.append(" to ");
        sb.append("(").append(y2).append(", ").append(x2).append(")");

        return sb.toString();
    }

    @Override
    public String toString() {
        return internalNotation();
    }

    public int getStartX() {
        return x1;
    }

    public int getStartY() {
        return y1;
    }

    public List<Piece> getCaptures() {
        return captures;
    }

    private boolean calculateIsKingCapture() {
        if (isCapture()) {
            Piece enemyKing = captures.getFirst().getTeam().getKing();
            int[] enemyKingCoOrds = enemyKing.getCoOrds();
            for (Piece piece : captures) {
                int[] coOrds = piece.getCoOrds();
                if (enemyKingCoOrds[0] == coOrds[0]
                        && enemyKingCoOrds[1] == coOrds[1]) return true;
            }
        }
        return false;
    }

//    public boolean isKingCapture() {
//        return capturesKing;
//    }

    public Piece getPiece() {
        return piece;
    }
}
