package com.chessfx.engine.moves;

public record Coordinate(int y, int x) {

    public boolean isSame(int y, int x) {
        return y() == y && x() == x;
    }
}
