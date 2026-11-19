package com.chessfx.engine.moves;

import com.chessfx.engine.pieces.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ComplexMove extends Move {
    private final List<Coordinate> captureCoordinates; // could be null

    protected ComplexMove(Piece piece, int y, int x, List<Coordinate> captureCoordinates) {
        super(piece, y, x, true);
        this.captureCoordinates = Objects.requireNonNullElseGet(captureCoordinates, ArrayList::new);
    }

    @Override
    public List<Coordinate> getCaptureCoordinates() {
        return captureCoordinates;
    }

    @Override
    public boolean isCapture() {
        return !captureCoordinates.isEmpty();
    }

    @Override
    public boolean isBasicCapture() {
        return isCapture();
    }

    @Override
    public boolean isSuperCapture() {
        return captureCoordinates.isEmpty();
    }
}
