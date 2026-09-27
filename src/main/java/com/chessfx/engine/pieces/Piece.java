package com.chessfx.engine.pieces;

import com.chessfx.engine.board.BoardContainer;
import com.chessfx.engine.board.Team;
import com.chessfx.engine.moves.Move;
import com.chessfx.engine.moves.MoveVector;
import com.chessfx.log.Log;

import java.util.ArrayList;
import java.util.List;

abstract public class Piece extends PieceBase {

    final protected String name;
    final protected String fenId;
    final protected int value;
    final protected Team team;
    final protected MoveVector[] moveSet;

    protected int[] coOrds;
    protected boolean hasMoved;


    protected List<Move> movesCache;


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

    public boolean hasMoved() {
        return hasMoved;
    }

    public List<Move> moves(BoardContainer boardModel) {
//        if (isMovesCacheValid(boardModel)) {
//            movesCache = calculateMoves(boardModel);
//        }
//        return movesCache;
        return calculateMoves(boardModel);
    }

    private boolean isMovesCacheValid(BoardContainer boardModel) {
        if (movesCache == null) return false;

        if (false) return true;

        return false;
    }

    private List<Move> calculateMoves(BoardContainer boardModel) {
        Log.info("Querying moves: " + team + " " + name);
        List<Move> ls = new ArrayList<>();
        for (MoveVector mv : filterInitialMoveVectors()) {
            if (mv.hasInterval()) {
                if (mv.isLeaping()) {
                    // don't need to check interval.
                    Log.debug(Log.Priority.LOW, "Move is leaping (like knight).");
                    handleConditions(mv, boardModel, ls);
                } else {
                    Log.debug(Log.Priority.LOW, "Move has interval and is not leaping! Oh no...");
                    // must check interval, check intermediate steps.
                    handleConditionsCheckingInterval(mv, boardModel, ls);
                    // isHopping should be handled here.
                }
                continue;
            }
            Log.debug(Log.Priority.LOW, "Move has no interval and travels in a single direction");
            // move has no interval - basic move in a single direction.
            handleConditions(mv, boardModel, ls);
        }
        return ls;
    }

    private void handleConditionsCheckingInterval(MoveVector mv, BoardContainer boardModel, List<Move> ls) {
        Log.warn("Piece", "Unimplemented method");
    }

    private void handleConditions(MoveVector mv, BoardContainer boardModel, List<Move> ls) {
        if (mv.isCaptureOnly()) {
            captureMoves(mv, boardModel, ls);
        } else if (mv.isNotCapture()) {
            nonCaptureMoves(mv, boardModel, ls);
        } else {
            // default
            standardMoves(mv, boardModel, ls);
        }
    }

    private void standardMoves(MoveVector mv, BoardContainer boardModel, List<Move> ls) {
        Log.debug(Log.Priority.LOW, "Piece", "executing standardMoves procedure.");
        int y = coOrds[0] + mv.getDy(), x = coOrds[1] + mv.getDx();
        int steps = mv.getSteps();
        while (steps != 0 && boardModel.inBoardBounds(y, x)) {
            if (boardModel.tileHasPiece(y, x)) {
                if (!boardModel.tileHasPieceMatchingTeam(y, x)) {
                    ls.add(new Move(this, y, x, List.of(boardModel.getTile(y, x).getPiece())));
                }
                return; // is stopped by allied piece and will stop after capturing enemy.
            } else {
                ls.add(new Move(this, y, x));
            }
            y += mv.getDy();
            x += mv.getDx();
            steps--;
        }
    }

    private void nonCaptureMoves(int dy, int dx, int steps, BoardContainer boardModel, List<Move> ls) {
        Log.debug(Log.Priority.LOW, "Piece", "executing nonCaptureMoves procedure.");
        int y = coOrds[0] + dy, x = coOrds[1] + dx;
        while (steps != 0 && boardModel.inBoardBounds(y, x)) {
            if (boardModel.tileHasPiece(y, x)) {
                return;
            }
            ls.add(new Move(this, y, x));
            y += dy;
            x += dx;
            steps--;
        }
    }

    private void nonCaptureMoves(MoveVector mv, BoardContainer boardModel, List<Move> ls) {
        nonCaptureMoves(mv.getDy(), mv.getDx(), mv.getSteps(), boardModel, ls);
    }

    // finish this.
    private void captureMoves(MoveVector mv, BoardContainer boardModel, List<Move> ls) {
        int y = coOrds[0] + mv.getDy(), x = coOrds[1] + mv.getDx();
        int steps = mv.getSteps();
        while (steps != 0 && boardModel.inBoardBounds(y, x)) {
            if (boardModel.tileHasPieceMatchingTeam(y, x)
                    || !boardModel.tileHasPiece(y, x)) {
                return;
            }
            List<Piece> captures = List.of(boardModel.getTile(y, x).getPiece());
            ls.add(new Move(this, y, x, captures));
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

    public int[] getCoOrds() {
        return coOrds;
    }

    public List<Move> captures(BoardContainer boardContainer) {
        List<Move> ls = new ArrayList<>();
        for (MoveVector mv : filterInitialAndNonCaptureMoveVectors()) {
            if (mv.hasInterval()) {
                if (mv.isLeaping()) {
                    // don't need to check interval.
                    Log.debug(Log.Priority.LOW, "Move is leaping (like knight).");
                    handleConditions(mv, boardContainer, ls);
                } else {
                    Log.debug(Log.Priority.LOW, "Move has interval and is not leaping! Oh no...");
                    // must check interval, check intermediate steps.
                    handleConditionsCheckingInterval(mv, boardContainer, ls);
                    // isHopping should be handled here.
                }
                continue;
            }
            Log.debug(Log.Priority.LOW, "Move has no interval and travels in a single direction");
            // move has no interval - basic move in a single direction.
            handleConditions(mv, boardContainer, ls);

        }
        List<Move> captures = new ArrayList<>();
        for (int i = 0; i < ls.size(); i++) {
            if (ls.get(i).isCapture()) {
                captures.add(ls.get(i));
            }
        }
        return captures;
    }

    private List<MoveVector> filterInitialMoveVectors() {
        List<MoveVector> ls = new ArrayList<>();
        for (MoveVector mv : moveSet) {
            if (mv.isInitialOnly() && hasMoved) {
                Log.debug(Log.Priority.LOW, "Skipping initial move because piece has moved.");
                continue;
            }
            ls.add(mv);
        }
        return ls;
    }

    private List<MoveVector> filterInitialAndNonCaptureMoveVectors() {
        List<MoveVector> ls = new ArrayList<>();
        for (MoveVector mv : moveSet) {
            if ((mv.isInitialOnly() && hasMoved) || mv.isNotCapture()) {
                Log.debug(Log.Priority.LOW, "Skipping move because either it is an initial move and piece has moved, " +
                        "or it is a non-capture move.");
                continue;
            }
            ls.add(mv);
        }
        return ls;
    }
}
