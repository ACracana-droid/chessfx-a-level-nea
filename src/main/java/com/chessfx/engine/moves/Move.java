package com.chessfx.engine.moves;

import com.chessfx.engine.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class Move extends MoveBase {

    /*
    captureList:
    The plan is to make a capture list that I can use to revive pieces at will.
     */
    protected final Piece piece;
    private final boolean isCapture;


    protected Move(Piece piece, Coordinate dest, boolean isCapture) {
        super(piece.getCoOrds(), dest);
        this.piece = piece;
        this.isCapture = isCapture;
    }

    protected Move(Piece piece, int y, int x, boolean isCapture) {
        super(piece.getCoOrds(), new Coordinate(y, x));
        this.piece = piece;
        this.isCapture = isCapture;
    }

    public static Move make(Piece piece, int y, int x) {
        return new Move(piece, y, x, false);
    }

    public static Move make(Piece piece, int y, int x, List<Piece> captures) {
        return new ComplexMove(piece, y, x, captures);
    }



    public static Move capture(Piece piece, int y, int x) {
        return new Move(piece, y, x, true);
    }


    /**
     * Does the move remove one or more enemy piece?
     * It does not matter whether it removes the piece at the move destination or another piece.
     *
     * @return bool
     */
    public boolean isCapture() {
        return isCapture;
    }


    /**
     * Does the move remove an enemy piece which is on the destination tile?
     *
     * @return bool
     */
    public boolean isBasicCapture() {
        return isCapture;
    }

    /**
     * Does the move remove a piece which is not at the destination tile?
     * @return bool
     */
    public boolean isSuperCapture() {
        return false;
    }

    public List<Piece> getCaptures() {
        return new ArrayList<>();
    }


    @Override
    public String internalNotation() {
        StringBuffer sb = new StringBuffer();
        if (isCapture())
            sb.append("x");
        sb.append("(").append(orig.y()).append(", ").append(orig.x()).append(")");
        sb.append(" to ");
        sb.append("(").append(dest.y()).append(", ").append(dest.x()).append(")");

        return sb.toString();
    }

    public Piece getPiece() {
        return piece;
    }
}
