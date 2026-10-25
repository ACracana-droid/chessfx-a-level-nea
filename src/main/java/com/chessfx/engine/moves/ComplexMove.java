package com.chessfx.engine.moves;

import com.chessfx.engine.pieces.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ComplexMove extends Move {
    private final List<Piece> captures; // could be null

    protected ComplexMove(Piece piece, int y, int x, List<Piece> captures) {
        super(piece, y, x, true);
        this.captures = Objects.requireNonNullElseGet(captures, ArrayList::new);
    }

    @Override
    public List<Piece> getCaptures() {
        return captures;
    }

    @Override
    public boolean isCapture() {
        return !captures.isEmpty();
    }

    @Override
    public boolean isBasicCapture() {
        return isCapture();
    }

    @Override
    public boolean isSuperCapture() {
        return captures.isEmpty();
    }
}
