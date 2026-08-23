package com.chessfx.engine.pieces;

import com.chessfx.engine.ChessGameModel;
import com.chessfx.engine.Move;
import com.chessfx.engine.MoveVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract public class Piece {

    protected String name;
    protected final String fenId;
    protected int value;
    protected Team team;
    protected final MoveVector[] moveSet;
    protected int[] coOrds;
    protected boolean hasMoved;

    public Piece(String name, String fenId, Team team, int value,
                 int rank, int file, MoveVector[] moveSet, boolean hasMoved) {
        this.name = name;
        this.fenId = fenId;
        this.team = team;
        this.value = value;
        this.moveSet = moveSet;
        this.hasMoved = hasMoved;
        coOrds = new int[]{rank, file};
        team.getPieceList().add(this);
    }


    public String getFenID() {
        return fenId;
    }

    public Team getTeam() {
        return team;
    }

    public String getName() {
        return name;
    }

    public MoveVector[] getMoveSet() {
        return moveSet;
    }

    public List<Move> moves(ChessGameModel boardModel) {
        System.out.println("Querying moves: " + team + " " + name);
        List<Move> ls = new ArrayList<>();
        for (MoveVector mv : moveSet) {
            if (mv.isInitialOnly() && hasMoved) {
                System.out.println("Skipping initial move because piece has moved.");
                continue;
            }
            if (mv.hasInterval()) {
                if (mv.isLeaping()) {
                    // don't need to check interval.
                    System.out.println("Move is leaping (like knight).");
                    handleConditions(mv, boardModel, ls);
                } else {
                    System.out.println("Move has interval and is not leaping! Oh no...");
                    // must check interval, check intermediate steps.
                    handleConditionsCheckingInterval(mv, boardModel, ls);
                    // isHopping should be handled here.
                }
                continue;
            }
            System.out.println("Move has no interval and travels in a single direction");
            // move has no interval - basic move in a single direction.
            handleConditions(mv, boardModel, ls);
        }
        return ls;
    }

    private void handleConditionsCheckingInterval(MoveVector mv, ChessGameModel boardModel, List<Move> ls) {
        System.out.println("Unimplemented.");
    }

    private void handleConditions(MoveVector mv, ChessGameModel boardModel, List<Move> ls) {
        if (mv.isCaptureOnly()) {
            captureMoves(mv, boardModel, ls);
        } else if (mv.isNotCapture()) {
            nonCaptureMoves(mv, boardModel, ls);
        } else {
            // default
            standardMoves(mv, boardModel, ls);
        }
    }

    private void standardMoves(MoveVector mv, ChessGameModel boardModel, List<Move> ls) {
        System.out.println("Doing standardMoves process:");
        int y = coOrds[0] + mv.getDy(), x = coOrds[1] + mv.getDx();
        System.out.println("y=" + y + " x=" + x);
        int steps = mv.getSteps();
        while (steps != 0 && boardModel.inBoardBounds(y, x)) {
            if (boardModel.tileHasPiece(y, x)) {
                if (!boardModel.tileHasPieceMatchingTeam(y, x)) {
                    ls.add(new Move(coOrds[0], coOrds[1], y, x, true));
                }
                return; // is stopped by allied piece and will stop after capturing enemy.
            } else {
                ls.add(new Move(coOrds[0], coOrds[1], y, x, false));
            }
            y += mv.getDy();
            x += mv.getDx();
            steps--;
        }
    }

    private void nonCaptureMoves(int dy, int dx, int steps, ChessGameModel boardModel, List<Move> ls) {
        System.out.println("Doing non capture move.");
        int y = coOrds[0] + dy, x = coOrds[1] + dx;
        while (steps != 0 && boardModel.inBoardBounds(y, x)) {
            System.out.println("(" + y + ", " + x + ")");
            if (boardModel.tileHasPiece(y, x)) {
                return;
            }
            ls.add(new Move(coOrds[0], coOrds[1], y, x, false));
            y += dy;
            x += dx;
            steps--;
        }
    }

    private void nonCaptureMoves(MoveVector mv, ChessGameModel boardModel, List<Move> ls) {
        nonCaptureMoves(mv.getDy(), mv.getDx(), mv.getSteps(), boardModel, ls);
    }

    // finish this.
    private void captureMoves(MoveVector mv, ChessGameModel boardModel, List<Move> ls) {
        int y = coOrds[0] + mv.getDy(), x = coOrds[1] + mv.getDx();
        int steps = mv.getSteps();
        while (steps != 0 && boardModel.inBoardBounds(y, x)) {
            if (boardModel.tileHasPieceMatchingTeam(y, x)
                    || !boardModel.tileHasPiece(y, x)) {
                return;
            }
            ls.add(new Move(coOrds[0], coOrds[1], y, x, true));
            y += mv.getDy();
            x += mv.getDx();
            steps--;
        }
    }

    public void setCoOrds(int y2, int x2) {
        coOrds[0] = y2;
        coOrds[1] = x2;
    }

    public void setHasMoved(boolean b) {
        hasMoved = b;
    }
}
