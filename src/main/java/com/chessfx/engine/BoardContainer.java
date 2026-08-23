package com.chessfx.engine;

import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.pieces.Team;
import com.chessfx.engine.tiles.TileParent;

import java.util.ArrayDeque;

public class BoardContainer {

    protected final GameStateModel gameModel;
    protected TileParent[][] board;
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
        // do nothing. Equivilant to an abstract method
    }

    public Team getCurrentTeam() {
        return gameModel.getCurrentTeam();
    }

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


    public TileParent getTile(int y, int x) {
        return board[y][x];
    }
}
