package com.chess.chessfx.engine;

public class Move {

    int y1, x1, y2, x2;
    boolean isCapture;

    // Should be inspired by SAN.
    public Move(int y1, int x1, int y2, int x2, boolean isCapture) {
        this.y1 = y1;
        this.x1 = x1;
        this.y2 = y2;
        this.x2 = x2;
        this.isCapture = isCapture;
    }

    public boolean isCapture() {
        return isCapture;
    }

    public int getEndY() {
        return y2;
    }

    public int getEndX() {
        return x2;
    }


    // must be adapted for non-standard boards.
    public String algebraicNotation() {
        StringBuffer sb = new StringBuffer();
        if (isCapture)
            sb.append("x");


        return sb.toString();
    }

    @Override
    public String toString() {
        return algebraicNotation();
    }
}
