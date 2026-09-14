package com.chessfx.engine;

import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.pieces.Team;
import com.chessfx.engine.tiles.TileParent;

import java.util.ArrayDeque;
import java.util.List;

public class BoardContainer {

    protected final GameStateModel gameModel;
    protected TileParent[][] board;
    // TODO: CONSIDER HYPOTHETICAL
    protected TileParent[][] hypotheticalBoard;
    protected ArrayDeque<Move> moveStack = new ArrayDeque<>();

    public BoardContainer(GameStateModel gameModel, TileParent[][] board) {
        this.gameModel = gameModel;
        this.board = board;
    }

    public boolean inBoardBounds(int y, int x) {
        return 0 <= y && y < gameModel.getHeight()
                && 0 <= x && x < gameModel.getWidth();
    }

    public boolean tileHasPiece(int rank, int file) {
        return board[rank][file].hasPiece();
    }

    public boolean tileHasPieceMatchingTeam(int y, int x) {
        return board[y][x].hasPieceMatchingTeam(gameModel.getCurrentTeam());
    }

    // shouldn't have to pass the team, it must be currentTeam
    public boolean isPossibleMove(Team team, int rank, int file) {
        return !board[rank][file].hasPieceMatchingTeam(team);
    }

    public TileParent[][] getBoard() {
        return board;
    }

    public void terminate() {
        // do nothing. Equivalent to an abstract method
    }

    public Team getCurrentTeam() {
        return gameModel.getCurrentTeam();
    }

    /**
     * Internally completes the move on the TileParent array board.
     * <p>
     * It removes the piece from the TileParent addressed by the initial coordinates of the move,
     * and places that piece onto the TileParent addressed by the destination coordinates of the move.
     * <p/>
     *
     * @param move the move being done
     */
    public void doMove(Move move) {
        Piece piece = board[move.y1][move.x1].removePiece();
        piece.setCoOrds(move.y2, move.x2);
        piece.setHasMoved(true);
        if (move.isCapture) {
            // for things like en passant
            if (move.hasCapturePiece()) {
                getCurrentTeam().addCapture(move.capture);
                // handle capture
            }
        }
        board[move.y2][move.x2].setPiece(piece);
        updateGame(move);
    }

    protected void updateGame(Move move) {
        gameModel.cycleTeam();
        moveStack.push(move);
    }

    public void undoMove(Move move) {
        Piece piece = board[move.y2][move.x2].removePiece();
        piece.setCoOrds(move.y1, move.x1);
        piece.setHasMoved(move.pieceHasMoved());
        if (move.isCapture) {
            // for things like en passant
            if (move.hasCapturePiece()) {
                getCurrentTeam().removeCapture(move.capture);
                // handle capture
            }
        }
        board[move.y1][move.x1].setPiece(piece);
        undoUpdateGame(move);
    }

    private void undoUpdateGame(Move move) {
        gameModel.cycleTeamBackwards();
        moveStack.pop();
    }


    public TileParent getTile(int y, int x) {
        return board[y][x];
    }

    public List<Move> getValidatedMoves(List<Move> unvalidatedMoves) {
        // validate moves by checking if causes team side check.
        // gotta move hypothetically.
        // need hypothetical board.


        return unvalidatedMoves;
    }
}
