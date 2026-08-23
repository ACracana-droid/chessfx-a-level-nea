package com.chessfx.engine.pieces;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private int[] moveTransform;

    //   p for player, b for bot
    private String name;
    private char playerType;

    final private List<Piece> pieceList;
    final private List<Piece> capturedPieceList;

    final private String styleClass;

    public Team(String styleClass, int[] moveTransform, char playerType) {
        this.styleClass = styleClass;
        this.moveTransform = moveTransform;
        this.playerType = playerType;
        pieceList = new ArrayList<>();
        capturedPieceList = new ArrayList<>();
    }


    public List<Piece> getPieceList() {
        return pieceList;
    }

    public String getStyleClassString() {
        return styleClass;
    }

    public void addCapture(Piece capture) {
        capturedPieceList.add(capture);
    }

    public int[] getMoveTransform() {
        return moveTransform;
    }

    public void setDirection(int[] vector) {
        this.moveTransform = vector;
    }

    public void setPlayerType(char c) {
        playerType = c;
    }

    public char getPlayerType() {
        return playerType;
    }
}
