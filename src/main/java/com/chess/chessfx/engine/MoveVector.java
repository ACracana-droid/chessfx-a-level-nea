package com.chess.chessfx.engine;

public class MoveVector {
    int dy;
    int dx;

    public MoveVector(int[] arrVector) {
        this.dy = arrVector[0];
        this.dx = arrVector[1];
        isSliding = true; // the default chess value
    }

    public MoveVector(int dy, int dx) {
        this.dy = dy;
        this.dx = dx;
        isSliding = true; // the default chess value
    }

    boolean isLeaping;
    boolean isSliding;
    int repetitions;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MoveVector moveObj) {
            return dy == moveObj.dy && dx == moveObj.dx && isLeaping == moveObj.isLeaping
                    && isSliding == moveObj.isSliding && repetitions == moveObj.repetitions;
        } else return super.equals(obj);
    }
}
