package com.chessfx.engine.pieces;

import com.chessfx.engine.board.BoardContainer;
import com.chessfx.engine.board.GameBoardContainer;
import com.chessfx.engine.board.Team;
import com.chessfx.engine.moves.Coordinate;
import com.chessfx.engine.moves.Move;
import com.chessfx.engine.moves.MoveVector;
import com.chessfx.engine.moves.PromotionMove;
import com.chessfx.log.Log;

import java.util.ArrayList;
import java.util.List;

public class Piece extends PieceBase {

    protected List<Move> movesCache;
    final protected MoveVector[] moveSet;
    protected BoardContainer game;

    Piece(Piece piece, Team team) {
        super(piece.name, piece.fenId, piece.value, team, piece.getPos());
        this.moveSet = piece.moveSet;
        this.hasMoved = piece.hasMoved;
    }

    public Piece(String name, Team team, int rank, int file, boolean hasMoved, PieceData pieceData) {
        super(name, pieceData.fenId, pieceData.value, team, new Coordinate(rank, file));
        this.hasMoved = hasMoved;
        this.moveSet = pieceData.getAllMoveVectors(team).toArray(new MoveVector[0]);
    }

    // TODO: fix pawn moves, which are totally broken.
    // Pieces remain on the board after being captured in a sort of ghost state.


    public boolean hasMoved() {
        return hasMoved;
    }

    public List<Move> moves() {
//        if (isMovesCacheValid(boardModel)) {
//            movesCache = calculateMoves(boardModel);
//        }
//        return movesCache;
        return calculateMoves();
    }

    private boolean isMovesCacheValid() {
        if (movesCache == null) return false;

        if (false) return true;

        return false;
    }

    protected List<Move> calculateMoves() {
        Log.info("Querying moves: " + team + " " + name);
        List<Move> ls = new ArrayList<>();
        for (MoveVector mv : filterInitialMoveVectors()) {
            if (mv.hasInterval()) {
                if (mv.isLeaping()) {
                    // don't need to check interval.
                    handleConditions(mv, ls);
                } else {
                    // must check interval, check intermediate steps.
                    handleConditionsCheckingInterval(mv, ls);
                    // isHopping should be handled here.
                }
                continue;
            }
            // move has no interval - basic move in a single direction.
            handleConditions(mv, ls);
        }
        return ls;
    }

    private void handleConditionsCheckingInterval(MoveVector mv, List<Move> ls) {
        Log.warn("Piece", "Unimplemented method");
    }

    private void handleConditions(MoveVector mv, List<Move> ls) {
        if (mv.isCaptureOnly()) {
            captureMoves(mv, ls);
        } else if (mv.isNotCapture()) {
            nonCaptureMoves(mv, ls);
        } else {
            // default
            standardMoves(mv, ls);
        }
    }

    private void standardMoves(MoveVector mv, List<Move> ls) {
        int y = pos.y() + mv.getDy(), x = pos.x() + mv.getDx();
        int steps = mv.getSteps();
        while (steps != 0 && game.inBoardBounds(y, x)) {
            if (game.tileHasPiece(y, x)) {
                if (!game.tileHasPieceMatchingTeam(y, x, getTeam())) {
                    ls.addAll(capture(this, y, x));
                }
                return; // is stopped by allied piece and will stop after capturing enemy.
            } else {
                ls.addAll(make(this, y, x));
            }
            y += mv.getDy();
            x += mv.getDx();
            steps--;
        }
    }

    protected List<Move> capture(Piece piece, int y, int x) {
        if (isPromotion(y)) {
            List<Move> promoteList = new ArrayList<>();
            ArbitraryPieceFactory pieceFactory = game.getGameModel().getChessConfig().getPieceFactory();
            for (String name : game.getGameModel().getChessConfig().getPromotablePieces()) {
                promoteList.add(new PromotionMove(piece, name, y, x, true));
            }
            return promoteList;
        }
        return List.of(Move.capture(piece, y, x));
    }

    protected boolean isPromotion(int y) {
        return false;
    }

    protected List<Move> make(Piece piece, int y, int x) {
        if (isPromotion(y)) {
            List<Move> promoteList = new ArrayList<>();
            ArbitraryPieceFactory pieceFactory = game.getGameModel().getChessConfig().getPieceFactory();
            for (String name : game.getGameModel().getChessConfig().getPromotablePieces()) {
                promoteList.add(new PromotionMove(piece, name, y, x, false));
            }
            return promoteList;
        }
        return List.of(Move.make(piece, y, x));
    }

    private void nonCaptureMoves(int dy, int dx, int steps, BoardContainer boardModel, List<Move> ls) {
        int y = pos.y() + dy, x = pos.x() + dx;
        while (steps != 0 && boardModel.inBoardBounds(y, x)) {
            if (boardModel.tileHasPiece(y, x)) {
                return;
            }
            ls.addAll(make(this, y, x));

            y += dy;
            x += dx;
            steps--;
        }
    }

    private void nonCaptureMoves(MoveVector mv, List<Move> ls) {
        nonCaptureMoves(mv.getDy(), mv.getDx(), mv.getSteps(), game, ls);
    }

    // finish this.
    private void captureMoves(MoveVector mv, List<Move> ls) {
        int y = pos.y() + mv.getDy(), x = pos.x() + mv.getDx();
        int steps = mv.getSteps();
        while (steps != 0 && game.inBoardBounds(y, x)) {
            if (game.tileHasPieceMatchingTeam(y, x, getTeam())
                    || !game.tileHasPiece(y, x)) {
                return;
            }
            ls.addAll(capture(this, y, x));
            y += mv.getDy();
            x += mv.getDx();
            steps--;
        }
    }


    private void captureMovesThreat(MoveVector mv, List<Move> ls) {
        int y = pos.y() + mv.getDy(), x = pos.x() + mv.getDx();
        int steps = mv.getSteps();
        while (steps != 0 && game.inBoardBounds(y, x)) {
            if (game.tileHasPieceMatchingTeam(y, x, getTeam())) {
                return;
            }
            ls.addAll(capture(this, y, x));
            y += mv.getDy();
            x += mv.getDx();
            steps--;
        }
    }

    public void setCoOrds(int y, int x) {
        pos = new Coordinate(y, x);
    }

    public void setCoOrds(Coordinate coOrds) {
        this.pos = coOrds;
    }

    public void setHasMoved(boolean b) {
        hasMoved = b;
    }


    public Coordinate getPos() {
        return pos;
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
                    handleConditions(mv, ls);
                } else {
                    // must check interval, check intermediate steps.
                    handleConditionsCheckingInterval(mv, ls);
                    // isHopping should be handled here.
                }
                continue;
            }
            // move has no interval - basic move in a single direction.
            handleConditions(mv, ls);

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

    public List<Move> threats() {
        List<Move> ls = new ArrayList<>();
        for (MoveVector mv : filterInitialAndNonCaptureMoveVectors()) {
            if (mv.hasInterval()) {
                if (mv.isLeaping()) {
                    // don't need to check interval.
                    if (mv.isCaptureOnly()) captureMovesThreat(mv, ls);
                    else standardMoves(mv, ls);
                } else {
                    // must check interval, check intermediate steps.
                    //TODO: fix this...
                    if (mv.isCaptureOnly()) captureMovesThreat(mv, ls);
                    else standardMoves(mv, ls);
                    // isHopping should be handled here.
                }
                continue;
            }
            // move has no interval - basic move in a single direction.
            if (mv.isCaptureOnly()) captureMovesThreat(mv, ls);
            else standardMoves(mv, ls);

        }
        return ls;
    }

    public boolean isKing() {
        return this instanceof King;
    }

    public void setBoardContainer(BoardContainer boardContainer) {
        this.game = boardContainer;
    }
}
