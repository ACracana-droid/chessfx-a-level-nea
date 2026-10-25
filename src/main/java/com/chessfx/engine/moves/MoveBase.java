package com.chessfx.engine.moves;

public class MoveBase {

    final protected Coordinate orig, dest;

    public MoveBase(Coordinate orig, Coordinate dest) {
        this.orig = orig;
        this.dest = dest;
    }

    public String internalNotation() {
        StringBuffer sb = new StringBuffer();
        sb.append("(").append(orig.y()).append(", ").append(orig.x()).append(")");
        sb.append(" to ");
        sb.append("(").append(dest.y()).append(", ").append(dest.x()).append(")");

        return sb.toString();
    }

    @Override
    public String toString() {
        return internalNotation();
    }



    public Coordinate startCoOrds() {
        return orig;
    }

    public Coordinate destCoOrds() {
        return dest;
    }

    public int getStartY() {
        return orig.y();
    }

    public int getStartX() {
        return orig.x();
    }


    public int getEndY() {
        return dest.y();
    }

    public int getEndX() {
        return dest.x();
    }
}
