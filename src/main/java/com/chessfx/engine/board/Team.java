package com.chessfx.engine.board;

import com.chessfx.engine.moves.MoveVector;
import com.chessfx.engine.pieces.King;
import com.chessfx.engine.pieces.Piece;

import java.util.*;

public class Team {

    private int[] moveTransform;
    private final int idx;

    //   p for player, b for bot
    private String name;
    private char playerType;

    private King king;
    final private List<Piece> pieceList = new ArrayList<>();
    final private List<Piece> capturedPieceList = new ArrayList<>();

    final private Map<String, Set<MoveVector>> moveSets = new HashMap<>();

    final private String styleClass;

    public Team(String styleClass, int[] moveTransform, int idx) {
        this.styleClass = styleClass;
        this.moveTransform = moveTransform;
        this.idx = idx;
    }

    Team(Team team) {
        this.styleClass = team.styleClass;
        this.moveTransform = team.moveTransform;
        this.idx = team.idx;
    }

    public List<Piece> getPieceList() {
        return pieceList;
    }

    public String getStyleClassString() {
        return styleClass;
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

    public String getName() {
        return styleClass;
    }

    public boolean isBot() {
        return playerType == 'b';
    }

    public King getKing() {
        return king;
    }

    public void setKing(King king) {
        this.king = king;
    }

    public Map<String, Set<MoveVector>> getMoveSets() {
        return moveSets;
    }

    public void addMoveSetToList(String fenId, Set<MoveVector> set) {
        moveSets.put(fenId, set);
    }

    public void removeCapture(Piece capture) {
        capturedPieceList.remove(capture);
    }

    public List<Piece> getCapturedPieces() {
        return capturedPieceList;
    }


    public int idx() {
        return idx;
    }
}
