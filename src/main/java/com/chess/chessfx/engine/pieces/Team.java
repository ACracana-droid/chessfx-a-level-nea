package com.chess.chessfx.engine.pieces;

import java.util.ArrayList;
import java.util.List;

public enum Team {
    WHITE("light", "white", "black"),
    BLACK("dark", "black", "white"),
    RED("", "white", "red"),
    BLUE("", "white", "red"),
    YELLOW("", "white", "red");

    final private List<Piece> pieceList;

    final private String styleClass;
    private String fill;
    private String stroke;


    Team(String styleClass, String fill, String stroke) {
        this.styleClass = styleClass;
        this.fill = fill;
        this.stroke = stroke;
        pieceList = new ArrayList<>();

    }

    public List<Piece> getPieceList() {
        return pieceList;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }


    public String getStyleClassString() {
        return styleClass;
    }

    public String getFill() {
        return fill;
    }

    public String getStroke() {
        return stroke;
    }
}
