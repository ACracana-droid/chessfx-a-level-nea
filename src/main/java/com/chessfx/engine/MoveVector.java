package com.chessfx.engine;

public class MoveVector implements Cloneable {


    // 1 1 1 1 1 1 1 int int int


    // Conditions:
    final boolean[] flags;
//    final boolean isInitialOnly; // i
//    final boolean isCaptureOnly; // c
//    final boolean isNotCapture; // o
//
//    // Move Type:
    /// /    boolean isSliding; // the default
//    final boolean isLeaping; // ~
//    final boolean isHopping; // ^


    // Distance & Direction:
//    boolean isSpecific; // otherwise move repeats for each cardinal direction.
    final int dy;
    final int dx;
    final int steps; // -1 means infinite

    public MoveVector(int[] vector, boolean[] flags, int steps) {
        this.dy = vector[0];
        this.dx = vector[1];
        this.flags = flags;
        assignFlags(flags);
        this.steps = steps;
    }

    private void assignFlags(boolean[] flags) {
//        this.flags = flags;
//        isInitialOnly = flags[0];
//        isCaptureOnly = flags[1];
//        isNotCapture = flags[2];
//        isLeaping = flags[3];
//        isHopping = flags[4];
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MoveVector moveObj) {
            return dy == moveObj.dy && dx == moveObj.dx
                    && steps == moveObj.steps;
        } else return super.equals(obj);
    }

    @Override
    public String toString() {

        return steps + "*(" + dy + ", " + dx + ")";
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


    public boolean isCaptureOnly() {
        return flags[1];
    }

    public boolean isInitialOnly() {
        return flags[0];
    }

    public boolean isNotCapture() {
        return flags[2];
    }

    public boolean isHopping() {
        return flags[4];
    }

    public boolean isLeaping() {
        return flags[3];
    }

    public boolean hasInterval() {
        return dy > 1 || dx > 1;
    }

    @Override
    public MoveVector clone() {
        try {
            MoveVector clone = (MoveVector) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
