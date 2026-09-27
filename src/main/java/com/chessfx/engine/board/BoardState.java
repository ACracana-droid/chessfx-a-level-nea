package com.chessfx.engine.board;

public enum BoardState {
    DEFAULT("DEFAULT"),
    CHECK("CHECK"),
    CHECKMATE("CHECKMATE"),
    STALEMATE("STALEMATE");

    private final String text;

    BoardState(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
