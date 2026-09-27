package com.chessfx.engine.moves;

public class MoveBase {

    final protected int y1, x1, y2, x2;

    public MoveBase(int y1, int x1, int y2, int x2) {
        // start
        this.y1 = y1;
        this.x1 = x1;

        // destination
        this.y2 = y2;
        this.x2 = x2;
    }
}
