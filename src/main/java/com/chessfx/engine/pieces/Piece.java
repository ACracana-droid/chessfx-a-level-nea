package com.chessfx.engine.pieces;

import com.chessfx.engine.board.BoardContainer;
import com.chessfx.engine.board.GameBoardContainer;
import com.chessfx.engine.board.BoardContainerBase;
import com.chessfx.engine.board.Team;
import com.chessfx.engine.moves.Coordinate;
import com.chessfx.engine.moves.Move;
import com.chessfx.engine.moves.MoveVector;
import com.chessfx.log.Log;

import java.util.ArrayList;
import java.util.List;

public class Piece extends PieceBase implements Cloneable {

    protected List<Move> movesCache;
    final protected MoveVector[] moveSet;

    Piece(String name, String fenId, Team team, int value,
          int rank, int file, MoveVector[] moveSet, boolean hasMoved) {
        super(name, fenId, value, team, new Coordinate(rank, file));
        this.moveSet = moveSet;
        this.hasMoved = hasMoved;
    }

    Piece(Piece piece, Team team) {
        super(piece.name, piece.fenId, piece.value, team, piece.getCoOrds());
        this.moveSet = piece.moveSet;
        this.hasMoved = piece.hasMoved;
    }

    // TODO: fix pawn moves, which are totally broken.
    // Pieces remain on the board after being captured in a sort of ghost state.

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

    private boolean isMovesCacheValid(GameBoardContainer boardModel) {
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
                    handleConditions(mv, boardModel, ls);
                } else {
                    // must check interval, check intermediate steps.
                    handleConditionsCheckingInterval(mv, boardModel, ls);
                    // isHopping should be handled here.
                }
                continue;
            }
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
        int y = coOrds.y() + mv.getDy(), x = coOrds.x() + mv.getDx();
        int steps = mv.getSteps();
        while (steps != 0 && boardModel.inBoardBounds(y, x)) {
            if (boardModel.tileHasPiece(y, x)) {
                if (!boardModel.tileHasPieceMatchingTeam(y, x, getTeam())) {
                    ls.add(capture(this, y, x));
                }
                return; // is stopped by allied piece and will stop after capturing enemy.
            } else {
                ls.add(make(this, y, x));
            }
            y += mv.getDy();
            x += mv.getDx();
            steps--;
        }
    }

    private Move capture(Piece piece, int y, int x) {
        return Move.capture(piece, y, x);
    }

    private Move make(Piece piece, int y, int x) {
        return Move.make(piece, y, x);
    }

    private void nonCaptureMoves(int dy, int dx, int steps, BoardContainer boardModel, List<Move> ls) {
        int y = coOrds.y() + dy, x = coOrds.x() + dx;
        while (steps != 0 && boardModel.inBoardBounds(y, x)) {
            if (boardModel.tileHasPiece(y, x)) {
                return;
            }
            ls.add(make(this, y, x));

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
        int y = coOrds.y() + mv.getDy(), x = coOrds.x() + mv.getDx();
        int steps = mv.getSteps();
        while (steps != 0 && boardModel.inBoardBounds(y, x)) {
            if (boardModel.tileHasPieceMatchingTeam(y, x, getTeam())
                    || !boardModel.tileHasPiece(y, x)) {
                return;
            }
            ls.add(capture(this, y, x));
            y += mv.getDy();
            x += mv.getDx();
            steps--;
        }
    }


    private void captureMovesThreat(MoveVector mv, BoardContainerBase boardModel, List<Move> ls) {
        int y = coOrds.y() + mv.getDy(), x = coOrds.x() + mv.getDx();
        int steps = mv.getSteps();
        while (steps != 0 && boardModel.inBoardBounds(y, x)) {
            if (boardModel.tileHasPieceMatchingTeam(y, x, getTeam())) {
                return;
            }
            ls.add(capture(this, y, x));
            y += mv.getDy();
            x += mv.getDx();
            steps--;
        }
    }

    public void setCoOrds(int y, int x) {
        coOrds = new Coordinate(y, x);
    }

    public void setCoOrds(Coordinate coOrds) {
        this.coOrds = coOrds;
    }

    public void setHasMoved(boolean b) {
        hasMoved = b;
    }

    @Override
    public Piece clone() {
        try {
            Log.warn("Hello piece clone...");
            Piece clone = (Piece) super.clone();
//            clone = new Piece(this);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }


    public Coordinate getCoOrds() {
        return coOrds;
    }

    /**
     * Returns a list of possible captures (not threats) from the current board state.
     *
     * @param boardContainer the board
     * @return list of valid captures
     */
    public List<Move> captures(GameBoardContainer boardContainer) {
        List<Move> ls = new ArrayList<>();
        for (MoveVector mv : filterInitialAndNonCaptureMoveVectors()) {
            if (mv.hasInterval()) {
                if (mv.isLeaping()) {
                    // don't need to check interval.
                    handleConditions(mv, boardContainer, ls);
                } else {
                    // must check interval, check intermediate steps.
                    handleConditionsCheckingInterval(mv, boardContainer, ls);
                    // isHopping should be handled here.
                }
                continue;
            }
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
                Log.debug(Log.Priority.LOWEST, "Skipping move because either it is an initial move and piece has moved, " +
                        "or it is a non-capture move.");
                continue;
            }
            ls.add(mv);
        }
        return ls;
    }

    public List<Move> threats(BoardContainer boardContainer) {
        List<Move> ls = new ArrayList<>();
        for (MoveVector mv : filterInitialAndNonCaptureMoveVectors()) {
            if (mv.hasInterval()) {
                if (mv.isLeaping()) {
                    // don't need to check interval.
                    if (mv.isCaptureOnly()) captureMovesThreat(mv, boardContainer, ls);
                    else standardMoves(mv, boardContainer, ls);
                } else {
                    // must check interval, check intermediate steps.
                    //TODO: fix this...
                    if (mv.isCaptureOnly()) captureMovesThreat(mv, boardContainer, ls);
                    else standardMoves(mv, boardContainer, ls);
                    // isHopping should be handled here.
                }
                continue;
            }
            // move has no interval - basic move in a single direction.
            if (mv.isCaptureOnly()) captureMovesThreat(mv, boardContainer, ls);
            else standardMoves(mv, boardContainer, ls);

        }
        return ls;
    }

    public boolean isKing() {
        return this instanceof King;
    }
}
