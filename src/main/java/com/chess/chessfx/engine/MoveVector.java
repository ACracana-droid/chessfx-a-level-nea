package com.chess.chessfx.engine;

public class MoveVector {

    MoveVector linkedMoveVector = null;

    // 1 1 1 1 1 1 1 int int int


    // Conditions:
    boolean[] flags;
    boolean isInitialOnly; // i
    boolean isCaptureOnly; // c
    boolean isNotCapture; // o

    // Move Type:
//    boolean isSliding; // the default
    boolean isLeaping; // ~
    boolean isHopping; // ^


    // Distance & Direction:
//    boolean isSpecific; // otherwise move repeats for each cardinal direction.
    int dy;
    int dx;
    int steps; // -1 means infinite

    public MoveVector(int[] vector, boolean[] flags, int steps) {
        this.dy = vector[0];
        this.dx = vector[1];
        assignFlags(flags);
        this.steps = steps;
    }

    public MoveVector(int dy, int dx, boolean[] flags, int steps) {
        this.dy = dy;
        this.dx = dx;
        assignFlags(flags);
        this.steps = steps;
    }

    private void assignFlags(boolean[] flags) {
        this.flags = flags;
        isInitialOnly = flags[0];
        isCaptureOnly = flags[1];
        isNotCapture = flags[2];
        isLeaping = flags[3];
        isHopping = flags[4];
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MoveVector moveObj) {
            return dy == moveObj.dy && dx == moveObj.dx && isLeaping == moveObj.isLeaping
                    && steps == moveObj.steps;
        } else return super.equals(obj);
    }

    @Override
    public String toString() {

        return  steps + "*(" + dy + ", " + dx + ")";
    }

    public int getDy() {
        return dy;
    }

    public int getDx() {
        return dx;
    }

    public boolean[] getFlags() {
        return flags;
    }

    public int getSteps() {
        return steps;
    }

    public void flipYValues() {
        dy = -dy;
    }

    public boolean isCaptureOnly() {
        return isCaptureOnly;
    }

    public boolean isInitialOnly() {
        return isInitialOnly;
    }

    public boolean isNotCapture() {
        return isNotCapture;
    }

    public boolean isHopping() {
        return isHopping;
    }

    public boolean isLeaping() {
        return isLeaping;
    }

    public boolean hasInterval() {
        return dy > 1 || dx > 1;
    }
}
