package com.chess.chessfx.engine;

public class MoveVector {
    int dy;
    int dx;

    MoveVector(int dy, int dx) {
        this.dy = dy;
        this.dx = dx;
        isSliding = true; // the default chess value
    }

    boolean isLeaping;
    boolean isSliding;
    int repititions;
}
